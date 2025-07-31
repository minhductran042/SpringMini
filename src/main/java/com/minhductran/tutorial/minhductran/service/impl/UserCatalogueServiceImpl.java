package com.minhductran.tutorial.minhductran.service.impl;

import com.minhductran.tutorial.minhductran.dto.request.UserCatalogue.StoreRequest;
import com.minhductran.tutorial.minhductran.dto.request.UserCatalogue.UpdateRequest;
import com.minhductran.tutorial.minhductran.exception.ResourceNotFoundException;
import com.minhductran.tutorial.minhductran.model.Permission;
import com.minhductran.tutorial.minhductran.model.User;
import com.minhductran.tutorial.minhductran.model.UserCatalogue;
import com.minhductran.tutorial.minhductran.repository.PermissionRepository;
import com.minhductran.tutorial.minhductran.repository.UserCatalogueRepository;
import com.minhductran.tutorial.minhductran.repository.UserRepository;
import com.minhductran.tutorial.minhductran.service.UserCatalogueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserCatalogueServiceImpl implements UserCatalogueService {

    private final UserCatalogueRepository userCatalogueRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserCatalogue create(StoreRequest request) {

        try {
            UserCatalogue userCatalogue = UserCatalogue.builder()
                    .name(request.getName())
                    .publish(request.getPublish())
                    .build();

            if(request.getPermissions() != null && !request.getPermissions().isEmpty()) {
                Set<Permission> permissions = new HashSet<>();
                List<Permission> foundPermissions = permissionRepository.findAllById(request.getPermissions());
                permissions.addAll(foundPermissions);
                userCatalogue.setPermissions(permissions);
            }

            if(request.getUsers() != null) {
                Set<User> users = new HashSet<>();
                if (!request.getUsers().isEmpty()) {
                    List<User> foundUsers = userRepository.findAllById(request.getUsers());
                    users.addAll(foundUsers);
                }
                userCatalogue.setUsers(users);
            }

            return userCatalogueRepository.save(userCatalogue);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user catalogue: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserCatalogue update(int id,UpdateRequest request) {
        try {
            UserCatalogue userCatalogue = getUserCatalogueById(id);
            userCatalogue.setName(request.getName());
            userCatalogue.setPublish(request.getPublish());

            // Cập nhật permissions nếu có trong request
            if (request.getPermissions() != null) {
                Set<Permission> permissions = new HashSet<>();

                if (!request.getPermissions().isEmpty()) {
                    List<Permission> foundPermissions = permissionRepository.findAllById(request.getPermissions());
                    permissions.addAll(foundPermissions);
                }

                userCatalogue.setPermissions(permissions);
            }

            if(request.getUsers() != null) {
                Set<User> users = new HashSet<>();
                if (!request.getUsers().isEmpty()) {
                    List<User> foundUsers = userRepository.findAllById(request.getUsers());
                    users.addAll(foundUsers);
                }
                userCatalogue.setUsers(users);
            }

            return userCatalogueRepository.save(userCatalogue);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user catalogue: " + e.getMessage());
        }
    }

    @Override
    public UserCatalogue getById(int id) {
        return userCatalogueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Catalogue not found with id: " + id));
    }

    @Override
    public Page<UserCatalogue> paginate(Map<String, String[]> parameters) {
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

        return userCatalogueRepository.findAll(pageable);
    }

    public UserCatalogue getUserCatalogueById(int id) {
        return userCatalogueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Catalogue not found with id: " + id));
    }

    @Override
    public void delete(int id) {
        UserCatalogue userCatalogue = getUserCatalogueById(id);
        try {
            userCatalogueRepository.delete(userCatalogue);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user catalogue: " + e.getMessage());
        }
    }

    public String[] getRelation() {
        return new String[] {
                "permission"
        };
    }
}
