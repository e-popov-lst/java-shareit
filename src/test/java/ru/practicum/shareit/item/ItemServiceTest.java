package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    @Autowired
    private final ItemService itemService;

    @Test
    @Order(1)
    void findAll() {
        itemService.getUserStorage().create(new User(101L, "user1", "user1@mytest.com"));
        itemService.create(101L, new Item(1001L, "item1", "item1 desc", true, null, null));
        itemService.create(101L, new Item(1002L, "item2", "item2 desc", true, null, null));

        assertTrue(itemService.findAll(101L).size() >= 2);
    }

    @Test
    void create() {
        itemService.getUserStorage().create(new User(102L, "user2", "user2@mytest.com"));
        ItemDto item3 = itemService.create(102L, new Item(1003L, "item3", "item3 desc", true, null, null));

        assertEquals("item3", item3.getName());
    }

    @Test
    void update() {
        User user4 = itemService.getUserStorage().create(new User(104L, "user4", "user4@mytest.com"));
        ItemDto item4 = itemService.create(104L, new Item(1004L, "item4", "item4 desc", true, null, null));
        item4 = itemService.update(new Item(1004L, "item42", "item4 desc", true, user4, null));

        assertEquals("item42", item4.getName());
    }

    @Test
    void updatePart() {
        User user5 = itemService.getUserStorage().create(new User(105L, "user5", "user5@mytest.com"));
        ItemDto item5 = itemService.create(105L, new Item(1005L, "item5", "item5 desc", true, null, null));

        item5 = itemService.updatePart(105L, 1005L, new Item(null, "item52", null, true, user5, null));

        assertEquals("item52", item5.getName());
        assertEquals("item5 desc", item5.getDescription());
    }
}
