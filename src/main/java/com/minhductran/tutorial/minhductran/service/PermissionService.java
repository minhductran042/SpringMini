package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.dto.request.Permission.StoreRequest;
import com.minhductran.tutorial.minhductran.dto.request.Permission.UpdateRequest;
import com.minhductran.tutorial.minhductran.dto.response.PermissionResponse;
import com.minhductran.tutorial.minhductran.model.Permission;
import com.minhductran.tutorial.minhductran.model.UserCatalogue;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface PermissionService {
    public Permission create(StoreRequest request);
    public Permission update(int id, UpdateRequest request);
    public Page<Permission> paginate(Map<String, String[]> parameters);
    public void delete(int id);
    public Permission getPermissionById(int id);
}
