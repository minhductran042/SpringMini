package com.minhductran.tutorial.minhductran.controller;


import com.minhductran.tutorial.minhductran.dto.request.ToDoCreationDTO;
import com.minhductran.tutorial.minhductran.dto.request.ToDoUpdateDTO;
import com.minhductran.tutorial.minhductran.dto.response.ResponeErrorDTO;
import com.minhductran.tutorial.minhductran.dto.response.ResponseData;
import com.minhductran.tutorial.minhductran.dto.response.ToDoDetailResponse;
import com.minhductran.tutorial.minhductran.model.ToDo;
import com.minhductran.tutorial.minhductran.service.ToDoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/todos")
@Validated
@Slf4j
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @PostMapping("create")
    ResponseData<ToDoDetailResponse> createTo(@RequestBody @Valid ToDoCreationDTO request) {
        try {
            ToDoDetailResponse toDo = toDoService.createToDo(request);

            return new ResponseData<>(HttpStatus.CREATED.value(), "ToDo created successfully", toDo);
        } catch (Exception e) {
            return new ResponeErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("get/{todoId}")
    ResponseData<ToDoDetailResponse> getToDo(@PathVariable int todoId) {
        try {
            ToDoDetailResponse toDo = toDoService.getToDo(todoId);
            return new ResponseData<>(HttpStatus.OK.value(), "Get ToDo successfully", toDo);
        } catch (Exception e) {
            return new ResponeErrorDTO(HttpStatus.BAD_REQUEST.value(), "ToDo not found");
        }
    }

    @GetMapping("getAll")
    ResponseData<List<ToDoDetailResponse>> getAllToDos(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                       @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                       @RequestParam(defaultValue = "id", required = false) String sortBy,
                                                       @RequestParam(defaultValue = "asc", required = false) String sortOrder) {
        try {
            List<ToDoDetailResponse> toDos = toDoService.getAllToDos(pageNo, pageSize, sortBy, sortOrder);
            return new ResponseData<>(HttpStatus.OK.value(), "Get all ToDos successfully", toDos);
        } catch (Exception e) {
            return new ResponeErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("update/{todoId}")
    ResponseData updateToDo(@PathVariable int todoId, @RequestBody @Valid ToDoUpdateDTO request) {
        try {
            ToDoDetailResponse toDo = toDoService.updateToDo(todoId, request);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Updated ToDo successfully", toDo);
        } catch (Exception e) {
            return new ResponeErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("delete/{todoId}")
    ResponseData<?> deleteToDo(@PathVariable int todoId) {
        try {
            toDoService.deleteToDo(todoId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "ToDo deleted successfully");
        } catch (Exception e) {
            return new ResponeErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
