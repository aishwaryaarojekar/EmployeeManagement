package com.company.enterprise.service.impl;

import com.company.enterprise.entity.AuditLog;
import com.company.enterprise.repository.AuditLogRepository;
import com.company.enterprise.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(Long userId, String action, String entityName, String entityId, String ipAddress) {
        AuditLog log = AuditLog.builder()
                .userId(userId)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .ipAddress(ipAddress)
                .build();
        auditLogRepository.save(log);
    }
}
