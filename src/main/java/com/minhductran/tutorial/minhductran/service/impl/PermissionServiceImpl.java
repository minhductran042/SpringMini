package com.minhductran.tutorial.minhductran.service.impl;

import com.minhductran.tutorial.minhductran.dto.request.Permission.StoreRequest;
import com.minhductran.tutorial.minhductran.dto.request.Permission.UpdateRequest;
import com.minhductran.tutorial.minhductran.exception.ResourceNotFoundException;
import com.minhductran.tutorial.minhductran.model.Permission;
import com.minhductran.tutorial.minhductran.repository.PermissionRepository;
import com.minhductran.tutorial.minhductran.service.PermissionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Permission create(StoreRequest request) {

        try {
            Permission permission = Permission.builder()
                    .name(request.getName())
                    .publish(request.getPublish())
                    .build();
            return permissionRepository.save(permission);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user catalogue: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Permission update(int id, UpdateRequest request) {
        try {
            Permission permission = getPermissionById(id);
            permission.setName(request.getName());
            permission.setPublish(request.getPublish());
            permissionRepository.save(permission);
            return permission;
        } catch (Exception e) {
            throw new RuntimeException("Error creating user catalogue: " + e.getMessage());
        }
    }

    @Override
    public Page<Permission> paginate(Map<String, String[]> parameters) {
        int page = parameters.containsKey("page") ? Integer.parseInt(parameters.get("page")[0]) : 1;
        int size = parameters.containsKey("size") ? Integer.parseInt(parameters.get("size")[0]) : 20;
        // Tạo Sort từ parameters
        Sort sort = Sort.unsorted(); // Mặc định không sort

        if (parameters.containsKey("sort") && parameters.get("sort").length > 0) {
            String[] sortParams = parameters.get("sort");

            for (String sortParam : sortParams) {
                // Tách field và direction (vd: "name,asc" hoặc "id,desc")
                String[] parts = sortParam.split(",");
                String field = parts[0]; // Tên field

                // Direction mặc định là ASC nếu không có
                Sort.Direction direction = Sort.Direction.ASC;
                if (parts.length > 1) {
                    direction = parts[1].equalsIgnoreCase("desc")
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC;
                }

                // Thêm vào sort (có thể sort nhiều field)
                sort = sort.and(Sort.by(direction, field));
            }
        } else {
            // Sort mặc định theo id DESC nếu không có sort parameter
            sort = Sort.by(Sort.Direction.DESC, "id");
        }

        Pageable pageable = PageRequest.of(page - 1,size, sort);

        return permissionRepository.findAll(pageable);
    }

    public Permission getPermissionById(int id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + id));
    }

    @Override
    public void delete(int id) {
        Permission permission =  getPermissionById(id);
        try {
            permissionRepository.delete(permission);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user catalogue: " + e.getMessage());
        }
    }
}
