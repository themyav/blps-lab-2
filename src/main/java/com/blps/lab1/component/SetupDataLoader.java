package com.blps.lab1.component;

import com.blps.lab1.model.Privilege;
import com.blps.lab1.model.User;
import com.blps.lab1.service.BalanceService;
import com.blps.lab1.util.enums.ModeratorPrivileges;
import com.blps.lab1.util.enums.UserPrivileges;
import com.blps.lab1.service.PrivilegeService;
import com.blps.lab1.service.RoleService;
import com.blps.lab1.service.UserService;
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

        User moderator = new User("a", "a", "a", "$2a$12$jdwdPUMEqNBrxmjyxJhKBuExnSjjKgGu3.W/PcizEn8c4koXfj5Je");
        moderator.setRoles(Collections.singletonList(roleService.findByName("MODERATOR")));
        userService.save(moderator);
        balanceService.deposit(moderator.getId(), 1000.0);

        User user = new User("Иван", "Иванов", "Ivan@ivan.ru", "$2a$12$jdwdPUMEqNBrxmjyxJhKBuExnSjjKgGu3.W/PcizEn8c4koXfj5Je");
        user.setRoles(Collections.singletonList(roleService.findByName("USER")));
        userService.save(user);




        alreadySetup = true;
    }

}
