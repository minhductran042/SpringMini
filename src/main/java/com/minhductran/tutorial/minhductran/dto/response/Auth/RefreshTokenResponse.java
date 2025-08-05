package com.minhductran.tutorial.minhductran.dto.response.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.minhductran.tutorial.minhductran.utils.DateTimeUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {
    private String accessToken;
    private String accessTokenExpiresAt;

    public RefreshTokenResponse(String accessToken, long accessTokenExpiresIn) {
        this.accessToken = accessToken;
        
        // Chuyển đổi milliseconds thành thời gian cụ thể
        this.accessTokenExpiresAt = DateTimeUtils.millisecondsToReadableDateTime(accessTokenExpiresIn);
    }
} 