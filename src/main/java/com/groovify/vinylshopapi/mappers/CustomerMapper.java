package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.CustomerRegisterDTO;
import com.groovify.vinylshopapi.dtos.CustomerResponseDTO;
import com.groovify.vinylshopapi.models.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CustomerMapper {
    Customer toEntity(CustomerRegisterDTO customerRegisterDTO);

    CustomerResponseDTO toResponseDTO(Customer customer);

    List<CustomerResponseDTO> toResponseDTOs(List<Customer> customers);
}
