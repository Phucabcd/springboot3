package ntp.springboot3.mapper;

import ntp.springboot3.dto.request.PermissionRequest;
import ntp.springboot3.dto.request.response.PermissionResponse;
import ntp.springboot3.entity.Permission;
import org.mapstruct.Mapper;




@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

}
