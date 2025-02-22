package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.CustomerRegisterDTO;
import com.groovify.vinylshopapi.dtos.CustomerResponseDTO;
import com.groovify.vinylshopapi.dtos.UserResponseDTO;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.VinylRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerRegisterDTO customerRegisterDTO);

    @Mapping(target = "favoriteVinylRecords", source = "favoriteVinylRecords", qualifiedByName = "mapFavoriteVinylRecords")
    CustomerResponseDTO toResponseDTO(Customer customer);

    List<CustomerResponseDTO> toResponseDTOs(List<Customer> customers);

    UserResponseDTO toUserResponseDTO(Customer customer);

    List<UserResponseDTO> toUserResponseDTOs(List<Customer> customers);

    @Named("mapFavoriteVinylRecords")
    static List<Long> mapFavoriteVinylRecords(List<VinylRecord> vinylRecords) {
        List<Long> vinylRecordIds = new ArrayList<>();
        for (VinylRecord vinylRecord : vinylRecords) {
            vinylRecordIds.add(vinylRecord.getId());
        }
        return vinylRecordIds;
    }
}
