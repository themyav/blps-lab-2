package com.blps.lab1.component;

import com.blps.lab1.model.Privilege;
import com.blps.lab1.model.Role;
import com.blps.lab1.model.User;
import com.blps.lab1.repo.PrivilegeRepository;
import com.blps.lab1.repo.RoleRepository;
import com.blps.lab1.repo.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    //@Autowired
    //private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;
        Privilege postOwnVacancy = createPrivilegeIfNotFound("POST_VACANCY");
        Privilege deleteOwnVacancy = createPrivilegeIfNotFound("DELETE_OWN_VACANCY");
        Privilege viewAllVacancy = createPrivilegeIfNotFound("VIEW_ALL_VACANCY");
        Privilege deleteAnyVacancy = createPrivilegeIfNotFound("DELETE_ANY_VACANCY");
        Privilege depositOwnBalance = createPrivilegeIfNotFound("DEPOSIT_OWN_BALANCE");
        Privilege checkOwnBalance = createPrivilegeIfNotFound("CHECK_OWN_BALANCE");
        Privilege checkAnyBalance = createPrivilegeIfNotFound("CHECK_ANY_BALANCE");
        Privilege assignModerator = createPrivilegeIfNotFound("ASSIGN_MODERATOR");
        Privilege removeModerator = createPrivilegeIfNotFound("REMOVE_MODERATOR");

        List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(postOwnVacancy, deleteOwnVacancy, viewAllVacancy, depositOwnBalance, checkOwnBalance));
        createRoleIfNotFound("ROLE_USER", userPrivileges);

        List<Privilege> moderatorPrivileges = new ArrayList<>(Arrays.asList(deleteAnyVacancy, checkAnyBalance));
        moderatorPrivileges.addAll(userPrivileges);
        createRoleIfNotFound("ROLE_MODERATOR", moderatorPrivileges);

        List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(assignModerator, removeModerator));
        adminPrivileges.addAll(moderatorPrivileges);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        //user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Collections.singletonList(adminRole));
        //user.setEnabled(true);
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    void createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }
}
