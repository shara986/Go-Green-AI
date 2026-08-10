package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.PaymentRequestDto;
import com.gogreen.ai.dto.response.PaymentResponseDto;
import com.gogreen.ai.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface PaymentMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.orderNumber", target = "orderNumber")
    PaymentResponseDto toResponseDto(Payment payment);

    List<PaymentResponseDto> toResponseDtoList(List<Payment> payments);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Payment toEntity(PaymentRequestDto dto);
}
