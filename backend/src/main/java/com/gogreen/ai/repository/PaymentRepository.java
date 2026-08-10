package com.gogreen.ai.repository;

import com.gogreen.ai.entity.Payment;
import com.gogreen.ai.entity.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByOrderId(UUID orderId);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByStatus(PaymentStatus status);

    @Query("select p from Payment p where " +
            "( :status is null or p.status = :status ) " +
            "and ( :search is null or lower(p.transactionId) like lower(concat('%', :search, '%')) " +
            "   or lower(p.paymentMethod) like lower(concat('%', :search, '%')) )")
    Page<Payment> searchPayments(@Param("search") String search,
                                 @Param("status") PaymentStatus status,
                                 Pageable pageable);
}
