package com.minhductran.tutorial.minhductran.dto.response.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.minhductran.tutorial.minhductran.utils.DateTimeUtils;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String accessTokenExpiresAt;
    private String refreshTokenExpiresAt;
    private Object user; // Thêm field user để lưu thông tin user

    public LoginResponse(String accessToken, String refreshToken, long accessTokenExpiresIn, long refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        
        // Chuyển đổi milliseconds thành thời gian cụ thể
        this.accessTokenExpiresAt = DateTimeUtils.millisecondsToReadableDateTime(accessTokenExpiresIn);
        this.refreshTokenExpiresAt = DateTimeUtils.millisecondsToReadableDateTime(refreshTokenExpiresIn);
    }
}
