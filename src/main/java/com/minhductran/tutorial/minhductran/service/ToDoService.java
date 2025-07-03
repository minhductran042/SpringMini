package com.minhductran.tutorial.minhductran.service;

import com.minhductran.tutorial.minhductran.dto.request.ToDoDTO;
import com.minhductran.tutorial.minhductran.dto.response.ToDoDetailResponse;
import com.minhductran.tutorial.minhductran.utils.ToDoStatus;

import java.util.Date;
import java.util.List;


public interface ToDoService {
      public ToDoDetailResponse createToDo(ToDoDTO request);
      public ToDoDetailResponse getToDo(int todoId);
      public List<ToDoDetailResponse> getAllToDos(int pageNo, int pageSize, String sortBy, String sortOrder);
      public ToDoDetailResponse updateToDo(int todoId, ToDoDTO request);
      public void deleteToDo(int todoId);
      public void changeToDoStatus(int todoId, ToDoStatus status);
      public List<ToDoDetailResponse> getToDoFromFilter(Date date, ToDoStatus status);

}
