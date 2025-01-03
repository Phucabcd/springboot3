package ntp.springboot3.dto.request.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ntp.springboot3.entity.Role;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     String id;
     String username;
     String firstName;
     String lastName;
     LocalDate dob;

     Set<RoleResponse> roles;
}
