package com.blps.lab3.controller;

import com.blps.lab3.util.ModeratorComment;
import com.blps.lab3.util.enums.Result;
import com.blps.lab3.service.VacancyService;
import com.blps.lab3.model.Vacancy;
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

    @GetMapping("/moderation/{id}/publish")
    public ResponseEntity<?> publishModerated(@PathVariable Long id){
        Result result = vacancyService.publishModerated(id);
        if(result == Result.OK) return ResponseEntity.ok().body(result.getMessage());
        else return ResponseEntity.badRequest().body(result.getMessage());
    }

    @PostMapping("/moderation/{id}/decline")
    public ResponseEntity<?> declineModerated(@PathVariable Long id, @RequestBody String comment){
        Result result = vacancyService.declineModerated(id, new ModeratorComment(comment));
        if(result == Result.OK) return ResponseEntity.ok().body(result.getMessage());
        else return ResponseEntity.badRequest().body(result.getMessage());
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
            return ResponseEntity.ok(publishResult); //TODO maybe published vacancy
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
