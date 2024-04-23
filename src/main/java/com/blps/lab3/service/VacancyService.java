package com.blps.lab3.service;

import com.blps.lab3.model.User;
import com.blps.lab3.util.ModeratorNotification;
import com.blps.lab3.util.Result;
import com.blps.lab3.model.Vacancy;
import com.blps.lab3.repo.VacancyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class VacancyService {

    private final Integer maxDescriptionLength = 1000;
    private final Integer minDescriptionLength = 5;



    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    public final TransactionTemplate transactionTemplate;
    @Autowired
    private UserService userService;

    public VacancyService(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public List<Vacancy> getAllPublished(){
        return vacancyRepository.findAll().stream().filter(Vacancy::isPublished).toList();
    }

    public List<Vacancy>getAllForModeration(){
        return vacancyRepository.findAll().stream().filter(Vacancy::isOnModeration).toList();
    }

    public Result validateVacancy(Vacancy vacancy){
        if(vacancy.getAuthorId() == null) return Result.NO_VACANCY_AUTHOR;
        if(vacancy.getTitle() == null) return Result.NO_VACANCY_TITLE;
        if(vacancy.getDescription() == null)  return Result.NO_VACANCY_DESCRIPTION;
        return Result.OK;
    }

    //Auto-moderator
    public Boolean autoModerateVacancy(Vacancy vacancy){
        return !vacancy.getTitle().contains("1C") && !vacancy.getDescription().contains("1C")
                && (minDescriptionLength <= vacancy.getDescription().length() &&
                vacancy.getDescription().length() <= maxDescriptionLength);
    }

    public Result processPublication(Vacancy vacancy){

        //проверка того, что вакансия содержит все необходимые поля
        Result validationResult = validateVacancy(vacancy);
        if(validationResult.getCode() != 0) return validationResult;
        //проверка того, что вакансия отправлена существующим пользователем
        Long userId = vacancy.getAuthorId();
        Result userValidation = balanceService.exist(userId);
        if(userValidation.getCode() != 0) return userValidation;

        //транзакция
        return publishAttempt(vacancy);

    }

    @Autowired
    private Queue queue;

    @Autowired
    private MqttClient mqttClient;

    public void send(Vacancy vacancy) {

        List<User> moderators = userService.getModerators();
        ObjectMapper objectMapper = new ObjectMapper();
        moderators.forEach(moderator -> {
            try{
                String message = objectMapper.writeValueAsString(new ModeratorNotification(moderator.getEmail(), "У вас новая вакансия для модерации!", vacancy.toString()));
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttClient.publish(queue.getName(), mqttMessage);
                System.out.println(" [x] Sent '" + message + "' via MQTT");
            }catch (MqttException | JsonProcessingException e) {
                throw new RuntimeException(e); //TODO может умнее
            }
        });
    }

    public Result publishAttempt(Vacancy vacancy){
        Long userId = vacancy.getAuthorId();

        return transactionTemplate.execute(status -> {
            Result freezeResult = balanceService.freeze(userId, balanceService.PUBLISH_COST);
            if(freezeResult != Result.OK) {
                status.setRollbackOnly();
                return freezeResult;
            }
            System.out.println("AAAAAA");
            send(vacancy);

            return Result.OK;

        });
    }



     /*Boolean vacancyApproved = autoModerateVacancy(vacancy);


            Result defreezeResult = balanceService.defreeze(userId, balanceService.PUBLISH_COST);
            if(defreezeResult != Result.OK) {
                status.setRollbackOnly();
                return defreezeResult;
            }

            if(vacancyApproved){
                publish(vacancy);
                Result result = balanceService.withdraw(userId, balanceService.PUBLISH_COST);
                if(result != Result.OK){
                    status.setRollbackOnly();
                }
                return result;
            }
            else{
                Result result = balanceService.deposit(userId, balanceService.PUBLISH_COST);
                if(result != Result.OK){
                    status.setRollbackOnly();
                    return result;
                }
                else{
                    saveAsDraft(vacancy);
                    return Result.VACANCY_NOT_APPROVED;
                }

            }*/

    public Vacancy declineModerated(Long id){
        Vacancy vacancy = vacancyRepository.findById(id).orElse(null);
        if(vacancy != null && vacancy.isOnModeration()){
            vacancy.setOnModeration(false);
            return vacancyRepository.save(vacancy);
        }
        else{
            System.out.println("vacancy is null");
            return null;
        }
    }

    public Vacancy publishModerated(Long id){
        Vacancy vacancy = vacancyRepository.findById(id).orElse(null);
        if(vacancy != null && vacancy.isOnModeration()){
            return publish(vacancy);
        }
        else return null;
    }

    public Vacancy publish(Vacancy vacancy) {
        vacancy.setPublished(true);
        vacancy.setOnModeration(false);
        return vacancyRepository.save(vacancy);
    }


    public Vacancy sendToModeration(Vacancy vacancy) {
        vacancy.setOnModeration(true);
        return vacancyRepository.save(vacancy);
    }



    public Vacancy saveAsDraft(Vacancy vacancy) {
        vacancy.setPublished(false);
        return vacancyRepository.save(vacancy);
    }
}
