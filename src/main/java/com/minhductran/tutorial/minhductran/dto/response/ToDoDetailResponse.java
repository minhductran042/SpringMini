package com.minhductran.tutorial.minhductran.dto.response;


import com.minhductran.tutorial.minhductran.model.ToDo;
import com.minhductran.tutorial.minhductran.utils.ToDoStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ToDoDetailResponse {
    private String title;
    private String description;
    private ToDoStatus status;
    private Date startDate;
}
