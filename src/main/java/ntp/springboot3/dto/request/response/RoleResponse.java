package ntp.springboot3.dto.request.response;

import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ntp.springboot3.entity.Permission;
import ntp.springboot3.entity.Role;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String name;
    String description;

    @ManyToMany
    Set<PermissionResponse> permissions;
}
