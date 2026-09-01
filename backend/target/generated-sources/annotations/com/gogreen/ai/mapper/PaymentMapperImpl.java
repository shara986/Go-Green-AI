package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.PaymentRequestDto;
import com.gogreen.ai.dto.response.PaymentResponseDto;
import com.gogreen.ai.entity.Order;
import com.gogreen.ai.entity.Payment;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-01T22:27:56+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentResponseDto toResponseDto(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();

        paymentResponseDto.setOrderId( paymentOrderId( payment ) );
        paymentResponseDto.setOrderNumber( paymentOrderOrderNumber( payment ) );
        paymentResponseDto.setId( payment.getId() );
        paymentResponseDto.setAmount( payment.getAmount() );
        paymentResponseDto.setStatus( payment.getStatus() );
        paymentResponseDto.setPaymentMethod( payment.getPaymentMethod() );
        paymentResponseDto.setTransactionId( payment.getTransactionId() );
        paymentResponseDto.setCreatedAt( payment.getCreatedAt() );
        paymentResponseDto.setUpdatedAt( payment.getUpdatedAt() );

        return paymentResponseDto;
    }

    @Override
    public List<PaymentResponseDto> toResponseDtoList(List<Payment> payments) {
        if ( payments == null ) {
            return null;
        }

        List<PaymentResponseDto> list = new ArrayList<PaymentResponseDto>( payments.size() );
        for ( Payment payment : payments ) {
            list.add( toResponseDto( payment ) );
        }

        return list;
    }

    @Override
    public Payment toEntity(PaymentRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Payment payment = new Payment();

        payment.setAmount( dto.getAmount() );
        payment.setStatus( dto.getStatus() );
        payment.setPaymentMethod( dto.getPaymentMethod() );
        payment.setTransactionId( dto.getTransactionId() );

        return payment;
    }

    private UUID paymentOrderId(Payment payment) {
        if ( payment == null ) {
            return null;
        }
        Order order = payment.getOrder();
        if ( order == null ) {
            return null;
        }
        UUID id = order.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String paymentOrderOrderNumber(Payment payment) {
        if ( payment == null ) {
            return null;
        }
        Order order = payment.getOrder();
        if ( order == null ) {
            return null;
        }
        String orderNumber = order.getOrderNumber();
        if ( orderNumber == null ) {
            return null;
        }
        return orderNumber;
    }
}
