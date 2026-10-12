package com.interntrack.service;

import com.interntrack.dto.AdminStatsResponse;
import com.interntrack.dto.StudentResponse;
import com.interntrack.model.ApplicationStatus;
import com.interntrack.model.Internship;
import com.interntrack.model.Role;
import com.interntrack.model.User;
import com.interntrack.repository.InternshipRepository;
import com.interntrack.repository.OfferRepository;
import com.interntrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final OfferRepository offerRepository;
    private final OfferService offerService;

    public DashboardService(UserRepository userRepository,
                            InternshipRepository internshipRepository,
                            OfferRepository offerRepository,
                            OfferService offerService) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.offerRepository = offerRepository;
        this.offerService = offerService;
    }

    public AdminStatsResponse stats() {
        offerService.expirePending();
        AdminStatsResponse stats = new AdminStatsResponse();
        stats.setTotalStudents(userRepository.countByRole(Role.STUDENT));
        stats.setTotalInternships(internshipRepository.count());
        stats.setTotalOffers(offerRepository.count());
        stats.setPendingOffers(offerRepository.countByStatus(com.interntrack.model.OfferStatus.PENDING));

        long overdue = internshipRepository.findAllByOrderByDeadlineAsc().stream()
                .filter(i -> !i.getStatus().isTerminal() && i.isOverdue())
                .count();
        stats.setOverdueDeadlines(overdue);

        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            byStatus.put(status.name(), internshipRepository.countByStatus(status));
        }
        stats.setInternshipsByStatus(byStatus);
        return stats;
    }

    public List<StudentResponse> students() {
        List<User> students = userRepository.findByRoleOrderByNameAsc(Role.STUDENT);
        return students.stream().map(u -> {
            StudentResponse dto = StudentResponse.from(u);
            List<Internship> internships = internshipRepository.findByUser_UserIdOrderByDeadlineAsc(u.getUserId());
            dto.setInternshipCount(internships.size());
            dto.setSelectedCount(internships.stream()
                    .filter(i -> i.getStatus() == ApplicationStatus.SELECTED).count());
            dto.setOfferCount(offerRepository.countByUser_UserId(u.getUserId()));
            dto.setPendingOffers(offerRepository.findByUser_UserIdOrderByCreatedAtDesc(u.getUserId()).stream()
                    .filter(o -> o.getStatus() == com.interntrack.model.OfferStatus.PENDING).count());
            return dto;
        }).toList();
    }
}
