package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.NotFoundException;

import java.util.*;

@Component("userStorageInMemory")
public class UserStorageInMemory implements UserStorage {
    private static long lastUserId = 0;
    private final Set<User> users = new HashSet<>();

    @Override
    public User create(User user) {
        if (user.getId() == null) {
            user.setId(++lastUserId);
        } else if (user.getId() > lastUserId) {
            lastUserId = user.getId();
        }

        users.add(user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.contains(user)) {
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден для изменения.", User.class.getName());
        } else {
            users.remove(user);

            users.add(user);
            return user;
        }
    }

    @Override
    public void delete(User user) {
        if (!users.contains(user)) {
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден для удаления.", User.class.getName());
        } else {
            users.remove(user);
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User findUserById(long id) {
        User user;

        try {
            user = users.stream().filter(data -> Objects.equals(data.getId(), id)).findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }

        return user;
    }

    @Override
    public boolean isExistsUserById(long id) {
        User user = findUserById(id);

        return (user != null);
    }

    @Override
    public User findUserByEmail(String email) {
        User user;

        try {
            user = users.stream().filter(data -> Objects.equals(data.getEmail(), email)).findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }

        return user;
    }

}
