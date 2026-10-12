package com.interntrack.dto;

import com.interntrack.model.User;

public class UserResponse {

    private Long userId;
    private String name;
    private String email;
    private String role;
    private String phone;
    private String college;
    private String branch;
    private Integer graduationYear;
    private Double cgpa;
    private String skills;

    public static UserResponse from(User u) {
        UserResponse dto = new UserResponse();
        dto.userId = u.getUserId();
        dto.name = u.getName();
        dto.email = u.getEmail();
        dto.role = u.getRole() == null ? null : u.getRole().name();
        dto.phone = u.getPhone();
        dto.college = u.getCollege();
        dto.branch = u.getBranch();
        dto.graduationYear = u.getGraduationYear();
        dto.cgpa = u.getCgpa();
        dto.skills = u.getSkills();
        return dto;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getCollege() {
        return college;
    }

    public String getBranch() {
        return branch;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public String getSkills() {
        return skills;
    }
}
