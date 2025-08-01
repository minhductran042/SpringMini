package com.minhductran.tutorial.minhductran.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailResponse {
    private int id;
    private String text;
    private String subject;
    private String to;
}
