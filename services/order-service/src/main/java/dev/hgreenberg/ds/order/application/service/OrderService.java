package dev.hgreenberg.ds.order.application.service;

import dev.hgreenberg.ds.order.application.command.AddItemCommand;
import dev.hgreenberg.ds.order.application.command.CreateOrderCommand;
import dev.hgreenberg.ds.order.domain.model.Order;
import java.util.UUID;

public interface OrderService {

  UUID createOrder(CreateOrderCommand command);

  void submitOrder(UUID orderId);

  void addItem(UUID orderId, AddItemCommand command);

  void cancelOrder(UUID orderId);

  Order getOrder(UUID orderId);
}
