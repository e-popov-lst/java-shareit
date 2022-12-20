package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@AllArgsConstructor
public class User {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @Email(message = "Не корректный email.")
    private String email;

}
