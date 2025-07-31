package com.minhductran.tutorial.minhductran.controller;

import com.minhductran.tutorial.minhductran.dto.request.Permission.StoreRequest;
import com.minhductran.tutorial.minhductran.dto.request.Permission.UpdateRequest;
import com.minhductran.tutorial.minhductran.dto.response.ApiResponse;
import com.minhductran.tutorial.minhductran.dto.response.PermissionResponse;
import com.minhductran.tutorial.minhductran.dto.response.UserCatalogueResponse;
import com.minhductran.tutorial.minhductran.model.Permission;
import com.minhductran.tutorial.minhductran.model.UserCatalogue;
import com.minhductran.tutorial.minhductran.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/permissions")
@Slf4j(topic = "Permission Controller")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("create")
    @PreAuthorize("hasAuthority('permission:store')")
    public ResponseEntity<?> create(@Valid @RequestBody StoreRequest request) {
        log.info("Creating user catalogue with request: {}", request);
        try {
            Permission permission = permissionService.create(request);
            log.info("Permission created successfully with ID: {}", permission.getId());
            PermissionResponse permissionResponse = PermissionResponse.builder()
                    .id(permission.getId())
                    .name(permission.getName())
                    .publish(permission.getPublish())
                    .build();

            ApiResponse<PermissionResponse> permissionResponseApi = ApiResponse.<PermissionResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Permission created successfully")
                    .data(permissionResponse)
                    .build();

            return ResponseEntity.ok(permissionResponseApi);

        } catch (Exception e) {
            return  ResponseEntity.badRequest().body("Error creating user catalogue: " + e.getMessage());
        }
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasAuthority('permission:update')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody UpdateRequest request) {
        log.info("Updating permission catalogue with request: {}", request);
        try {
            Permission permission = permissionService.update(id, request);
            log.info("Permission updated successfully with ID: {}", permission.getId());
            PermissionResponse permissionResponse = PermissionResponse.builder()
                    .id(permission.getId())
                    .name(permission.getName())
                    .publish(permission.getPublish())
                    .build();

            ApiResponse<?> permissionResponseApiResponse = ApiResponse.<PermissionResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Permission updated successfully")
                    .data(permissionResponse)
                    .build();

            return ResponseEntity.ok(permissionResponseApiResponse);

        } catch (Exception e) {
            return  ResponseEntity.badRequest().body("Error updating user catalogue: " + e.getMessage());
        }
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('permission:list')")
    public ResponseEntity<?> paginate(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        Page<Permission> permissions = permissionService.paginate(params);

        Page<PermissionResponse> permissionResponses = permissions.map(userCatalogue ->
                PermissionResponse.builder()
                        .id(userCatalogue.getId())
                        .name(userCatalogue.getName())
                        .publish(userCatalogue.getPublish())
                        .build()
        );

        ApiResponse<Page<PermissionResponse>> response = ApiResponse.<Page<PermissionResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("User catalogues retrieved successfully")
                .data(permissionResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('permission:delete')")
    public ResponseEntity<?> delete(@PathVariable int id) {
        log.info("Deleting user catalogue with ID: {}", id);
        try {
            permissionService.delete(id);
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .status(HttpStatus.OK.value())
                    .message("User catalogue deleted successfully")
                    .data("User catalogue with ID " + id + " has been deleted.")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting user catalogue: " + e.getMessage());
        }
    }

    @GetMapping("get/{id}")
    @PreAuthorize("hasAuthority('permission:show')")
    public ResponseEntity<?> getPermissionById(@PathVariable int id) {
        log.info("Getting permission with ID: {}", id);
        try {
            Permission permission = permissionService.getPermissionById(id);
            PermissionResponse permissionResponse = PermissionResponse.builder()
                    .id(permission.getId())
                    .name(permission.getName())
                    .publish(permission.getPublish())
                    .build();

            ApiResponse<PermissionResponse> response = ApiResponse.<PermissionResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Permission retrieved successfully")
                    .data(permissionResponse)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting permission: " + e.getMessage());
        }
    }
}
