package com.company.enterprise.service;

import com.company.enterprise.dto.JwtResponse;
import com.company.enterprise.dto.LoginRequest;
import com.company.enterprise.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
}
