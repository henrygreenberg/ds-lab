package dev.hgreenberg.ds.order.application.service;

import dev.hgreenberg.ds.order.application.client.ProductServiceClient;
import dev.hgreenberg.ds.order.application.command.AddItemCommand;
import dev.hgreenberg.ds.order.application.command.CreateOrderCommand;
import dev.hgreenberg.ds.order.domain.model.Order;
import dev.hgreenberg.ds.order.domain.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productClient;

    public OrderServiceImpl(OrderRepository orderRepository, ProductServiceClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    @Override
    public UUID createOrder(CreateOrderCommand command) {
        var order = Order.create(command.customerId());

        for (var item : command.items()) {
            var product = productClient.getProduct(item.productId());

            order.addItem(product.id(), product.price(), item.quantity());
        }
        orderRepository.save(order);

        return order.getId();
    }

    @Override
    public void addItem(UUID orderId, AddItemCommand command) {
        Order order = orderRepository.findByIdWithItems(orderId).orElseThrow();
        var product = productClient.getProduct(command.productId());

        order.addItem(product.id(), product.price(), command.quantity());
    }

    @Override
    public void submitOrder(UUID orderId) {
        Order order = orderRepository.findByIdWithItems(orderId).orElseThrow();
        order.submit();
    }

    @Override
    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.cancel();
    }

    @Override
    public Order getOrder(UUID orderId) {
        return orderRepository.findByIdWithItems(orderId).orElseThrow();
    }
}
