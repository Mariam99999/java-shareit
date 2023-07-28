package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime s, LocalDateTime n, Pageable pageable);

    List<Booking> findByBookerIdAndEndBefore(long bookerId, LocalDateTime o, Pageable pageable);

    List<Booking> findByBookerIdAndStartAfter(long bookerId, LocalDateTime o, Pageable pageable);

    List<Booking> findByBookerIdAndStatus(long id, Status status, Pageable pageable);

    List<Booking> findByBookerIdAndItemIdAndStatusAndEndBefore(long bookerId, long itemId, Status status,
                                                               LocalDateTime localDateTime);

    List<Booking> findByItemId(long itemId, Sort sort);

    List<Booking> findByItemOwnerId(long ownerId, Sort sort);

 //   @Query("select b from Booking as b where b.item.owner.id = :ownerId order by b.start desc ")
    List<Booking> findAllByItemOwnerId(long ownerId,Pageable pageable);

    //    @Query("select b from Booking as b where b.item.owner.id = :ownerId" +
//            " AND b.start <= :now  AND b.end > :now order by b.start desc ")
//    List<Booking> findCurrentBookingByItemOwner(long ownerId, LocalDateTime now,Pageable pageable);
    List<Booking> findAllByItemOwnerIdAndEndAfterAndStartBeforeOrStart(long ownerId, LocalDateTime now,
                                                                       LocalDateTime l, LocalDateTime ll, Pageable pageable);

    //    @Query("select b from Booking as b where b.item.owner.id = :ownerId" +
//            " AND b.end < :now  order by b.start desc ")
//    List<Booking> findPastBookingByItemOwner(long ownerId, LocalDateTime now,Pageable pageable);
    List<Booking> findAllByItemOwnerIdAndEndBefore(long ownerId, LocalDateTime now, Pageable pageable);


    //    @Query("select b from Booking as b where b.item.owner.id = :ownerId" +
//            " AND b.start > :now order by b.start desc ")
//    List<Booking> findFutureBookingByItemOwner(long ownerId, LocalDateTime now);
    List<Booking> findAllByItemOwnerIdAndStartAfter(long ownerId, LocalDateTime now, Pageable pageable);

    //    @Query("select b from Booking as b where b.item.owner.id = :ownerId" +
//            " AND b.status = :status order by b.start desc ")
    List<Booking> findAllByItemOwnerIdAndStatus(long ownerId, Status status, Pageable pageable);

}
