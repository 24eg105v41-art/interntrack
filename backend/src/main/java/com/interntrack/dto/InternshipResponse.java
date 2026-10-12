package com.interntrack.dto;

import com.interntrack.model.Internship;
import com.interntrack.model.WorkMode;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InternshipResponse {

    private Long internshipId;
    private Long userId;
    private String companyName;
    private String roleTitle;
    private String location;
    private String workMode;
    private String workModeLabel;
    private Integer stipend;
    private LocalDate applicationDate;
    private LocalDate deadline;
    private long daysLeft;
    private boolean overdue;
    private String status;
    private String statusLabel;
    private int stage;
    private boolean terminal;
    private String sourceLink;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean hasOffer;

    // admin-only view helper
    private String studentName;
    private String studentEmail;

    public static InternshipResponse from(Internship i) {
        InternshipResponse dto = new InternshipResponse();
        dto.internshipId = i.getInternshipId();
        dto.userId = i.getUser() == null ? null : i.getUser().getUserId();
        if (i.getUser() != null) {
            dto.studentName = i.getUser().getName();
            dto.studentEmail = i.getUser().getEmail();
        }
        dto.companyName = i.getCompanyName();
        dto.roleTitle = i.getRoleTitle();
        dto.location = i.getLocation();
        dto.workMode = i.getWorkMode() == null ? WorkMode.ONSITE.name() : i.getWorkMode().name();
        dto.workModeLabel = i.getWorkMode() == null ? WorkMode.ONSITE.getLabel() : i.getWorkMode().getLabel();
        dto.stipend = i.getStipend();
        dto.applicationDate = i.getApplicationDate();
        dto.deadline = i.getDeadline();
        dto.daysLeft = i.getDaysLeft();
        dto.overdue = i.isOverdue();
        dto.status = i.getStatus().name();
        dto.statusLabel = i.getStatus().getLabel();
        dto.stage = i.getStatus().getStage();
        dto.terminal = i.getStatus().isTerminal();
        dto.sourceLink = i.getSourceLink();
        dto.notes = i.getNotes();
        dto.createdAt = i.getCreatedAt();
        dto.updatedAt = i.getUpdatedAt();
        return dto;
    }

    public Long getInternshipId() {
        return internshipId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public String getLocation() {
        return location;
    }

    public String getWorkMode() {
        return workMode;
    }

    public String getWorkModeLabel() {
        return workModeLabel;
    }

    public Integer getStipend() {
        return stipend;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public long getDaysLeft() {
        return daysLeft;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public int getStage() {
        return stage;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isHasOffer() {
        return hasOffer;
    }

    public void setHasOffer(boolean hasOffer) {
        this.hasOffer = hasOffer;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }
}
