package com.interntrack.repository;

import com.interntrack.model.Offer;
import com.interntrack.model.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    Optional<Offer> findByOfferIdAndUser_UserId(Long offerId, Long userId);

    List<Offer> findAllByOrderByCreatedAtDesc();

    List<Offer> findByStatusOrderByResponseDeadlineAsc(OfferStatus status);

    long countByStatus(OfferStatus status);

    long countByUser_UserId(Long userId);
}
