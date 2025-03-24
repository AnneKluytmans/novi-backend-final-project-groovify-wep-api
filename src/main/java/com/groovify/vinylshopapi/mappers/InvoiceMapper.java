package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.InvoiceResponseDTO;
import com.groovify.vinylshopapi.models.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, OrderItemMapper.class, CustomerMapper.class})
public interface InvoiceMapper {

    @Mapping(target = "customer", source = "order.customer")
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "orderDate", source = "order.orderDate")
    @Mapping(target = "orderItems", source = "order.orderItems")

    @Mapping(target = "paymentStatus", source = "order.paymentStatus")
    @Mapping(target = "subTotalPrice", source = "order.subTotalPrice")
    @Mapping(target = "shippingCost", source = "order.shippingCost")
    @Mapping(target = "totalPrice", expression = "java(invoice.getOrder().getSubTotalPrice().add(invoice.getOrder().getShippingCost()))")

    @Mapping(target = "shippingAddress", source = "order.shippingAddress")
    @Mapping(target = "billingAddress", source = "order.billingAddress")
    InvoiceResponseDTO toResponseDTO(Invoice invoice);
}
