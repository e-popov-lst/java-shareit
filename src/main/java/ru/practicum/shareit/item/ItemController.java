package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @GetMapping()
    public List<ItemDto> findAll(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemService.findAll(userId);
    }

    @PostMapping()
    public ItemDto create(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @Valid @RequestBody Item item) {
        return itemService.create(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updatePart(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @Valid @RequestBody Item item) {
        return itemService.updatePart(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getUser(@PathVariable long itemId) {
        return itemService.findItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @RequestParam String text) {
        return itemService.searchItems(userId, text);
    }

}
