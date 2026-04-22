package dev.hgreenberg.ds.order.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Order order;

  @Column(nullable = false)
  private UUID productId;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private int quantity;

  protected OrderItem() {}

  OrderItem(Order order, UUID productId, BigDecimal price, int quantity) {
    this.order = Objects.requireNonNull(order);
    this.productId = Objects.requireNonNull(productId);
    this.price = Objects.requireNonNull(price);
    this.quantity = quantity;

    if (price.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("price must be positive");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("quantity must be positive");
    }
  }

  public BigDecimal getSubtotal() {
    return price.multiply(BigDecimal.valueOf(quantity));
  }

  public UUID getProductId() {
    return productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }
}
