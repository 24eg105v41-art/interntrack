package com.interntrack.controller;

import com.interntrack.dto.InternshipRequest;
import com.interntrack.dto.InternshipResponse;
import com.interntrack.dto.StatusUpdateRequest;
import com.interntrack.service.AuthService;
import com.interntrack.service.InternshipService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internships")
public class InternshipController {

    private final InternshipService internshipService;
    private final AuthService authService;

    public InternshipController(InternshipService internshipService, AuthService authService) {
        this.internshipService = internshipService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<InternshipResponse>> mine(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(internshipService.listMine(authService.currentUserId(), status, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternshipResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(internshipService.getMine(authService.currentUserId(), id));
    }

    @PostMapping
    public ResponseEntity<InternshipResponse> create(@Valid @RequestBody InternshipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(internshipService.create(authService.currentUserId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InternshipResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody InternshipRequest request) {
        return ResponseEntity.ok(internshipService.update(authService.currentUserId(), id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InternshipResponse> updateStatus(@PathVariable Long id,
                                                           @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(internshipService.updateStatus(authService.currentUserId(), id, request.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        internshipService.delete(authService.currentUserId(), id);
        return ResponseEntity.ok(Map.of("message", "Internship deleted"));
    }
}
