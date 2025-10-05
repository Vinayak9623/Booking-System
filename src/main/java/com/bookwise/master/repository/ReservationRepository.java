package com.bookwise.master.repository;

import com.bookwise.master.entity.Reservation;
import com.bookwise.master.entity.ReservationStatus;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query("""
    SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END 
    FROM Reservation r 
    WHERE r.resource.id = :resourceId 
      AND r.status = :status 
      AND (
           (r.startTime < :endTime AND r.endTime > :startTime)
      )
""")
    boolean existsByResourceIdAndStatusAndTimeOverlap(
            UUID resourceId,
            ReservationStatus status,
            Instant startTime,
            Instant endTime
    );

}
