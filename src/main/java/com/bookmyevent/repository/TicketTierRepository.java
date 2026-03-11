package com.bookmyevent.repository;

import com.bookmyevent.entity.TicketTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketTierRepository extends JpaRepository<TicketTier, Long> {

    List<TicketTier> findByEventId(Long eventId);

    @Modifying
    @Query("UPDATE TicketTier t SET t.availableQty = t.availableQty - :quantity WHERE t.id = :tierId AND t.availableQty >= :quantity")
    int decrementInventory(@Param("tierId") Long tierId, @Param("quantity") int quantity);
}
