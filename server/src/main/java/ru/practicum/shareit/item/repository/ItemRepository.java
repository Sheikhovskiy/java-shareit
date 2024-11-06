package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Item;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Override
    @EntityGraph("item.owner")
    Item save(Item item);

    @EntityGraph("item.owner")
    Optional<Item> findById(long itemId);

    List<Item> findAllByOwnerId(long userId);

    Item deleteById(long itemId);

    @Query(value = "select it " +
            "from Item as it " +
            "where (it.available = true) " +
            "and ( (LOWER(it.name) like LOWER(CONCAT('%', ?1, '%'))) " +
            "or (LOWER(it.description) like LOWER(CONCAT('%', ?1, '%'))) ) "
    )
    List<Item> getItemsBySearchRequest(String text);

    @Query(value = "select it " +
            "from Item as it " +
            "where it.request.id = ?1")
    List<Item> findByRequestId(long requestId);

    @Query(value = "select rq, it " +
            "from Item as it " +
            "left join Request as rq on it.request.id = rq.id " +
            "where rq.owner.id = ?1")
    List<Object[]> findByRequesterId(long userId);

}
