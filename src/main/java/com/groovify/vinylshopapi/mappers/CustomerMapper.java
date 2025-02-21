package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.CustomerRegisterDTO;
import com.groovify.vinylshopapi.dtos.CustomerResponseDTO;
import com.groovify.vinylshopapi.dtos.UserResponseDTO;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.VinylRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    static Set<Long> mapFavoriteVinylRecords(Set<VinylRecord> vinylRecords) {
        Set<Long> vinylRecordIds = new HashSet<>();
        for (VinylRecord vinylRecord : vinylRecords) {
            vinylRecordIds.add(vinylRecord.getId());
        }
        return vinylRecordIds;
    }
}
