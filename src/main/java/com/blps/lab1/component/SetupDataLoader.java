package com.blps.lab1.component;

import com.blps.lab1.model.Privilege;
import com.blps.lab1.model.Role;
import com.blps.lab1.model.User;
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
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    //@Autowired
    //private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;
        Privilege postDraft = privilegeService.create("POST_DRAFT"); //+
        Privilege postOwnVacancy = privilegeService.create("POST_VACANCY"); //+

        Privilege deleteOwnVacancy = privilegeService.create("DELETE_OWN_VACANCY"); //no
        Privilege deleteAnyVacancy = privilegeService.create("DELETE_ANY_VACANCY"); //no

        Privilege viewAllVacancy = privilegeService.create("VIEW_ALL_VACANCY"); //+

        Privilege viewOwnDrafts = privilegeService.create("VIEW_OWN_DRAFTS"); //no
        Privilege viewAllDrafts = privilegeService.create("VIEW_ALL_DRAFTS"); //no
        Privilege depositOwnBalance = privilegeService.create("DEPOSIT_OWN_BALANCE"); //+

        Privilege checkOwnBalance = privilegeService.create("CHECK_OWN_BALANCE"); //no
        Privilege checkAnyBalance = privilegeService.create("CHECK_ANY_BALANCE"); //no

        Privilege assignModerator = privilegeService.create("ASSIGN_MODERATOR"); //no
        Privilege removeModerator = privilegeService.create("REMOVE_MODERATOR"); //no

        List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(postDraft, postOwnVacancy, deleteOwnVacancy, viewAllVacancy, depositOwnBalance, checkOwnBalance));
        roleService.create("USER", userPrivileges);

        List<Privilege> moderatorPrivileges = new ArrayList<>(Arrays.asList(deleteAnyVacancy, checkAnyBalance, viewAllDrafts));
        moderatorPrivileges.addAll(userPrivileges);
        roleService.create("MODERATOR", moderatorPrivileges);

        List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(assignModerator, removeModerator));
        adminPrivileges.addAll(moderatorPrivileges);
        roleService.create("ADMIN", adminPrivileges);

        Role adminRole = roleService.findByName("ADMIN");
        User user = new User("a", "a", "a", "$2a$12$jdwdPUMEqNBrxmjyxJhKBuExnSjjKgGu3.W/PcizEn8c4koXfj5Je");
        user.setRoles(Collections.singletonList(adminRole));
        userService.save(user);

        User user1 = new User("Иван", "Иванов", "Ivan@ivan.ru", "$2a$12$jdwdPUMEqNBrxmjyxJhKBuExnSjjKgGu3.W/PcizEn8c4koXfj5Je");
        user1.setRoles(Collections.singletonList(roleService.findByName("USER")));
        userService.save(user1);

        alreadySetup = true;
    }

}
