package com.minhductran.tutorial.minhductran.dto.response;


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
    private Boolean completed;
    private Date startDate;
}
