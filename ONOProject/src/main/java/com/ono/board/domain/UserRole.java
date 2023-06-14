package com.ono.board.domain;

import lombok.Getter;


@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
	GUEST("ROLE_GUEST");

    UserRole(String value) {
        this.value = value;
    }

    private String key;
    private String value;
}