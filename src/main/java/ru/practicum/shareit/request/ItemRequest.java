package ru.practicum.shareit.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@EqualsAndHashCode(of = "id")
public class ItemRequest {

    private long id;

    private String description;

    private long requester;

    private LocalDateTime created;

}
