package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.CustomerRegisterDTO;
import com.groovify.vinylshopapi.dtos.CustomerResponseDTO;
import com.groovify.vinylshopapi.dtos.UserResponseDTO;
import com.groovify.vinylshopapi.dtos.UserSummaryResponseDTO;
import com.groovify.vinylshopapi.models.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VinylRecordMapper.class})
public interface CustomerMapper {
    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerRegisterDTO customerRegisterDTO);

    @Mapping(target = "favoriteVinylRecords", source = "favoriteVinylRecords")
    CustomerResponseDTO toResponseDTO(Customer customer);

    List<CustomerResponseDTO> toResponseDTOs(List<Customer> customers);

    UserResponseDTO toUserResponseDTO(Customer customer);

    List<UserResponseDTO> toUserResponseDTOs(List<Customer> customers);

    UserSummaryResponseDTO toUserSummaryResponseDTO(Customer customer);

    List<UserSummaryResponseDTO> toUserSummaryResponseDTOs(List<Customer> customers);
}
