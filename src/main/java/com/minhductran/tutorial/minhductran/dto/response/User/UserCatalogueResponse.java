package com.minhductran.tutorial.minhductran.dto.response.User;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class UserCatalogueResponse {
    private final int id;
    private final String name;
    private final Integer publish;
}
