package com.diarpy.accountservice.service;

import com.diarpy.accountservice.entities.SecurityEvent;
import com.diarpy.accountservice.repository.SecurityEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

@Service
public class SecurityEventService {
    @Autowired
    private SecurityEventRepository securityEventRepository;

    public void save(String action, String subject, String object, String path) {
        SecurityEvent se = new SecurityEvent(action, subject, object, path);
        securityEventRepository.save(se);
    }
}