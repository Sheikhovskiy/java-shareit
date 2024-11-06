package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final ItemRepository itemRepository;

    private static final String NOT_EXISTING_USER = "Ошибка создания запросов: Пользователь с идентификатором {} " +
            "не существует!";

    private static final String NOT_EXISTING_REQUEST = "Ошибка создания запросов: Запрос на добавление предмета с идентификатором %d " +
            "не существует!";


    public Request createRequest(Request request, long userId) {

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_USER, userId));
        }

        request.setOwner(userOpt.get());
        request.setCreated(LocalDateTime.now());

        return requestRepository.save(request);
    }

    public List<Request> getAllRequestsByUserId(long userId) {

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_USER, userId));
        }

        List<Optional<Request>> requestListOpt = requestRepository.findAllByOwner_Id(userId);

        List<Request> requestList = requestListOpt.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        List<Object[]> results = itemRepository.findByRequesterId(userId);
        Map<Request, List<Item>> requestListMap = new HashMap<>();

        for (Object[] result : results) {
            Request request = (Request) result[0];
            Item item = (Item) result[1];

            requestListMap.computeIfAbsent(request, it -> new ArrayList<>()).add(item);
        }

        for (Request rq : requestList) {

            if (rq.getId() != 0) {
                requestListMap.put(rq, requestListMap.get(rq));
            }
            if (requestListMap.get(rq) != null) {
                rq.setItemList(requestListMap.get(rq));
            } else {
                rq.setItemList(new ArrayList<>());
            }
        }
        return requestList;
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request getRequestById(long requestId) {

        Optional<Request> requestOpt = requestRepository.findById(requestId);

        if (requestOpt.isEmpty()) {
            throw new NotFoundException(String.format(NOT_EXISTING_REQUEST, requestId));
        }

        Request request = requestOpt.get();

        List<Item> items = itemRepository.findByRequestId(requestId);
        request.setItemList(items);

        return request;
    }





}
