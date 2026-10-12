package com.interntrack.controller;

import com.interntrack.dto.OfferRequest;
import com.interntrack.dto.OfferResponse;
import com.interntrack.service.AuthService;
import com.interntrack.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferService offerService;
    private final AuthService authService;

    public OfferController(OfferService offerService, AuthService authService) {
        this.offerService = offerService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<OfferResponse>> mine() {
        return ResponseEntity.ok(offerService.listMine(authService.currentUserId()));
    }

    @PostMapping
    public ResponseEntity<OfferResponse> create(@Valid @RequestBody OfferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(offerService.create(authService.currentUserId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody OfferRequest request) {
        return ResponseEntity.ok(offerService.update(authService.currentUserId(), id, request));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<OfferResponse> accept(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.accept(authService.currentUserId(), id));
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<OfferResponse> decline(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.decline(authService.currentUserId(), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        offerService.delete(authService.currentUserId(), id);
        return ResponseEntity.ok(Map.of("message", "Offer deleted"));
    }
}
