package dev.guedes.jdbcdemo.service;

import dev.guedes.jdbcdemo.repository.AccountRepository;
import dev.guedes.jdbcdemo.repository.ItemRepository;
import dev.guedes.jdbcdemo.repository.OrderRepository;
import dev.guedes.jdbcdemo.repository.ProductRepository;
import dev.guedes.jdbcdemo.repository.impl.AccountRepositoryImpl;
import dev.guedes.jdbcdemo.repository.impl.ItemRepositoryImpl;
import dev.guedes.jdbcdemo.repository.impl.OrderRepositoryImpl;
import dev.guedes.jdbcdemo.repository.impl.ProductRepositoryImpl;

/**
 * @author João Guedes
 */
public class ServiceFactory {

    public static AccountService createAccountService() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        return new AccountService(accountRepository);
    }

    public static ProductService createProductService() {
        ProductRepository productRepository = new ProductRepositoryImpl();
        return new ProductService(productRepository);
    }

    public static OrderService createOrderService() {
        OrderRepository orderRepository = new OrderRepositoryImpl();
        return new OrderService(orderRepository);
    }

    public static ItemService createItemService() {
        ItemRepository itemRepository = new ItemRepositoryImpl();
        return new ItemService(itemRepository);
    }

}
