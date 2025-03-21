package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.AddressRequestDTO;
import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
import com.groovify.vinylshopapi.dtos.CustomerAddressResponseDTO;
import com.groovify.vinylshopapi.dtos.CustomerAddressRequestDTO;
import com.groovify.vinylshopapi.models.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressRequestDTO addressRequestDTO);

    Address toCustomerAddressEntity(CustomerAddressRequestDTO customerAddressRequestDTO);

    AddressResponseDTO toResponseDTO(Address address);

    List<AddressResponseDTO> toResponseDTOs(List<Address> addresses);

    CustomerAddressResponseDTO toCustomerResponseDTO(Address address);

    List<CustomerAddressResponseDTO> toCustomerResponseDTOs(List<Address> addresses);

    void updateAddress(AddressRequestDTO addressRequestDTO, @MappingTarget Address address);
}
