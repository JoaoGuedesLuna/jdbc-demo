package dev.guedes.jdbcdemo.service;

import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Item;
import dev.guedes.jdbcdemo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

/**
 * @author João Guedes
 */
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void save(Item item) throws DatabaseException {
        this.itemRepository.save(item);
    }

    public Optional<Item> findById(Long id) throws DatabaseException {
        return this.itemRepository.findById(id);
    }

    public List<Item> findAllByOrderId(Long orderId) throws DatabaseException {
        return this.itemRepository.findAllByOrderId(orderId);
    }

    public void delete(Item item) throws DatabaseException {
        this.itemRepository.delete(item);
    }

}
