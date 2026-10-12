package com.interntrack.service;

import com.interntrack.dto.OfferRequest;
import com.interntrack.dto.OfferResponse;
import com.interntrack.exception.ApiException;
import com.interntrack.model.ApplicationStatus;
import com.interntrack.model.Internship;
import com.interntrack.model.Offer;
import com.interntrack.model.OfferStatus;
import com.interntrack.model.User;
import com.interntrack.repository.InternshipRepository;
import com.interntrack.repository.OfferRepository;
import com.interntrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final InternshipRepository internshipRepository;
    private final UserRepository userRepository;
    private final int defaultResponseDays;

    public OfferService(OfferRepository offerRepository,
                        InternshipRepository internshipRepository,
                        UserRepository userRepository,
                        @Value("${interntrack.offer.default-response-days:7}") int defaultResponseDays) {
        this.offerRepository = offerRepository;
        this.internshipRepository = internshipRepository;
        this.userRepository = userRepository;
        this.defaultResponseDays = defaultResponseDays;
    }

    public List<OfferResponse> listMine(Long userId) {
        expirePending();
        return offerRepository.findByUser_UserIdOrderByCreatedAtDesc(userId)
                .stream().map(OfferResponse::from).toList();
    }

    public OfferResponse create(Long userId, OfferRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(404, "User not found"));
        Offer offer = new Offer(user, req.getCompanyName().trim(), req.getRoleTitle().trim());
        offer.setStipend(req.getStipend());
        offer.setOfferDate(req.getOfferDate() == null ? LocalDate.now() : req.getOfferDate());
        offer.setJoiningDate(req.getJoiningDate());
        offer.setLetterLink(req.getLetterLink());
        offer.setNotes(req.getNotes());

        LocalDate responseDeadline = req.getResponseDeadline() == null
                ? LocalDate.now().plusDays(defaultResponseDays)
                : req.getResponseDeadline();
        if (responseDeadline.isBefore(LocalDate.now())) {
            throw new ApiException(400, "Response deadline cannot be in the past");
        }
        offer.setResponseDeadline(responseDeadline);

        if (req.getInternshipId() != null) {
            Internship internship = internshipRepository
                    .findByInternshipIdAndUser_UserId(req.getInternshipId(), userId)
                    .orElseThrow(() -> new ApiException(404, "Linked internship not found"));
            offer.setInternship(internship);
            // Receiving an offer moves the linked application to Selected.
            internship.setStatus(ApplicationStatus.SELECTED);
            internshipRepository.save(internship);
        }

        offerRepository.save(offer);
        return OfferResponse.from(offer);
    }

    public OfferResponse update(Long userId, Long offerId, OfferRequest req) {
        Offer offer = ownedOffer(userId, offerId);
        offer.setCompanyName(req.getCompanyName().trim());
        offer.setRoleTitle(req.getRoleTitle().trim());
        offer.setStipend(req.getStipend());
        if (req.getOfferDate() != null) {
            offer.setOfferDate(req.getOfferDate());
        }
        offer.setJoiningDate(req.getJoiningDate());
        if (req.getResponseDeadline() != null) {
            offer.setResponseDeadline(req.getResponseDeadline());
        }
        offer.setLetterLink(req.getLetterLink());
        offer.setNotes(req.getNotes());
        if (req.getInternshipId() != null) {
            Internship internship = internshipRepository
                    .findByInternshipIdAndUser_UserId(req.getInternshipId(), userId)
                    .orElseThrow(() -> new ApiException(404, "Linked internship not found"));
            offer.setInternship(internship);
        }
        offerRepository.save(offer);
        return OfferResponse.from(offer);
    }

    public OfferResponse accept(Long userId, Long offerId) {
        Offer offer = ownedOffer(userId, offerId);
        requirePending(offer);
        offer.setStatus(OfferStatus.ACCEPTED);
        offerRepository.save(offer);
        return OfferResponse.from(offer);
    }

    public OfferResponse decline(Long userId, Long offerId) {
        Offer offer = ownedOffer(userId, offerId);
        requirePending(offer);
        offer.setStatus(OfferStatus.DECLINED);
        offerRepository.save(offer);
        return OfferResponse.from(offer);
    }

    public void delete(Long userId, Long offerId) {
        Offer offer = ownedOffer(userId, offerId);
        offerRepository.delete(offer);
    }

    public List<OfferResponse> adminAll() {
        expirePending();
        return offerRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(OfferResponse::from).toList();
    }

    /** Offers whose response window has closed are treated as EXPIRED. */
    public void expirePending() {
        for (Offer offer : offerRepository.findByStatusOrderByResponseDeadlineAsc(OfferStatus.PENDING)) {
            if (offer.isExpired()) {
                offer.setStatus(OfferStatus.EXPIRED);
                offerRepository.save(offer);
            }
        }
    }

    private Offer ownedOffer(Long userId, Long offerId) {
        return offerRepository.findByOfferIdAndUser_UserId(offerId, userId)
                .orElseThrow(() -> new ApiException(404, "Offer not found"));
    }

    private void requirePending(Offer offer) {
        if (offer.isExpired()) {
            offer.setStatus(OfferStatus.EXPIRED);
            offerRepository.save(offer);
            throw new ApiException(400, "This offer expired on " + offer.getResponseDeadline());
        }
        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new ApiException(400, "This offer is already " + offer.getStatus().getLabel().toLowerCase());
        }
    }
}
