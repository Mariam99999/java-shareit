package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartBeforeOrStartAndEndAfter(Long bookerId, LocalDateTime i, LocalDateTime s, LocalDateTime n, Sort sort);

    List<Booking> findByBookerIdAndEndBefore(long bookerId, LocalDateTime o, Sort sort);

    List<Booking> findByBookerIdAndStartAfter(long bookerId, LocalDateTime o, Sort sort);

    List<Booking> findByBookerAndStatus(long id, Status status, Sort sort);

    @Query("select b from Booking as b " +
            "join b.item as i join b.booker join i.owner where i.owner.id = :id")
    List<Booking> findBookingByItemOwner(long id);
}