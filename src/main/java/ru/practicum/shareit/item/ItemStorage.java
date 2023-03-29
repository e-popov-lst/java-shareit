package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {

    Item create(Item item);

    Item update(Item item);

    List<Item> findAll(Long userId);

    Item findItemById(long itemId);

    List<Item> searchItems(Long userId, String text);

    boolean isExistsItemById(long id);

}
