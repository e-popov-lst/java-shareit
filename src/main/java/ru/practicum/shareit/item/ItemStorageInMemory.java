package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Component("itemStorageInMemory")
public class ItemStorageInMemory implements ItemStorage {
    private static long lastItemId = 0;
    private final Set<Item> items = new HashSet<>();

    @Override
    public Item create(Item item) {
        if (item.getId() == null) {
            item.setId(++lastItemId);
        } else if (item.getId() > lastItemId) {
            lastItemId = item.getId();
        }

        items.add(item);
        return item;
    }

    @Override
    public Item update(Item item) {
        if (!items.contains(item)) {
            throw new NotFoundException("Вещь с id=" + item.getId() + " не найдена для изменения.", Item.class.getName());
        } else {
            items.remove(item);

            items.add(item);
            return item;
        }
    }

    @Override
    public List<Item> findAll(Long userId) {
        return items.stream()
                .filter(data -> (data.getOwner().getId().equals(userId)))
                .collect(Collectors.toList());
    }

    @Override
    public Item findItemById(long itemId) {
        Item item;

        try {
            item = items.stream()
                    .filter(data -> Objects.equals(data.getId(), itemId))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            return null;
        }

        return item;
    }

    @Override
    public List<Item> searchItems(Long userId, String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return items.stream()
                    .filter(data -> data.isAvailable())
                    .filter(data -> (data.getDescription().toLowerCase().indexOf(text.toLowerCase()) > -1))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean isExistsItemById(long itemId) {
        Item item = findItemById(itemId);

        return (item != null);
    }

}
