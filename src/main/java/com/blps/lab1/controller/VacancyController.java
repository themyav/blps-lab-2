package com.blps.lab1.controller;

import com.blps.lab1.util.Result;
import com.blps.lab1.service.BalanceService;
import com.blps.lab1.service.VacancyService;
import com.blps.lab1.model.Vacancy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vacancy")

public class VacancyController {
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private BalanceService balanceService;

    @PostMapping("/draft")
    public ResponseEntity<?> saveVacancyAsDraft(@RequestBody Vacancy vacancy) {
        Result result = vacancyService.validateVacancy(vacancy);
        if(result.getCode() != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
        }
        Vacancy savedVacancy = vacancyService.saveAsDraft(vacancy);
        return ResponseEntity.ok(savedVacancy);

    }

    @PostMapping("/publish")
    public ResponseEntity<?> publishVacancy(@RequestBody Vacancy vacancy) {

        //проверка того, что вакансия содержит все необходимые поля
        Result validationResult = vacancyService.validateVacancy(vacancy);
        if(validationResult.getCode() != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getMessage());
        }

        //проверка того, что вакансия отправлена существующим пользователем
        Long userId = vacancy.getAuthorId();
        Result userValidation = balanceService.exist(userId);
        if(userValidation.getCode() != 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.getMessage());
        }


        Result withdrawalResult = balanceService.withdraw(userId, balanceService.PUBLISH_COST);

        if (withdrawalResult.getCode() == 0) {
            Vacancy publishedVacancy = vacancyService.publish(vacancy);
            return ResponseEntity.ok(publishedVacancy);
        } else {
            String errorMessage = withdrawalResult.getMessage();
            if (withdrawalResult.getCode() == 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
        }
    }
}
