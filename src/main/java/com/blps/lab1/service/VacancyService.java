package com.blps.lab1.service;

import com.blps.lab1.repo.BalanceRepository;
import com.blps.lab1.util.Result;
import com.blps.lab1.model.Vacancy;
import com.blps.lab1.repo.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    public final TransactionTemplate transactionTemplate;

    public VacancyService(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
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
        if(validationResult.getCode() != 0) return validationResult;

        //проверка того, что вакансия отправлена существующим пользователем
        Long userId = vacancy.getAuthorId();
        Result userValidation = balanceService.exist(userId);
        if(userValidation.getCode() != 0) return userValidation;

        //транзакция
        return publishAttempt(vacancy);

    }

    //TODO типа тракзакция, но rollback следует делать ток если проблемы?
    public Result publishAttempt(Vacancy vacancy){
        Long userId = vacancy.getAuthorId();

        return transactionTemplate.execute(status -> {
            Result freezeResult = balanceService.freeze(userId, balanceService.PUBLISH_COST);
            if(freezeResult != Result.OK) {
                status.setRollbackOnly();
                return freezeResult;
            }

            Boolean vacancyApproved = moderateVacancy(vacancy);

            Result defreezeResult = balanceService.defreeze(userId, balanceService.PUBLISH_COST);
            if(defreezeResult != Result.OK) {
                status.setRollbackOnly();
                return defreezeResult;
            }

            if(vacancyApproved){
                return balanceService.withdraw(userId, balanceService.PUBLISH_COST);
            }
            else{
                return balanceService.deposit(userId, balanceService.PUBLISH_COST);
            }
        });
    }



    //Auto-moderator
    public Boolean moderateVacancy(Vacancy vacancy){
        return !vacancy.getTitle().contains("1C");
    }

    public Vacancy saveAsDraft(Vacancy vacancy) {
        vacancy.setPublished(false);
        return vacancyRepository.save(vacancy);
    }

    public Vacancy publish(Vacancy vacancy) {
        vacancy.setPublished(true);
        return vacancyRepository.save(vacancy);
    }
}
