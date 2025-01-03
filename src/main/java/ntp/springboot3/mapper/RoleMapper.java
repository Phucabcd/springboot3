package ntp.springboot3.mapper;

import ntp.springboot3.dto.request.PermissionRequest;
import ntp.springboot3.dto.request.RoleRequest;
import ntp.springboot3.dto.request.response.PermissionResponse;
import ntp.springboot3.dto.request.response.RoleResponse;
import ntp.springboot3.entity.Permission;
import ntp.springboot3.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "premissions",ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse ToRoleResponse(Role role);

}
