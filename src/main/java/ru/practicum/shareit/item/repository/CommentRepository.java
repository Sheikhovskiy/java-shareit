package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    @EntityGraph("comment.item")
    Comment save(Comment comment);


}

