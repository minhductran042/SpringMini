package com.minhductran.tutorial.minhductran.utils;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ToDoStatus {
    COMPLETED("COMPLETED"),
    IN_PROGRESS("IN_PROGRESS"),
    NOT_STARTED("NOT_STARTED");

    private final String displayName;

    ToDoStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
