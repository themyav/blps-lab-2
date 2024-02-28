package com.blps.lab1.repo;

import com.blps.lab1.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}

