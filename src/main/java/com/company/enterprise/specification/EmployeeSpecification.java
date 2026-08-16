package com.company.enterprise.specification;

import com.company.enterprise.entity.Employee;
import com.company.enterprise.entity.EmployeeStatus;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasDepartment(String departmentName) {
        return (root, query, cb) -> departmentName == null ? null :
                cb.equal(cb.lower(root.join("department").get("departmentName")), departmentName.toLowerCase());
    }

    public static Specification<Employee> hasDesignation(String designation) {
        return (root, query, cb) -> designation == null ? null :
                cb.equal(cb.lower(root.get("designation")), designation.toLowerCase());
    }

    public static Specification<Employee> hasLocation(String location) {
        return (root, query, cb) -> location == null ? null :
                cb.equal(cb.lower(root.get("location")), location.toLowerCase());
    }

    public static Specification<Employee> hasStatus(EmployeeStatus status) {
        return (root, query, cb) -> status == null ? null :
                cb.equal(root.get("status"), status);
    }
}
