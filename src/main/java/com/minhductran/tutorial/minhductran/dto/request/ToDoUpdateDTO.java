package com.minhductran.tutorial.minhductran.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ToDoUpdateDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;
    private boolean completed;
}
