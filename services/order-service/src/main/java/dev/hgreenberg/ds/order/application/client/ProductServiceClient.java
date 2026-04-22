package dev.hgreenberg.ds.order.application.client;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductServiceClient {

  ProductData getProduct(UUID productId);

  record ProductData(UUID id, BigDecimal price) {}
}
