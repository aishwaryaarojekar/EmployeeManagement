package com.company.enterprise.util;

import java.util.UUID;

public class CodeGenerator {
    public static String employeeCode() {
        return "EMP" + (1000 + (int) (Math.random() * 9000));
    }
    public static String projectCode() {
        return "PRJ" + (1000 + (int) (Math.random() * 9000));
    }
    public static String departmentCode() {
        return "DEP" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}
