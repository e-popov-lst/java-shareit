package ru.practicum.shareit.user;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return (user == null) ? null : new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

}
