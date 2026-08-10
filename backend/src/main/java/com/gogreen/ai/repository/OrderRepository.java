package com.gogreen.ai.repository;

import com.gogreen.ai.entity.Order;
import com.gogreen.ai.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserId(UUID userId);

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByOrderNumber(String orderNumber);

    @Query("select coalesce(sum(o.totalAmount), 0) from Order o")
    Double sumTotalAmount();

    @Query("select o from Order o where " +
            "( :search is null or lower(o.orderNumber) like lower(concat('%', :search, '%')) ) " +
            "and ( :status is null or o.status = :status )")
    Page<Order> searchOrders(@Param("search") String search,
                             @Param("status") OrderStatus status,
                             Pageable pageable);

    @Query("select function('date_format', o.createdAt, '%Y-%m') as month, coalesce(sum(o.totalAmount), 0) " +
            "from Order o where o.status <> 'CANCELLED' group by function('date_format', o.createdAt, '%Y-%m') order by month")
    List<Object[]> sumRevenueByMonth();

    @Query("select function('date_format', o.createdAt, '%Y-%m') as month, count(o) " +
            "from Order o group by function('date_format', o.createdAt, '%Y-%m') order by month")
    List<Object[]> countOrdersByMonth();

    @Query("select oi.plant.name as plantName, coalesce(sum(oi.quantity), 0) as soldCount " +
            "from OrderItem oi join oi.order o where o.status <> 'CANCELLED' " +
            "group by oi.plant.name order by soldCount desc")
    List<Object[]> findTopSellingPlants();
}
