package com.minhductran.tutorial.minhductran.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class UserCatalogueResponse {
    private final int id;
    private final String name;
    private final String publish;
}
