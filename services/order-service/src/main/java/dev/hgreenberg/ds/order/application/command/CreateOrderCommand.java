package dev.hgreenberg.ds.order.application.command;

import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(UUID customerId, List<OrderItemCommand> items) {
}
