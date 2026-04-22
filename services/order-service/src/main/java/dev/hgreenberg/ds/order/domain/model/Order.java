package dev.hgreenberg.ds.order.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "orders")
public class Order {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItem> items = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, updatable = false)
    private UUID customerId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @Column(nullable = false)
    private BigDecimal totalAmount;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    // --- JPA constructor ---
    protected Order() {
    }

    // --- Factory method ---
    public static Order create(UUID customerId) {
        Objects.requireNonNull(customerId, "customerId must not be null");

        Order order = new Order();
        order.customerId = customerId;
        order.status = OrderStatus.CREATED;
        order.totalAmount = BigDecimal.ZERO;
        order.createdAt = Instant.now();

        return order;
    }

    // --- Domain behavior ---

    public void addItem(UUID productId, BigDecimal price, int quantity) {
        ensureModifiable();

        validateItem(productId, price, quantity);

        OrderItem item = new OrderItem(this, productId, price, quantity);
        items.add(item);

        recalculateTotal();
    }

    public void removeItem(UUID productId) {
        ensureModifiable();

        items.removeIf(i -> i.getProductId().equals(productId));

        recalculateTotal();
    }

    public void submit() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Order cannot be submitted");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }
        this.status = OrderStatus.SUBMITTED;
    }

    public void markPaid() {
        if (status != OrderStatus.SUBMITTED) {
            throw new IllegalStateException("Order must be submitted before payment");
        }
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        if (status == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Cannot cancel shipped order");
        }
        this.status = OrderStatus.CANCELLED;
    }

    // --- Internal logic ---

    private void recalculateTotal() {
        this.totalAmount = items.stream().map(OrderItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void ensureModifiable() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Order cannot be modified in state: " + status);
        }
    }

    private void validateItem(UUID productId, BigDecimal price, int quantity) {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(price, "price must not be null");

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("price must be positive");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }

    // --- Getters ---

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    // --- equals / hashCode ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
