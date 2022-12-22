package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.CheckParamException;
import ru.practicum.shareit.common.NotFoundException;
import ru.practicum.shareit.common.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ItemService {
    public final ItemStorage itemStorage;
    public final UserStorage userStorage;

    @Autowired
    public ItemService(@Qualifier("itemStorageInMemory") ItemStorage itemStorage, @Qualifier("userStorageInMemory") UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }


    public ItemDto create(Long userId, Item item) {
        validate(item);

        if (item.getId() != null && itemStorage.isExistsItemById(item.getId())) {
            throw new ValidationException("Вещь с id=" + item.getId() + " уже зарегистрирована.");
        }

        setOwnerByUserId(userId, item);

        return ItemMapper.toItemDto(itemStorage.create(item));
    }

    public ItemDto update(Item item) {
        validate(item);
        return ItemMapper.toItemDto(itemStorage.update(item));
    }

    public ItemDto updatePart(Long userId, Long itemId, Item item) {
        Item itemById = itemStorage.findItemById(itemId);

        if (itemById.getOwner() != null && !itemById.getOwner().getId().equals(userId)) {
            throw new NotFoundException("У пользователя id=" + userId + " не найдена вещь с id=" + itemId + " для изменения.", Item.class.getName());
        }

        itemById.setId(item.getId() != null ? item.getId() : itemById.getId());
        itemById.setName(item.getName() != null ? item.getName() : itemById.getName());
        itemById.setDescription(item.getDescription() != null ? item.getDescription() : itemById.getDescription());
        itemById.setAvailable(item.isAvailable() != null ? item.isAvailable() : itemById.isAvailable());

        return update(itemById);
    }

    public List<ItemDto> findAll(Long userId) {
        List<ItemDto> itemsList = new ArrayList<>();

        Iterator<Item> iterator = itemStorage.findAll(userId).iterator();
        while (iterator.hasNext()) {
            itemsList.add(ItemMapper.toItemDto(iterator.next()));
        }

        return itemsList;
    }

    public ItemDto findItemById(long itemId) {
        return ItemMapper.toItemDto(itemStorage.findItemById(itemId));
    }

    public List<ItemDto> searchItems(Long userId, String text) {
        List<ItemDto> itemsList = new ArrayList<>();

        Iterator<Item> iterator = itemStorage.searchItems(userId, text).iterator();
        while (iterator.hasNext()) {
            itemsList.add(ItemMapper.toItemDto(iterator.next()));
        }

        return itemsList;
    }

    private void setOwnerByUserId(Long userId, Item item) {
        if (userId != null) {
            User user = userStorage.findUserById(userId);
            if (user == null) {
                throw new NotFoundException("Пользователь-владелец с id=" + userId + " не найден.", User.class.getName());
            }

            item.setOwner(user);
        }
    }

    private void validate(Item item) {
        if (item.isAvailable() == null) {
            throw new CheckParamException("Статус доступности для аренды не может быть пустым.");
        }

        if (item.getName() == null || item.getName().isBlank()) {
            throw new CheckParamException("Наименование вещи не может быть пустым.");
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new CheckParamException("Описание вещи не может быть пустым.");
        }
    }

}
