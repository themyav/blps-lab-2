package com.blps.lab3.component;

import com.blps.lab3.model.Privilege;
import com.blps.lab3.model.User;
import com.blps.lab3.model.Vacancy;
import com.blps.lab3.service.*;
import com.blps.lab3.util.enums.ModeratorPrivileges;
import com.blps.lab3.util.enums.UserPrivileges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;
        ArrayList<Privilege>userPrivilege = new ArrayList<>();
        for(UserPrivileges privileges : UserPrivileges.values()){
            userPrivilege.add(privilegeService.create(privileges.name()));
        }

        ArrayList<Privilege>moderatorPrivilege = new ArrayList<>();
        for(ModeratorPrivileges privileges : ModeratorPrivileges.values()){
            moderatorPrivilege.add(privilegeService.create(privileges.name()));
        }
        roleService.create("USER", userPrivilege);

        moderatorPrivilege.addAll(userPrivilege);
        roleService.create("MODERATOR", moderatorPrivilege);

        User moderator = new User("Кристина", "Мявук", "krystyna-myar@mail.ru", "$2a$12$jdwdPUMEqNBrxmjyxJhKBuExnSjjKgGu3.W/PcizEn8c4koXfj5Je");
        moderator.setRoles(Collections.singletonList(roleService.findByName("MODERATOR")));
        userService.save(moderator);
        balanceService.deposit(moderator.getId(), 1000.0);

        User user = new User("Иван", "Иванов", "Myavochka1119@gmail.com", "$2a$12$jdwdPUMEqNBrxmjyxJhKBuExnSjjKgGu3.W/PcizEn8c4koXfj5Je");
        user.setRoles(Collections.singletonList(roleService.findByName("USER")));
        userService.save(user);

        Vacancy dvornik = new Vacancy(user, "Дворник", "Мести пол");
        dvornik.setOnModeration(true);
        vacancyService.sendToModeration(dvornik);

        userService.setModerators();
        userService.setUsers();


        alreadySetup = true;
    }

}
