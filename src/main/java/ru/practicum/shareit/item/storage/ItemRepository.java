package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select new ru.practicum.shareit.item.dto.ItemDto(it.userId, count(it.id)) " +
            "from Booking as b " +
            "where b.item.id = :itemId " +
            "AND b.start > :dateTime " +
            "order by b.start asc " +
            "LIMIT 1")
    Optional<ItemDto> findByIdDto(long itemId, LocalDateTime dateTime);
    List<Item> findByOwnerId(long ownerId);
    Optional<Item> findByIdAndAvailable(long itemId,boolean available);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(String nameT, String descriptionT, Boolean b);
}
