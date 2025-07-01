package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.dto.request.ToDoCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.ToDoUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.ToDoDetailResponse;
import com.minhductran.tutorial.minhductran.model.ToDo;
import com.minhductran.tutorial.minhductran.utils.ToDoStatus;

import java.util.List;


public interface ToDoService {
      public ToDoDetailResponse createToDo(ToDoCreationDTO request);
      public ToDoDetailResponse getToDo(int todoId);
      public List<ToDoDetailResponse> getAllToDos(int pageNo, int pageSize, String sortBy, String sortOrder);
      public ToDoDetailResponse updateToDo(int todoId, ToDoUpdateDTO request);
      public void deleteToDo(int todoId);
      public void changeToDoStatus(int todoId, ToDoStatus status);

}
