package ru.practicum.shareit.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */

@Data
@EqualsAndHashCode(of = "id")
public class User {

    private long id;

    private String name;

    private String email;
}
