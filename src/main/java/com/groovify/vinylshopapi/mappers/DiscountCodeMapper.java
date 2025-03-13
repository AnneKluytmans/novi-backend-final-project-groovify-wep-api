package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.DiscountCodeRequestDTO;
import com.groovify.vinylshopapi.dtos.DiscountCodeResponseDTO;
import com.groovify.vinylshopapi.dtos.DiscountCodeSummaryResponseDTO;
import com.groovify.vinylshopapi.models.DiscountCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountCodeMapper {
    @Mapping(target = "id", ignore = true)
    DiscountCode toEntity(DiscountCodeRequestDTO discountCodeRequestDTO);

    DiscountCodeResponseDTO toResponseDTO(DiscountCode discountCode);

    List<DiscountCodeResponseDTO> toResponseDTOs(List<DiscountCode> discountCodes);

    DiscountCodeSummaryResponseDTO toSummaryResponseDTO(DiscountCode discountCode);

    List<DiscountCodeSummaryResponseDTO> toSummaryResponseDTOs(List<DiscountCode> discountCodes);

    void updateDiscountCode(DiscountCodeRequestDTO discountCodeRequestDTO, @MappingTarget DiscountCode discountCode);
}
