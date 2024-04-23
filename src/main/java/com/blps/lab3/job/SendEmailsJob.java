package com.blps.lab3.job;

import com.blps.lab3.model.User;
import com.blps.lab3.model.Vacancy;
import com.blps.lab3.service.RabbitMQService;
import com.blps.lab3.service.UserService;
import com.blps.lab3.service.VacancyService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class SendEmailsJob implements Job {
    private final UserService userService;
    private final RabbitMQService rabbitMQService;
    private final VacancyService vacancyService;

    public SendEmailsJob(UserService userService, RabbitMQService rabbitMQService, VacancyService vacancyService) {
        this.userService = userService;
        this.rabbitMQService = rabbitMQService;
        this.vacancyService = vacancyService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        List<User>users = userService.getSubscribedUsers();
        if(users == null) return;
        for(User user : users){
            List<Vacancy>vacancies =  vacancyService.getAllByUserId(user.getId());
            if(vacancies.isEmpty()) continue;
            rabbitMQService.sendUserStats(user, vacancies);
        }
    }
}
