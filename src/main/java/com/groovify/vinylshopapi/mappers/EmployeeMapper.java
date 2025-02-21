package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.EmployeeRegisterDTO;
import com.groovify.vinylshopapi.dtos.EmployeeResponseDTO;
import com.groovify.vinylshopapi.models.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface EmployeeMapper {
    Employee toEntity(EmployeeRegisterDTO employeeRegisterDTO);

    EmployeeResponseDTO toResponseDTO(Employee employee);

    List<EmployeeResponseDTO> toResponseDTOs(List<Employee> employees);
}
