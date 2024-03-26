package com.blps.lab1.controller;

import com.blps.lab1.util.ModeratorComment;
import com.blps.lab1.util.Result;
import com.blps.lab1.service.BalanceService;
import com.blps.lab1.service.VacancyService;
import com.blps.lab1.model.Vacancy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacancy")

public class VacancyController {
    @Autowired
    private VacancyService vacancyService;

    @GetMapping("/published")
    public ResponseEntity<List<Vacancy>> getAll(){
        return ResponseEntity.ok(vacancyService.getAllPublished());
    }

    @GetMapping("/moderation")
    public ResponseEntity<List<Vacancy>> getAllForModeration(){
        return ResponseEntity.ok(vacancyService.getAllForModeration());
    }

    @GetMapping("vacancy/moderation/{id}/publish")
    public ResponseEntity<?> publishModerated(@PathVariable Long id){
        Vacancy vacancy = vacancyService.publishModerated(id);
        if(vacancy != null) return ResponseEntity.ok(vacancy);
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.VACANCY_NOT_FOUND.name());
    }

    @PostMapping("vacancy/moderation/{id}/decline")
    public ResponseEntity<?> declineModerated(@PathVariable Long id, @RequestBody ModeratorComment comment){
        Vacancy vacancy = vacancyService.declineModerated(id);
        if(vacancy != null) return ResponseEntity.ok(comment.getComment());
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.VACANCY_NOT_FOUND.name());
    }


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

        Result publishResult = vacancyService.processPublication(vacancy);

        if (publishResult == Result.OK) {
            return ResponseEntity.ok(vacancy); //TODO maybe published vacancy
        } else {
            String errorMessage = publishResult.getMessage();
            if (publishResult == Result.USER_NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
        }
    }
}
