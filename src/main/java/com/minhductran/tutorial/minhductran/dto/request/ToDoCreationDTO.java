package com.minhductran.tutorial.minhductran.dto.request;

import com.minhductran.tutorial.minhductran.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ToDoCreationDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    private Date startDate;

    private boolean completed;

    private Integer userId;// Giả sử bạn muốn liên kết với người dùng đã tạo ToDo này
}
