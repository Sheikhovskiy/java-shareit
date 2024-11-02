package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConditionsNotRespected;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final BookingRepository bookingRepository;

    private final RequestRepository requestRepository;


    private static final String NOT_EXISTING_ITEM = "Ошибка при работе с предметами: Предмет с идентификатором %d " +
            "не существует!";

    private static final String NOT_EXISTING_USER = "Ошибка при работе с предметами: Пользователь с идентификатором %d " +
            "не существует!";

    private static final String INEXISTENT_BOOKING_FOR_USER = "Ошибка при работе с предметами: У пользователя с идентификатором %d," +
            "не существует бронирований предметов";


    @Override
    public Item createItem(Item item) {

        Optional<User> itemOwnerIdOpt = Optional.ofNullable(item.getOwner());
        if (itemOwnerIdOpt.isEmpty()) {
            throw new NotFoundException("У каждого предмета должен быть владелец!");
        }

        item.setCommentList(Collections.emptyList());

        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item) {
        Optional<User> itemOwnerIdOpt = Optional.ofNullable(item.getOwner());
        if (itemOwnerIdOpt.isEmpty()) {
            throw new NotFoundException("У каждого предмета должен быть владелец!");
        }

        return itemRepository.save(item);
    }

    @Override
    public Item getItemInfoById(long itemId) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);

        if (itemOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_ITEM, itemId));
        }

        Item item = itemOpt.get();

//        if (item.getCommentList() == null) {
//            item.setCommentList(new ArrayList<>());
//        }

        return item;
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        return itemRepository.findAllByOwnerId(userId);
    }

    @Override
    public List<Item> getItemsBySearchRequest(String text, long userId) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.getItemsBySearchRequest(text);
    }


    @Override
    public Comment createComment(Comment comment, long itemId, long userId) {

        Optional<Item> itemOpt = itemRepository.findById(itemId);

        Optional<User> userOpt = userRepository.findById(userId);


        if (itemOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_ITEM, itemId));
        }

        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_USER, userId));
        }

        comment.setItem(itemOpt.get());
        comment.setAuthor(userOpt.get());

        if (!bookingRepository.existsByIdAndDone(userId, LocalDateTime.now())) {
            throw new ConditionsNotRespected(String.format(INEXISTENT_BOOKING_FOR_USER, userId));
        }

        return commentRepository.save(comment);
    }


}
