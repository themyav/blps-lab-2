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

    public Result checkVacancy(Vacancy vacancy){
        if(vacancy.getAuthorId() == null) return new Result(1, "Не указан автор");
        if(vacancy.getTitle() == null) return new Result(2, "Не указано название");
        if(vacancy.getDescription() == null)  return new Result(2, "Не указано описание");
        return new Result(0, "ОК");
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
