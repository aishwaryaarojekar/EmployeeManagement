-- Reference schema (Hibernate auto-creates these tables on startup via ddl-auto=update).
-- Kept here for documentation / manual MySQL setup if you disable auto-ddl.

CREATE DATABASE IF NOT EXISTS enterprise_management;
USE enterprise_management;

CREATE TABLE IF NOT EXISTS departments (
    department_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    department_code VARCHAR(20) NOT NULL UNIQUE,
    department_name VARCHAR(100) NOT NULL,
    department_head VARCHAR(100),
    location VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS employees (
    employee_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_code VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    date_of_joining DATE NOT NULL,
    designation VARCHAR(100),
    salary DECIMAL(12,2),
    employment_type VARCHAR(20),
    department_id BIGINT,
    location VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

CREATE TABLE IF NOT EXISTS projects (
    project_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_code VARCHAR(20) NOT NULL UNIQUE,
    project_name VARCHAR(150) NOT NULL,
    description VARCHAR(2000),
    client_name VARCHAR(150),
    start_date DATE,
    end_date DATE,
    budget DECIMAL(15,2),
    project_manager_id BIGINT,
    project_status VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
    priority VARCHAR(20) DEFAULT 'MEDIUM',
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (project_manager_id) REFERENCES employees(employee_id)
);

CREATE TABLE IF NOT EXISTS project_allocations (
    allocation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    role VARCHAR(100),
    allocation_percentage INT NOT NULL,
    start_date DATE,
    end_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
    FOREIGN KEY (project_id) REFERENCES projects(project_id)
);

CREATE TABLE IF NOT EXISTS leave_requests (
    leave_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    leave_type VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    approved_by VARCHAR(100),
    approved_date DATETIME,
    created_at DATETIME,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    employee_id BIGINT,
    enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

CREATE TABLE IF NOT EXISTS audit_logs (
    audit_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_name VARCHAR(100),
    entity_id VARCHAR(50),
    timestamp DATETIME NOT NULL,
    ip_address VARCHAR(50)
);
