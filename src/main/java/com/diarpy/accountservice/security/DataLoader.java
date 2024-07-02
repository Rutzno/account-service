package com.diarpy.accountservice.security;

import com.diarpy.accountservice.entities.Group;
import com.diarpy.accountservice.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

@Component
public class DataLoader {
    private final GroupRepository groupRepository;

    @Autowired
    public DataLoader(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {
        groupRepository.save(new Group(1L, "ROLE_ADMINISTRATOR"));
        groupRepository.save(new Group(2L, "ROLE_USER"));
        groupRepository.save(new Group(3L, "ROLE_ACCOUNTANT"));
        groupRepository.save(new Group(4L, "ROLE_AUDITOR"));
    }
}