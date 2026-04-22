package dev.hgreenberg.ds.order.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(UUID productId, BigDecimal price, int quantity) {
}
