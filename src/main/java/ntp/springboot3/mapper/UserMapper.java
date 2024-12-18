package ntp.springboot3.mapper;

import ntp.springboot3.dto.request.UserCreationRequest;
import ntp.springboot3.dto.request.UserUpdateRequest;
import ntp.springboot3.dto.request.response.UserResponse;
import ntp.springboot3.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "firstName", target = "lastName")
    UserResponse toUserResponse(User user);

    User toUser(UserCreationRequest request);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
