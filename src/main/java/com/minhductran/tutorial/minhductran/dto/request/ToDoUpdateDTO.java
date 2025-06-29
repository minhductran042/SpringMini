package com.minhductran.tutorial.minhductran.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.minhductran.tutorial.minhductran.utils.ToDoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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

    @NotNull(message = "startDate cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date startDate;

    private ToDoStatus toDoStatus;

    private Integer userId; // Giả sử bạn muốn liên kết với người dùng đã tạo ToDo này
}
