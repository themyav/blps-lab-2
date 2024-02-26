package com.blps.lab1.controller;

import com.blps.lab1.service.VacancyService;
import com.blps.lab1.entity.Vacancy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vacancy")

public class VacancyController {
    @Autowired
    private VacancyService vacancyService;

    @PostMapping("/draft")
    public ResponseEntity<Vacancy> saveVacancyAsDraft(@RequestBody Vacancy vacancy) {
        Vacancy savedVacancy = vacancyService.saveAsDraft(vacancy);
        return ResponseEntity.ok(savedVacancy);
    }

    @PostMapping("/publish")
    public ResponseEntity<Vacancy> publishVacancy(@RequestBody Vacancy vacancy) {
        Vacancy publishedVacancy = vacancyService.publish(vacancy);
        return ResponseEntity.ok(publishedVacancy);
    }
}
