package ntp.springboot3.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ntp.springboot3.dto.request.ApiResponse;
import ntp.springboot3.dto.request.PermissionRequest;
import ntp.springboot3.dto.request.response.PermissionResponse;
import ntp.springboot3.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(permissionRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> findAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.findAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission){
        permissionService.delete(permission);
        return ApiResponse.<Void>builder()
                .build();
    }

}
