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
        Privilege postOwnVacancy = privilegeService.create("POST_VACANCY");
        Privilege deleteOwnVacancy = privilegeService.create("DELETE_OWN_VACANCY");
        Privilege viewAllVacancy = privilegeService.create("VIEW_ALL_VACANCY");
        Privilege deleteAnyVacancy = privilegeService.create("DELETE_ANY_VACANCY");
        Privilege depositOwnBalance = privilegeService.create("DEPOSIT_OWN_BALANCE");
        Privilege checkOwnBalance = privilegeService.create("CHECK_OWN_BALANCE");
        Privilege checkAnyBalance = privilegeService.create("CHECK_ANY_BALANCE");
        Privilege assignModerator = privilegeService.create("ASSIGN_MODERATOR");
        Privilege removeModerator = privilegeService.create("REMOVE_MODERATOR");

        List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(postOwnVacancy, deleteOwnVacancy, viewAllVacancy, depositOwnBalance, checkOwnBalance));
        roleService.create("ROLE_USER", userPrivileges);

        List<Privilege> moderatorPrivileges = new ArrayList<>(Arrays.asList(deleteAnyVacancy, checkAnyBalance));
        moderatorPrivileges.addAll(userPrivileges);
        roleService.create("ROLE_MODERATOR", moderatorPrivileges);

        List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(assignModerator, removeModerator));
        adminPrivileges.addAll(moderatorPrivileges);
        roleService.create("ROLE_ADMIN", adminPrivileges);

        Role adminRole = roleService.findByName("ROLE_ADMIN");
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        //user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Collections.singletonList(adminRole));
        //user.setEnabled(true);
        userService.save(user);

        alreadySetup = true;
    }

}
