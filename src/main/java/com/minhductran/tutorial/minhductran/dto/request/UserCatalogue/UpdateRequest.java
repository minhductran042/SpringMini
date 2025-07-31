package com.minhductran.tutorial.minhductran.dto.request.UserCatalogue;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    @NotBlank(message = "Nhóm thành viên khong được để trống")
    private String name;

    @NotNull(message = "Trạng thái không được để trống")
    @Min(value = 0)
    @Max(value = 20, message = "Trạng thái chỉ có thể giữa 0 và 20")
    private Integer publish;

    @NotNull(message = "Danh sách quyền không được để trống")
    List<Integer> permissions;

    @NotNull(message = "Danh sách users không được để trống")
    private List<Integer> users;
}
