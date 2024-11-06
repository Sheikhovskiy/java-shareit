package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @EntityGraph("request.item")
    Request save(Request request);

    @EntityGraph("request.item")
    List<Optional<Request>> findAllByOwner_Id(long userId);

}
