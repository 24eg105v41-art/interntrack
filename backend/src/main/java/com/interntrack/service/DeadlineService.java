package com.interntrack.service;

import com.interntrack.dto.DeadlineResponse;
import com.interntrack.model.Internship;
import com.interntrack.model.Offer;
import com.interntrack.model.OfferStatus;
import com.interntrack.repository.InternshipRepository;
import com.interntrack.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DeadlineService {

    private final InternshipRepository internshipRepository;
    private final OfferRepository offerRepository;
    private final OfferService offerService;
    private final int soonDays;

    public DeadlineService(InternshipRepository internshipRepository,
                           OfferRepository offerRepository,
                           OfferService offerService,
                           @Value("${interntrack.deadline.soon-days:3}") int soonDays) {
        this.internshipRepository = internshipRepository;
        this.offerRepository = offerRepository;
        this.offerService = offerService;
        this.soonDays = soonDays;
    }

    public List<DeadlineResponse> forUser(Long userId) {
        LocalDate today = LocalDate.now();
        List<DeadlineResponse> deadlines = new ArrayList<>();

        for (Internship i : internshipRepository.findByUser_UserIdOrderByDeadlineAsc(userId)) {
            deadlines.add(DeadlineResponse.of(
                    i.getInternshipId(), "INTERNSHIP",
                    i.getCompanyName() + " - " + i.getRoleTitle(),
                    "Application deadline (" + i.getStatus().getLabel() + ")",
                    i.getDeadline(), i.getDaysLeft(), urgency(i.getDeadline(), today)));
        }

        for (Offer o : offerRepository.findByUser_UserIdOrderByCreatedAtDesc(userId)) {
            if (o.getStatus() == OfferStatus.PENDING && o.getResponseDeadline() != null) {
                deadlines.add(DeadlineResponse.of(
                        o.getOfferId(), "OFFER",
                        o.getCompanyName() + " - " + o.getRoleTitle(),
                        "Offer response required",
                        o.getResponseDeadline(), o.getDaysToRespond(), urgency(o.getResponseDeadline(), today)));
            }
        }

        deadlines.sort(Comparator.comparing(DeadlineResponse::getDueDate));
        return deadlines;
    }

    public List<DeadlineResponse> all() {
        offerService.expirePending();
        LocalDate today = LocalDate.now();
        List<DeadlineResponse> deadlines = new ArrayList<>();

        for (Internship i : internshipRepository.findAllByOrderByDeadlineAsc()) {
            String student = i.getUser() == null ? "" : i.getUser().getName();
            deadlines.add(DeadlineResponse.of(
                    i.getInternshipId(), "INTERNSHIP",
                    i.getCompanyName() + " - " + i.getRoleTitle(),
                    student + " - application deadline (" + i.getStatus().getLabel() + ")",
                    i.getDeadline(), i.getDaysLeft(), urgency(i.getDeadline(), today)));
        }

        for (Offer o : offerRepository.findByStatusOrderByResponseDeadlineAsc(OfferStatus.PENDING)) {
            String student = o.getUser() == null ? "" : o.getUser().getName();
            deadlines.add(DeadlineResponse.of(
                    o.getOfferId(), "OFFER",
                    o.getCompanyName() + " - " + o.getRoleTitle(),
                    student + " - offer response required",
                    o.getResponseDeadline(), o.getDaysToRespond(), urgency(o.getResponseDeadline(), today)));
        }

        deadlines.sort(Comparator.comparing(DeadlineResponse::getDueDate));
        return deadlines;
    }

    private String urgency(LocalDate due, LocalDate today) {
        if (due.isBefore(today)) {
            return "OVERDUE";
        }
        if (due.isEqual(today)) {
            return "TODAY";
        }
        if (due.isBefore(today.plusDays(soonDays + 1))) {
            return "SOON";
        }
        return "UPCOMING";
    }
}
