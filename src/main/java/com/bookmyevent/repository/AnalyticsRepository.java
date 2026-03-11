package com.bookmyevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

// Using Event entity as the shell simply to attach custom queries
public interface AnalyticsRepository extends JpaRepository<com.bookmyevent.entity.Event, Long> {

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.event.id = :eventId AND b.status = 'CONFIRMED'")
    BigDecimal calculateTotalRevenueForEvent(@Param("eventId") Long eventId);

    // Returns Object[] where [0] is tier name and [1] is count
    @Query("SELECT t.ticketTier.name, COUNT(t.id) FROM Ticket t WHERE t.booking.event.id = :eventId AND t.status IN ('VALID', 'CHECKED_IN') GROUP BY t.ticketTier.name")
    List<Object[]> countTicketsSoldPerTier(@Param("eventId") Long eventId);

    @Query("SELECT COUNT(c.id) FROM CheckIn c JOIN c.ticket t JOIN t.booking b WHERE b.event.id = :eventId")
    Long countCheckInsForEvent(@Param("eventId") Long eventId);
}
