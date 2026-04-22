package dev.hgreenberg.ds.order.infrastructure.client;

import dev.hgreenberg.ds.order.application.client.ProductServiceClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.UUID;

@Component
@Primary
public class StubProductServiceClient implements ProductServiceClient {

    private final Random random = new Random();

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
