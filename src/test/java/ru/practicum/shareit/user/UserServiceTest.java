package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    @Autowired
    private final UserService userService;

    @Test
    @Order(1)
    void findAll() {
        userService.create(new User(1001L, "user1", "user1@test.com"));
        userService.create(new User(1002L, "user2", "user2@test.com"));

        assertTrue(userService.findAll().size() >= 2);
    }

    @Test
    void create() {
        UserDto user3 = userService.create(new User(1003L, "user3", "user3@test.com"));

        assertEquals("user3", user3.getName());
    }

    @Test
    void update() {
        UserDto user4 = userService.create(new User(1004L, "user4", "user4@test.com"));
        user4 = userService.update(new User(1004L, "user4", "user42@test.com"));

        assertEquals("user42@test.com", user4.getEmail());
    }

    @Test
    void updatePart() {
        UserDto user5 = userService.create(new User(1005L, "user5", "user5@test.com"));
        user5 = userService.updatePart(1005L, new User(null, "user52", null));

        assertEquals("user52", user5.getName());
    }

    @Test
    void delete() {
        UserDto user6 = userService.create(new User(1006L, "user6", "user6@test.com"));
        UserDto user7 = userService.create(new User(1007L, "user7", "user7@test.com"));
        assertEquals(1007L, user7.getId());

        userService.delete(1007);

        user7 = userService.findUserById(7);

        assertTrue(user7 == null);
        assertEquals(1006L, user6.getId());
    }
}
