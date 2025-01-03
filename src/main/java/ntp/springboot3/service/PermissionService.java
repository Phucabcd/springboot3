package ntp.springboot3.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ntp.springboot3.dto.request.PermissionRequest;
import ntp.springboot3.dto.request.response.PermissionResponse;
import ntp.springboot3.entity.Permission;
import ntp.springboot3.mapper.PermissionMapper;
import ntp.springboot3.repo.PermissionRepo;
import org.springframework.stereotype.Service;


import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepo permissionRepo;
    PermissionMapper permissionMapper;


    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepo.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepo.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission){
        permissionRepo.deleteById(permission);
    }


}
