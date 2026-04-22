package dev.hgreenberg.ds.order.domain.repository;

import dev.hgreenberg.ds.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByIdAndCustomerId(UUID id, UUID customerId);

    List<Order> findAllByCustomerId(UUID customerId);

    @Query("""
                select distinct o from Order o
                left join fetch o.items
                where o.customerId = :customerId
            """)
    List<Order> findAllByCustomerIdWithItems(UUID customerId);

    @Query("""
                select o from Order o
                left join fetch o.items
                where o.id = :id
            """)
    Optional<Order> findByIdWithItems(@Param("id") UUID id);
}
