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

    private static final String NotExistingUser = "Ошибка создания запросов: Пользователь с идентификатором {} " +
            "не существует!";

    private static final String NotExistingRequest = "Ошибка создания запросов: Запрос на добавление предмета с идентификатором %d " +
            "не существует!";


    public Request createRequest(Request request, long userId) {

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format(NotExistingUser, userId));
        }

        request.setOwner(userOpt.get());
        request.setCreated(LocalDateTime.now());

        return requestRepository.save(request);
    }

    public List<Request> getAllRequestsByUserId(long userId) {

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new NotFoundException(String.format(NotExistingUser, userId));
        }

        List<Optional<Request>> requestListOpt = requestRepository.findAllByOwner_Id(userId);

        List<Request> requestList = requestListOpt.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        Map<Request, List<Item>> requestListMap = new HashMap<>();
//        List<Item> requestListMap = new HashMap<>();

        for (Request rq : requestList) {
            List<Item> itemList = new ArrayList<>();

            if (rq.getId() != 0) {
               requestListMap.put(rq, itemRepository.findByRequestId(rq.getId()));
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
            throw new NotFoundException(String.format(NotExistingRequest, requestId));
        }

        Request request = requestOpt.get();

//        List<Item> requestListMap = new ArrayList<>();

        List<Item> itemList = itemRepository.findByRequestId(requestId);

        request.setItemList(itemList);

        return requestOpt.get();
    }





}
