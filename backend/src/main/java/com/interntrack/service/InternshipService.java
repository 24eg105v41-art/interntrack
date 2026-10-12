package com.interntrack.service;

import com.interntrack.dto.InternshipRequest;
import com.interntrack.dto.InternshipResponse;
import com.interntrack.exception.ApiException;
import com.interntrack.model.ApplicationStatus;
import com.interntrack.model.Internship;
import com.interntrack.model.User;
import com.interntrack.model.WorkMode;
import com.interntrack.repository.InternshipRepository;
import com.interntrack.repository.OfferRepository;
import com.interntrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public InternshipService(InternshipRepository internshipRepository,
                             OfferRepository offerRepository,
                             UserRepository userRepository) {
        this.internshipRepository = internshipRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

    // ---- Student scope ----

    public List<InternshipResponse> listMine(Long userId, String status, String keyword) {
        ApplicationStatus statusFilter = parseStatusOrNull(status);
        Set<Long> offeredInternshipIds = offerRepository.findByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .filter(o -> o.getInternship() != null)
                .map(o -> o.getInternship().getInternshipId())
                .collect(Collectors.toSet());

        return internshipRepository.findByUser_UserIdOrderByDeadlineAsc(userId).stream()
                .filter(i -> statusFilter == null || i.getStatus() == statusFilter)
                .filter(i -> keyword == null || keyword.isBlank() || matches(i, keyword.toLowerCase(Locale.ROOT)))
                .map(i -> {
                    InternshipResponse dto = InternshipResponse.from(i);
                    dto.setHasOffer(offeredInternshipIds.contains(i.getInternshipId()));
                    return dto;
                })
                .toList();
    }

    public InternshipResponse getMine(Long userId, Long id) {
        Internship i = ownedInternship(userId, id);
        InternshipResponse dto = InternshipResponse.from(i);
        dto.setHasOffer(offerRepository.findByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .anyMatch(o -> o.getInternship() != null && o.getInternship().getInternshipId().equals(id)));
        return dto;
    }

    public InternshipResponse create(Long userId, InternshipRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(404, "User not found"));
        Internship i = new Internship();
        i.setUser(user);
        apply(i, req);
        internshipRepository.save(i);
        return InternshipResponse.from(i);
    }

    public InternshipResponse update(Long userId, Long id, InternshipRequest req) {
        Internship i = ownedInternship(userId, id);
        apply(i, req);
        internshipRepository.save(i);
        return InternshipResponse.from(i);
    }

    public InternshipResponse updateStatus(Long userId, Long id, String status) {
        Internship i = ownedInternship(userId, id);
        try {
            i.setStatus(ApplicationStatus.parse(status));
        } catch (IllegalArgumentException ex) {
            throw new ApiException(400, ex.getMessage());
        }
        internshipRepository.save(i);
        return InternshipResponse.from(i);
    }

    public void delete(Long userId, Long id) {
        Internship i = ownedInternship(userId, id);
        internshipRepository.delete(i);
    }

    private Internship ownedInternship(Long userId, Long id) {
        return internshipRepository.findByInternshipIdAndUser_UserId(id, userId)
                .orElseThrow(() -> new ApiException(404, "Internship not found"));
    }

    // ---- Admin scope ----

    public List<InternshipResponse> adminAll(String status, String keyword) {
        ApplicationStatus statusFilter = parseStatusOrNull(status);
        return internshipRepository.findAllByOrderByDeadlineAsc().stream()
                .filter(i -> statusFilter == null || i.getStatus() == statusFilter)
                .filter(i -> keyword == null || keyword.isBlank() || matches(i, keyword.toLowerCase(Locale.ROOT)))
                .map(InternshipResponse::from)
                .toList();
    }

    // ---- helpers ----

    private void apply(Internship i, InternshipRequest req) {
        i.setCompanyName(req.getCompanyName().trim());
        i.setRoleTitle(req.getRoleTitle().trim());
        i.setLocation(req.getLocation());
        try {
            i.setWorkMode(WorkMode.parse(req.getWorkMode()));
        } catch (IllegalArgumentException ex) {
            throw new ApiException(400, ex.getMessage());
        }
        i.setStipend(req.getStipend());
        i.setApplicationDate(req.getApplicationDate() == null
                ? (i.getApplicationDate() == null ? java.time.LocalDate.now() : i.getApplicationDate())
                : req.getApplicationDate());
        i.setDeadline(req.getDeadline());
        if (req.getStatus() != null && !req.getStatus().isBlank()) {
            try {
                i.setStatus(ApplicationStatus.parse(req.getStatus()));
            } catch (IllegalArgumentException ex) {
                throw new ApiException(400, ex.getMessage());
            }
        }
        i.setSourceLink(req.getSourceLink());
        i.setNotes(req.getNotes());
    }

    private ApplicationStatus parseStatusOrNull(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return ApplicationStatus.parse(status);
        } catch (IllegalArgumentException ex) {
            throw new ApiException(400, ex.getMessage());
        }
    }

    private boolean matches(Internship i, String keyword) {
        String haystack = (nz(i.getCompanyName()) + " " + nz(i.getRoleTitle()) + " "
                + nz(i.getLocation()) + " " + nz(i.getNotes())).toLowerCase(Locale.ROOT);
        return haystack.contains(keyword);
    }

    private String nz(String s) {
        return s == null ? "" : s;
    }
}
