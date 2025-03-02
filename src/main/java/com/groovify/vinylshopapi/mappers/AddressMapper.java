package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.AddressRequestDTO;
import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
import com.groovify.vinylshopapi.dtos.CustomerAddressResponseDTO;
import com.groovify.vinylshopapi.models.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressRequestDTO addressRequestDTO);

    AddressResponseDTO toResponseDTO(Address address);

    List<AddressResponseDTO> toResponseDTOs(List<Address> addresses);

    CustomerAddressResponseDTO toCustomerAddressResponseDTO(Address address);

    List<CustomerAddressResponseDTO> toCustomerAddressResponseDTOs(List<Address> addresses);
}
