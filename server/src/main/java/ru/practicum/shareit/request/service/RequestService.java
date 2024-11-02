package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.Request;

import java.util.List;

public interface RequestService {

    Request createRequest(Request request, long userId);

    List<Request> getAllRequestsByUserId(long userId);

    List<Request> getAllRequests();

    Request getRequestById(long requestId);

}
