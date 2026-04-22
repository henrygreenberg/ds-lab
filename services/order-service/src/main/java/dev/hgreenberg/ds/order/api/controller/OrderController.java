package dev.hgreenberg.ds.order.api.controller;

import dev.hgreenberg.ds.order.api.dto.CreateOrderRequest;
import dev.hgreenberg.ds.order.api.dto.OrderItemRequest;
import dev.hgreenberg.ds.order.api.dto.OrderItemResponse;
import dev.hgreenberg.ds.order.api.dto.OrderResponse;
import dev.hgreenberg.ds.order.application.command.AddItemCommand;
import dev.hgreenberg.ds.order.application.command.CreateOrderCommand;
import dev.hgreenberg.ds.order.application.command.OrderItemCommand;
import dev.hgreenberg.ds.order.application.service.OrderService;
import dev.hgreenberg.ds.order.domain.model.Order;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public UUID createOrder(@RequestBody CreateOrderRequest request) {
    var command =
        new CreateOrderCommand(
            request.customerId(),
            request.items().stream()
                .map(i -> new OrderItemCommand(i.productId(), i.quantity()))
                .toList());
    return orderService.createOrder(command);
  }

  @PostMapping("/{orderId}/items")
  public void addItem(@PathVariable UUID orderId, @RequestBody OrderItemRequest request) {
    var command = new AddItemCommand(request.productId(), request.quantity());

    orderService.addItem(orderId, command);
  }

  @PostMapping("/{orderId}/submit")
  public void submit(@PathVariable UUID orderId) {
    orderService.submitOrder(orderId);
  }

  @PostMapping("/{orderId}/cancel")
  public void cancel(@PathVariable UUID orderId) {
    orderService.cancelOrder(orderId);
  }

  @GetMapping("/{orderId}")
  public OrderResponse getOrder(@PathVariable UUID orderId) {
    Order order = orderService.getOrder(orderId);

    return new OrderResponse(
        order.getId(),
        order.getCustomerId(),
        order.getStatus().name(),
        order.getTotalAmount(),
        order.getCreatedAt(),
        order.getItems().stream()
            .map(i -> new OrderItemResponse(i.getProductId(), i.getPrice(), i.getQuantity()))
            .toList());
  }
}
