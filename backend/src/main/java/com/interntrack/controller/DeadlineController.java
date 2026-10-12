package com.interntrack.controller;

import com.interntrack.dto.DeadlineResponse;
import com.interntrack.service.AuthService;
import com.interntrack.service.DeadlineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/deadlines")
public class DeadlineController {

    private final DeadlineService deadlineService;
    private final AuthService authService;

    public DeadlineController(DeadlineService deadlineService, AuthService authService) {
        this.deadlineService = deadlineService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<DeadlineResponse>> mine() {
        return ResponseEntity.ok(deadlineService.forUser(authService.currentUserId()));
    }
}
