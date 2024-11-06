package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph("comment.item")
    Comment save(Comment comment);

    @Query(value = "select cm " +
            "from Comment as cm " +
            "where cm.item.id = ?1 " +
            "ORDER BY cm.createdAt DESC")
    List<Comment> findByItemId(long itemId);

}

