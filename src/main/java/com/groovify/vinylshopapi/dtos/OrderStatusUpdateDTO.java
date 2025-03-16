package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    private OrderStatus orderStatus;
}
