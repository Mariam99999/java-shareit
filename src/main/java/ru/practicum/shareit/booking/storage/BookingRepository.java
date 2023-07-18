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

    @Query("select b from Booking as b where b.item.owner.id = :ownerId order by b.start desc ")
    List<Booking> findAllBookingByItemOwner(long ownerId);

    @Query("select b from Booking as b where b.item.owner.id = :ownerId" +
            " AND b.start <= :now  AND b.end > :now order by b.start desc ")
    List<Booking> findCurrentBookingByItemOwner(long ownerId, LocalDateTime now);
    @Query("select b from Booking as b where b.item.owner.id = :ownerId" +
            " AND b.end < :now  order by b.start desc ")
    List<Booking> findPastBookingByItemOwner(long ownerId, LocalDateTime now);
    @Query("select b from Booking as b where b.item.owner.id = :ownerId" +
            " AND b.start > :now order by b.start desc ")
    List<Booking> findFutureBookingByItemOwner(long ownerId, LocalDateTime now);
    @Query("select b from Booking as b where b.item.owner.id = :ownerId" +
            " AND b.status = :status order by b.start desc ")
    List<Booking> findWaitingAndRejectedBookingByItemOwner(long ownerId, Status status);

}