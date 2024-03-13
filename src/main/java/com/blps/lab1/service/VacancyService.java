package com.blps.lab1.service;

import com.blps.lab1.util.Result;
import com.blps.lab1.model.Vacancy;
import com.blps.lab1.repo.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;

    public Result validateVacancy(Vacancy vacancy){
        if(vacancy.getAuthorId() == null) return Result.NO_VACANCY_AUTHOR;
        if(vacancy.getTitle() == null) return Result.NO_VACANCY_TITLE;
        if(vacancy.getDescription() == null)  return Result.NO_VACANCY_DESCRIPTION;
        return Result.OK;
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
