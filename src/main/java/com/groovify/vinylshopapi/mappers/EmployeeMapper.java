package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.models.Employee;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface EmployeeMapper {
    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeRegisterDTO employeeRegisterDTO);

    @Mapping(target = "address", source = "address")
    EmployeeResponseDTO toResponseDTO(Employee employee);

    List<EmployeeResponseDTO> toResponseDTOs(List<Employee> employees);

    UserResponseDTO toUserResponseDTO(Employee employee);

    UserSummaryResponseDTO toUserSummaryResponseDTO(Employee employee);

    List<UserSummaryResponseDTO> toUserSummaryResponseDTOs(List<Employee> employees);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateEmployee(UserPatchDTO userPatchDTO, @MappingTarget Employee employee);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateEmployeeByAdmin(EmployeeAdminPatchDTO employeeAdminPatchDTO, @MappingTarget Employee employee);
}
