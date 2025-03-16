package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.OrderItemResponseDTO;
import com.groovify.vinylshopapi.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VinylRecordMapper.class})
public interface OrderItemMapper {
    @Mapping(target = "vinylRecord", source = "vinylRecord")
    OrderItemResponseDTO toResponseDTO(OrderItem orderItem);

    List<OrderItemResponseDTO> toResponseDTOs(List<OrderItem> orderItems);
}
