package dev.hgreenberg.ds.order.api.dto;

import java.util.UUID;

public record OrderItemRequest(UUID productId, int quantity) {
}
