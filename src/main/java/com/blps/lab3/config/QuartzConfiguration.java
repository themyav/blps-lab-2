package com.blps.lab3.config;

import com.blps.lab3.job.SendEmailsJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail printJobDetail() {
        return JobBuilder.newJob(SendEmailsJob.class)
                .withIdentity("sendEmailJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger printJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(30)
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(printJobDetail())
                .withIdentity("sendEmailJobTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
