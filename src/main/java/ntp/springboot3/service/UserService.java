package ntp.springboot3.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ntp.springboot3.dto.request.UserCreationRequest;
import ntp.springboot3.dto.request.UserUpdateRequest;
import ntp.springboot3.dto.request.response.UserResponse;
import ntp.springboot3.entity.User;
import ntp.springboot3.exception.AppException;
import ntp.springboot3.exception.ErrorCode;
import ntp.springboot3.mapper.UserMapper;
import ntp.springboot3.repo.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//khong can su dung autowired
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepo userRepo;
    UserMapper userMapper;

    public User createUser(UserCreationRequest request){
//        User user = new User();
        if(userRepo.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
//          throw new RuntimeException("Username already exists");

        //@Builder
//        UserCreationRequest request1 = UserCreationRequest.builder()
//                .username("")
//                .password("")
//                .build();

          User user = userMapper.toUser(request);

        // so cuoi cung cang cao se cang bao mat nhung dong nghia voi viec chay cang cham
          PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
          user.setPassword(passwordEncoder.encode(user.getPassword()));

//        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        return userRepo.save(user);
    }

    public List<User> getUser(){
        return userRepo.findAll();
    }

    public UserResponse getUserById(String id){
        return userMapper.toUserResponse(userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        return userMapper.toUserResponse(userRepo.save(user));
    }

    public void deleteUser(String userId) {
        userRepo.deleteById(userId);
    }


}
