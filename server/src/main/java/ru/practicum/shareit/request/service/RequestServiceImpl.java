package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImplementation implements RequestService {

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private static final String NOT_EXISTING_USER = "Ошибка создания запросов: Пользователь с идентификатором {} " +
            "не существует!";

    private static final String NOT_EXISTING_REQUEST = "Ошибка создания запросов: Запрос на добавление предмета с идентификатором {} " +
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


        List<Optional<Request>> requestList = requestRepository.findAllByOwner_Id(userId);

        return requestList.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request getRequestById(long requestId) {

        Optional<Request> requestOpt = requestRepository.findById(requestId);
        System.out.println("=================================================> " + requestId);

        if (requestOpt.isEmpty()) {
            throw new NotFoundException(String.format("Ошибка создания запросов: Запрос на добавление предмета с идентификатором %d " +
                    "не существует!", requestId));
        }

        return requestOpt.get();
    }





}
