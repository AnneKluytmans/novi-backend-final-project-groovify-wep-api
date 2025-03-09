package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.models.Customer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VinylRecordMapper.class})
public interface CustomerMapper {
    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerRegisterDTO customerRegisterDTO);

    @Mapping(target = "favoriteVinylRecords", source = "favoriteVinylRecords")
    CustomerResponseDTO toResponseDTO(Customer customer);

    List<CustomerResponseDTO> toResponseDTOs(List<Customer> customers);

    UserResponseDTO toUserResponseDTO(Customer customer);

    UserSummaryResponseDTO toUserSummaryResponseDTO(Customer customer);

    List<UserSummaryResponseDTO> toUserSummaryResponseDTOs(List<Customer> customers);

    void updateCustomer(CustomerUpdateDTO customerUpdateDTO, @MappingTarget Customer customer);
}
