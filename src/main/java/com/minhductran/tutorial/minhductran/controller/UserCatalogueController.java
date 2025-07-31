package com.minhductran.tutorial.minhductran.controller;

import com.minhductran.tutorial.minhductran.dto.request.UserCatalogue.StoreRequest;
import com.minhductran.tutorial.minhductran.dto.request.UserCatalogue.UpdateRequest;
import com.minhductran.tutorial.minhductran.dto.response.ApiResponse;
import com.minhductran.tutorial.minhductran.dto.response.UserCatalogueResponse;
import com.minhductran.tutorial.minhductran.model.UserCatalogue;
import com.minhductran.tutorial.minhductran.service.UserCatalogueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user-catalogue")
@Slf4j(topic = "User Catalogue Controller")
@RequiredArgsConstructor
public class UserCatalogueController {

    private final UserCatalogueService userCatalogueService;

    @PostMapping("create")
    public ResponseEntity<?> create(@Valid @RequestBody StoreRequest request) {
        log.info("Creating user catalogue with request: {}", request);
        try {
            UserCatalogue userCatalogue = userCatalogueService.create(request);
            log.info("User catalogue created successfully with ID: {}", userCatalogue.getId());
            UserCatalogueResponse userCatalogueResponse = UserCatalogueResponse.builder()
                    .id(userCatalogue.getId())
                    .name(userCatalogue.getName())
                    .publish(userCatalogue.getPublish())
                    .build();

            ApiResponse<UserCatalogueResponse> userCatalogueResponseApi = ApiResponse.<UserCatalogueResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("User catalogue created successfully")
                    .data(userCatalogueResponse)
                    .build();

            return ResponseEntity.ok(userCatalogueResponseApi);

        } catch (Exception e) {
            return  ResponseEntity.badRequest().body("Error creating user catalogue: " + e.getMessage());
        }
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasAuthority('user_catalogue:update')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody UpdateRequest request) {
        log.info("Creating user catalogue with request: {}", request);
        try {
            UserCatalogue userCatalogueUpdate = userCatalogueService.update(id, request);
            log.info("User catalogue updated successfully with ID: {}", userCatalogueUpdate.getId());
            UserCatalogueResponse userCatalogueResponse = UserCatalogueResponse.builder()
                    .id(userCatalogueUpdate.getId())
                    .name(userCatalogueUpdate.getName())
                    .publish(userCatalogueUpdate.getPublish())
                    .build();

            ApiResponse<UserCatalogueResponse> userCatalogueResponseApi = ApiResponse.<UserCatalogueResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("User catalogue updated successfully")
                    .data(userCatalogueResponse)
                    .build();

            return ResponseEntity.ok(userCatalogueResponseApi);

        } catch (Exception e) {
            return  ResponseEntity.badRequest().body("Error updating user catalogue: " + e.getMessage());
        }
    }


    @GetMapping("list")
    @PreAuthorize("hasAuthority('user_catalogue:list')")
    public ResponseEntity<?> paginate(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        Page<UserCatalogue> userCatalogues = userCatalogueService.paginate(params);

        Page<UserCatalogueResponse> userCatalogueResponses = userCatalogues.map(userCatalogue ->
            UserCatalogueResponse.builder()
                .id(userCatalogue.getId())
                .name(userCatalogue.getName())
                .publish(userCatalogue.getPublish())
                .build()
        );

        ApiResponse<Page<UserCatalogueResponse>> response = ApiResponse.<Page<UserCatalogueResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("User catalogues retrieved successfully")
                .data(userCatalogueResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("get/{id}")
    @PreAuthorize("hasAuthority('user_catalogue:show')")
    public ResponseEntity<?> getById(@PathVariable int id) {
        log.info("Retrieving user catalogue with ID: {}", id);
        try {
            UserCatalogue userCatalogue = userCatalogueService.getById(id);
            UserCatalogueResponse userCatalogueResponse = UserCatalogueResponse.builder()
                    .id(userCatalogue.getId())
                    .name(userCatalogue.getName())
                    .publish(userCatalogue.getPublish())
                    .build();

            ApiResponse<UserCatalogueResponse> response = ApiResponse.<UserCatalogueResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("User catalogue retrieved successfully")
                    .data(userCatalogueResponse)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving user catalogue: " + e.getMessage());
        }
    }


    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('user_catalogue:delete')")
    public ResponseEntity<?> delete(@PathVariable int id) {
        log.info("Deleting user catalogue with ID: {}", id);
        try {
            userCatalogueService.delete(id);
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


}
