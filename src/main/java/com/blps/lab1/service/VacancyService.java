package com.blps.lab1.service;

import com.blps.lab1.entity.Vacancy;
import com.blps.lab1.repo.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;

    public Vacancy saveAsDraft(Vacancy vacancy) {
        vacancy.setPublished(false);
        return vacancyRepository.save(vacancy);
    }

    public Vacancy publish(Vacancy vacancy) {
        vacancy.setPublished(true);
        return vacancyRepository.save(vacancy);
    }
}
