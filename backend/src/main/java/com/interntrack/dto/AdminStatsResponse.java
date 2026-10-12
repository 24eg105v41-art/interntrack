package com.interntrack.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class AdminStatsResponse {

    private long totalStudents;
    private long totalInternships;
    private long totalOffers;
    private long pendingOffers;
    private long overdueDeadlines;
    private Map<String, Long> internshipsByStatus = new LinkedHashMap<>();

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalInternships() {
        return totalInternships;
    }

    public void setTotalInternships(long totalInternships) {
        this.totalInternships = totalInternships;
    }

    public long getTotalOffers() {
        return totalOffers;
    }

    public void setTotalOffers(long totalOffers) {
        this.totalOffers = totalOffers;
    }

    public long getPendingOffers() {
        return pendingOffers;
    }

    public void setPendingOffers(long pendingOffers) {
        this.pendingOffers = pendingOffers;
    }

    public long getOverdueDeadlines() {
        return overdueDeadlines;
    }

    public void setOverdueDeadlines(long overdueDeadlines) {
        this.overdueDeadlines = overdueDeadlines;
    }

    public Map<String, Long> getInternshipsByStatus() {
        return internshipsByStatus;
    }

    public void setInternshipsByStatus(Map<String, Long> internshipsByStatus) {
        this.internshipsByStatus = internshipsByStatus;
    }
}
