package com.blps.lab3.service;

import com.blps.lab3.util.ModeratorComment;
import com.blps.lab3.util.enums.Result;
import com.blps.lab3.model.Vacancy;
import com.blps.lab3.repo.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    public final TransactionTemplate transactionTemplate;

    @Autowired
    private RabbitMQService rabbitMQService;

    public VacancyService(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public List<Vacancy> getAllByUserId(Long id){
        return vacancyRepository.findAll().stream().filter(x -> Objects.equals(x.getAuthorId(), id)).toList();
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

    public Result processPublication(Vacancy vacancy){

        //проверка того, что вакансия содержит все необходимые поля
        Result validationResult = validateVacancy(vacancy);
        if(validationResult != Result.OK) return validationResult;
        //проверка того, что вакансия отправлена существующим пользователем
        Long userId = vacancy.getAuthorId();
        Result userValidation = balanceService.exist(userId);
        if(userValidation != Result.OK) return userValidation;
        //транзакция
        return publishAttempt(vacancy);

    }



    public Result publishAttempt(Vacancy vacancy){
        Long userId = vacancy.getAuthorId();

        return transactionTemplate.execute(status -> {
            Result freezeResult = balanceService.freeze(userId, balanceService.PUBLISH_COST);
            if(freezeResult != Result.OK) {
                status.setRollbackOnly();
                return freezeResult;
            }
            Vacancy savedVacancy = sendToModeration(vacancy);
            Result sendToModerators = rabbitMQService.sendModeratorNotification(savedVacancy);
            if(sendToModerators != Result.OK){
                status.setRollbackOnly();
                return sendToModerators;
            }
            return Result.OK;
        });
    }

    public Result declineModerated(Long id, ModeratorComment comment){
        //TODO duplicated defreeze
        return transactionTemplate.execute(status -> {
            Vacancy vacancy = vacancyRepository.findById(id).orElse(null);
            if(vacancy == null || !vacancy.isOnModeration()) return Result.VACANCY_NOT_FOUND;
            Long userId = vacancy.getAuthorId();
            System.out.println("AAAAA1");
            Result defreezeResult = balanceService.defreeze(userId, balanceService.PUBLISH_COST);
            if(defreezeResult != Result.OK) {
                status.setRollbackOnly();
                return defreezeResult;
            }
            System.out.println("AAAAA2");

            vacancy.setOnModeration(false);
            Vacancy notPublished = saveAsDraft(vacancy);

            Result sendResult = rabbitMQService.sendUserNotification(notPublished, comment);
            if(sendResult != Result.OK){
                status.setRollbackOnly();
                return sendResult;
            }
            return Result.OK;


        });
    }

    public Result publishModerated(Long id){

        return transactionTemplate.execute(status -> {
            Vacancy vacancy = vacancyRepository.findById(id).orElse(null);
            if(vacancy == null || !vacancy.isOnModeration()) return Result.VACANCY_NOT_FOUND;

            Long userId = vacancy.getAuthorId();
            Result defreezeResult = balanceService.defreeze(userId, balanceService.PUBLISH_COST);
            if(defreezeResult != Result.OK) {
                status.setRollbackOnly();
                return defreezeResult;
            }
            Vacancy published = publish(vacancy);
            Result withdrawResult = balanceService.withdraw(userId, balanceService.PUBLISH_COST);
            if(withdrawResult != Result.OK){
                status.setRollbackOnly();
                return withdrawResult;
            }
            Result sendResult = rabbitMQService.sendUserNotification(published, null);
            if(sendResult != Result.OK){
                status.setRollbackOnly();
                return sendResult;
            }
            return Result.OK;
        });
    }

    public Vacancy publish(Vacancy vacancy) {
        vacancy.setPublished(true);
        vacancy.setOnModeration(false);
        return vacancyRepository.save(vacancy);
    }


    public Vacancy sendToModeration(Vacancy vacancy) {
        vacancy.setPublished(false);
        vacancy.setOnModeration(true);
        return vacancyRepository.save(vacancy);
    }



    public Vacancy saveAsDraft(Vacancy vacancy) {
        vacancy.setPublished(false);
        return vacancyRepository.save(vacancy);
    }
}
