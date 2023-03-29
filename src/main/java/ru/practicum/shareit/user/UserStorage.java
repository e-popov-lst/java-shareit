package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void delete(User user);

    List<User> findAll();

    User findUserById(long id);

    boolean isExistsUserById(long userId);

    User findUserByEmail(String email);

}
