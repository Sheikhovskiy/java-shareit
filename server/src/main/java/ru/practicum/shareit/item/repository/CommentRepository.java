package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph("comment.item")
    Comment save(Comment comment);


}

