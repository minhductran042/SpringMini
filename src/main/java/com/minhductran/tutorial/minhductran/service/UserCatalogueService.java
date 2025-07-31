package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.dto.request.UserCatalogue.StoreRequest;
import com.minhductran.tutorial.minhductran.dto.request.UserCatalogue.UpdateRequest;
import com.minhductran.tutorial.minhductran.model.UserCatalogue;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UserCatalogueService {
    public UserCatalogue create(StoreRequest request);
    public UserCatalogue update(int id, UpdateRequest request);
    public UserCatalogue getById(int id);
    public Page<UserCatalogue> paginate(Map<String, String[]> parameters);
    public void delete(int id);
}
