package edu.octavio.simplified_picpay.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserType {
    COMMON,
    MERCHANT;

    @JsonCreator
    public static UserType fromString(String value) {
        for (UserType userType : UserType.values()) {
            if (userType.name().equalsIgnoreCase(value)) {
                return userType;
            }
        }

        throw new IllegalArgumentException("Invalid user type: " + value);
    }
}
