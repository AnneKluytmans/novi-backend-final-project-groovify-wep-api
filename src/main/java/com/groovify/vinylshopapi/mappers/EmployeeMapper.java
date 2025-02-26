package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.EmployeeRegisterDTO;
import com.groovify.vinylshopapi.dtos.EmployeeResponseDTO;
import com.groovify.vinylshopapi.dtos.UserResponseDTO;
import com.groovify.vinylshopapi.dtos.UserSummaryResponseDTO;
import com.groovify.vinylshopapi.models.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeRegisterDTO employeeRegisterDTO);

    EmployeeResponseDTO toResponseDTO(Employee employee);

    List<EmployeeResponseDTO> toResponseDTOs(List<Employee> employees);

    UserResponseDTO toUserResponseDTO(Employee employee);

    List<UserResponseDTO> toUserResponseDTOs(List<Employee> employees);

    UserSummaryResponseDTO toUserSummaryResponseDTO(Employee employee);

    List<UserSummaryResponseDTO> toUserSummaryResponseDTOs(List<Employee> employees);
}
