package com.company.enterprise.service;

public interface AuditService {
    void log(Long userId, String action, String entityName, String entityId, String ipAddress);
}
