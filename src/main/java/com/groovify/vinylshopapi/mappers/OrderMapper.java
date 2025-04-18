package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.OrderPatchDTO;
import com.groovify.vinylshopapi.dtos.OrderRequestDTO;
import com.groovify.vinylshopapi.dtos.OrderResponseDTO;
import com.groovify.vinylshopapi.dtos.OrderSummaryResponseDTO;
import com.groovify.vinylshopapi.models.Order;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, CustomerMapper.class, OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    Order toEntity(OrderRequestDTO orderRequestDTO);

    @Mapping(target = "shippingAddress", source = "shippingAddress")
    @Mapping(target = "billingAddress", source = "billingAddress")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponseDTO toResponseDTO(Order order);

    List<OrderResponseDTO> toResponseDTOs(List<Order> orders);

    @Mapping(target = "customer", source = "customer")
    OrderSummaryResponseDTO toOrderSummaryResponseDTO(Order order);

    List<OrderSummaryResponseDTO> toOrderSummaryResponseDTOs(List<Order> orders);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateOrder(OrderPatchDTO orderPatchDTO, @MappingTarget Order order);
}
