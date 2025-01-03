package ntp.springboot3.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ntp.springboot3.dto.request.PermissionRequest;
import ntp.springboot3.dto.request.RoleRequest;
import ntp.springboot3.dto.request.response.PermissionResponse;
import ntp.springboot3.dto.request.response.RoleResponse;
import ntp.springboot3.entity.Permission;
import ntp.springboot3.mapper.PermissionMapper;
import ntp.springboot3.mapper.RoleMapper;
import ntp.springboot3.repo.PermissionRepo;
import ntp.springboot3.repo.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
   RoleRepo roleRepo;
   PermissionRepo permissionRepo;
   RoleMapper roleMapper;

   public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepo.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepo.save(role);

        return roleMapper.ToRoleResponse(role);
   }

   public List<RoleResponse> getAll(){
       return roleRepo.findAll()
               .stream()
               .map(roleMapper::ToRoleResponse)
               .toList();
   }

   public void delete(String role){
       roleRepo.deleteById(role);
   }
}
