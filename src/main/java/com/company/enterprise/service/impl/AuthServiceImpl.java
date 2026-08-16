package com.company.enterprise.service.impl;

import com.company.enterprise.dto.JwtResponse;
import com.company.enterprise.dto.LoginRequest;
import com.company.enterprise.dto.RegisterRequest;
import com.company.enterprise.entity.Employee;
import com.company.enterprise.entity.User;
import com.company.enterprise.exception.DuplicateResourceException;
import com.company.enterprise.exception.ResourceNotFoundException;
import com.company.enterprise.repository.EmployeeRepository;
import com.company.enterprise.repository.UserRepository;
import com.company.enterprise.security.JwtUtil;
import com.company.enterprise.security.UserPrincipal;
import com.company.enterprise.service.AuditService;
import com.company.enterprise.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuditService auditService;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        Employee employee = null;
        if (request.getEmployeeId() != null) {
            employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + request.getEmployeeId()));
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .employee(employee)
                .enabled(true)
                .build();

        userRepository.save(user);
        auditService.log(user.getUserId(), "REGISTER", "User", user.getUsername(), null);
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtUtil.generateToken(principal);
        String role = principal.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        auditService.log(principal.getUserId(), "LOGIN", "User", principal.getUsername(), null);

        return JwtResponse.builder()
                .token(token)
                .username(principal.getUsername())
                .role(role)
                .expiresInMs(jwtUtil.getExpirationMs())
                .build();
    }
}
