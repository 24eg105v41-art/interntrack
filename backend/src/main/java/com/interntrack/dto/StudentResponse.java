package com.interntrack.dto;

import com.interntrack.model.User;

public class StudentResponse {

    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String college;
    private String branch;
    private Integer graduationYear;
    private Double cgpa;
    private String skills;

    private long internshipCount;
    private long offerCount;
    private long selectedCount;
    private long pendingOffers;

    public static StudentResponse from(User u) {
        StudentResponse dto = new StudentResponse();
        dto.userId = u.getUserId();
        dto.name = u.getName();
        dto.email = u.getEmail();
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

    public long getInternshipCount() {
        return internshipCount;
    }

    public void setInternshipCount(long internshipCount) {
        this.internshipCount = internshipCount;
    }

    public long getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(long offerCount) {
        this.offerCount = offerCount;
    }

    public long getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(long selectedCount) {
        this.selectedCount = selectedCount;
    }

    public long getPendingOffers() {
        return pendingOffers;
    }

    public void setPendingOffers(long pendingOffers) {
        this.pendingOffers = pendingOffers;
    }
}
