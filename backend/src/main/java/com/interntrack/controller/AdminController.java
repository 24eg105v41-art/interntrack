package com.interntrack.controller;

import com.interntrack.dto.AdminStatsResponse;
import com.interntrack.dto.DeadlineResponse;
import com.interntrack.dto.InternshipResponse;
import com.interntrack.dto.OfferResponse;
import com.interntrack.dto.StudentResponse;
import com.interntrack.service.DashboardService;
import com.interntrack.service.DeadlineService;
import com.interntrack.service.InternshipService;
import com.interntrack.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final DashboardService dashboardService;
    private final InternshipService internshipService;
    private final OfferService offerService;
    private final DeadlineService deadlineService;

    public AdminController(DashboardService dashboardService,
                           InternshipService internshipService,
                           OfferService offerService,
                           DeadlineService deadlineService) {
        this.dashboardService = dashboardService;
        this.internshipService = internshipService;
        this.offerService = offerService;
        this.deadlineService = deadlineService;
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> stats() {
        return ResponseEntity.ok(dashboardService.stats());
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> students() {
        return ResponseEntity.ok(dashboardService.students());
    }

    @GetMapping("/internships")
    public ResponseEntity<List<InternshipResponse>> internships(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(internshipService.adminAll(status, keyword));
    }

    @GetMapping("/offers")
    public ResponseEntity<List<OfferResponse>> offers() {
        return ResponseEntity.ok(offerService.adminAll());
    }

    @GetMapping("/deadlines")
    public ResponseEntity<List<DeadlineResponse>> deadlines() {
        return ResponseEntity.ok(deadlineService.all());
    }
}
