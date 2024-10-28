package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Override
    @EntityGraph("booking.item")
    Booking save(Booking booking);

    @EntityGraph("booking.item")
    Optional<Booking> findById(Long bookingId);


    @Query(value = "select case when count(bk) > 0 then true else false end " +
            "from Booking as bk " +
            "where bk.booker.id = ?1 " +
            "and (bk.end < ?2) "
    )
    boolean existsByIdAndDone(long userId, LocalDateTime actualDate);

    @EntityGraph("booking.item")
    List<Optional<Booking>> findByBookerIdOrderByStartDesc(long bookerId);

    @Query(value = "select bk " +
            "from Booking as bk " +
            "where bk.booker = ?1 " +
            "and ((bk.start < ?2) " + "and (bk.end > ?2)) " +
            "ORDER BY bk.start DESC "
    )
    List<Optional<Booking>> findByBooker_IdAndCurrent(long bookerId, LocalDateTime actualDate);

    @Query(value = "select bk " +
            "from Booking as bk " +
            "where bk.booker.id = ?1 " +
            "and (bk.end < ?2) " +
            "ORDER BY bk.start DESC "
    )
    List<Optional<Booking>> findByBooker_IdAndDone(long bookerId, LocalDateTime actualDate);

    @Query(value = "select bk " +
            "from Booking as bk " +
            "where bk.booker = ?1 " +
            "and (bk.start > ?2) " +
            "ORDER BY bk.start DESC "
    )
    List<Optional<Booking>> findByBooker_IdAndFuture(long bookerId, LocalDateTime actualDate);

    @EntityGraph("booking.item")
    List<Optional<Booking>> findByBookerIdAndStatus(long bookerId, String status);

    @Query(value = "select bk " +
            "from Booking as bk " +
            "where bk.item.owner = ?1 " +
            "ORDER BY bk.start DESC "
    )
    List<Optional<Booking>> findByItemOwner(long ownerId);

    @Query(value = "select bk " +
            "from Booking as bk " +
            "where bk.item.owner = ?1 " +
            "and ((bk.start < ?2) " + "and (bk.end > ?2)) " +
            "ORDER BY bk.start DESC "
    )
    List<Optional<Booking>> findByOwner_IdAndCurrent(long ownerId, LocalDateTime actualDate);

    @Query(value = "select bk " +
            "from Booking as bk " +
            "where bk.item.owner = ?1 " +
            "and (bk.end < ?2) " +
            "ORDER BY bk.start DESC "
    )
    List<Optional<Booking>> findByOwner_IdAndDone(long ownerId, LocalDateTime actualDate);

    @Query(value = "select bk " +
            "from Booking as bk " +
            "where bk.item.owner = ?1 " +
            "and (bk.start > ?2) " +
            "ORDER BY bk.start DESC "
    )
    List<Optional<Booking>> findByOwner_IdAndFuture(long ownerId, LocalDateTime actualDate);

    @Query(value = "select bk " +
            "from Booking as bk " +
            "where bk.item.owner = ?1" +
            "and bk.status = ?2" +
            "ORDER BY bk.start DESC "
    )
    List<Optional<Booking>> findByOwnerIdAndStatusOrderByStartDesc(long ownerId, String status);


}
