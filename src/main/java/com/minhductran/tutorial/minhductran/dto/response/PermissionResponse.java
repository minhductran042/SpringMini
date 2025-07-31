package com.minhductran.tutorial.minhductran.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class PermissionResponse {
    private final int id;
    private final String name;
    private final Integer publish;
}
