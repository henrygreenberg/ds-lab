package dev.hgreenberg.ds.order.infrastructure.client;

import dev.hgreenberg.ds.order.application.client.ProductServiceClient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class StubProductServiceClient implements ProductServiceClient {

  @Override
  public ProductData getProduct(UUID productId) {
    BigDecimal price = randomPrice(productId);

    return new ProductData(productId, price);
  }

  private BigDecimal randomPrice(UUID productId) {
    int hash = Math.abs(productId.hashCode());
    double value = 10 + (hash % 490); // 10–500

    return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
  }
}
