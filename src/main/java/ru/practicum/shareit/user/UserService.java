package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.CheckParamException;
import ru.practicum.shareit.common.NotFoundException;
import ru.practicum.shareit.common.ValidationException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserService {

    public final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userStorageInMemory") UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public UserDto create(User user) {
        if (user.getId() != null && userStorage.isExistsUserById(user.getId())) {
            throw new ValidationException("Пользователь с id=" + user.getId() + " уже зарегистрирован.");
        }

        validate(user);
        return UserMapper.toUserDto(userStorage.create(user));
    }

    public UserDto update(User user) {
        if (!userStorage.isExistsUserById(user.getId())) {
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден для изменения.");
        }

        validate(user);
        return UserMapper.toUserDto(userStorage.update(user));
    }

    public UserDto updatePart(Long id, User user) {
        User userById = userStorage.findUserById(id);

        if (userById == null) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден для изменения.");
        }

        validate(new User(id, user.getName() != null ? user.getName() : userById.getName(), user.getEmail() != null ? user.getEmail() : userById.getEmail()));

        userById.setName(user.getName() != null ? user.getName() : userById.getName());
        userById.setEmail(user.getEmail() != null ? user.getEmail() : userById.getEmail());

        return UserMapper.toUserDto(userStorage.update(userById));
    }

    public List<UserDto> findAll() {
        List<UserDto> usersList = new ArrayList<>();

        Iterator<User> iterator = userStorage.findAll().iterator();
        while (iterator.hasNext()) {
            usersList.add(UserMapper.toUserDto(iterator.next()));
        }

        return usersList;
    }

    public UserDto findUserById(long id) {
        return UserMapper.toUserDto(userStorage.findUserById(id));
    }

    public void delete(long id) {
        userStorage.delete(userStorage.findUserById(id));
    }

    private void validate(User user) {
        String email = user.getEmail();

        if (email == null) {
            throw new CheckParamException("Email пользователя не может быть пустым.");
        }

        User userByEmail = userStorage.findUserByEmail(email);

        if (userByEmail != null && (user.getId() == null || userByEmail.getId() != user.getId())) {
            throw new ValidationException("Пользователь с email=" + user.getEmail() + " уже зарегистрирован.");
        }

    }

}
