package com.minhductran.tutorial.minhductran.utils;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserStatus {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    NONE("NONE");
    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
