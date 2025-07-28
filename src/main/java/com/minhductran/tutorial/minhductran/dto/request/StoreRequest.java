package com.minhductran.tutorial.minhductran.dto.request;

import jakarta.validation.constraints.NotBlank;

public class StoreRequest {
    @NotBlank(message = "Nhóm thành viên khong được để trống")
    private String email;

    @NotBlank(message = "Trạng thái không được để trống")

    private Integer publish;
}
