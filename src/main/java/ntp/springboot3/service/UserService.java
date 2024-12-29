package ntp.springboot3.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ntp.springboot3.dto.request.UserCreationRequest;
import ntp.springboot3.dto.request.UserUpdateRequest;
import ntp.springboot3.dto.request.response.UserResponse;
import ntp.springboot3.entity.User;
import ntp.springboot3.enums.Role;
import ntp.springboot3.exception.AppException;
import ntp.springboot3.exception.ErrorCode;
import ntp.springboot3.mapper.UserMapper;
import ntp.springboot3.repo.UserRepo;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//khong can su dung autowired
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepo userRepo;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

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
        // PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); // comment vi da su dung @Bean ben Securiry
          user.setPassword(passwordEncoder.encode(user.getPassword()));

          //tao role
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

//        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        return userRepo.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUser(){
        log.info("In method get Users");
        return userRepo.findAll();
    }

    //user chi lay duoc thong tin cua chinh minh
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id){
        log.info("In method UserById xx");
        return userMapper.toUserResponse(userRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GETUSERERROR)));
    }

    //lay thong tin cua chinh minh qua token bang SecuriryContextHolder
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepo.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        return userMapper.toUserResponse(user);
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
