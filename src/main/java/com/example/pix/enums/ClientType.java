package com.example.pix.enums;

import lombok.Getter;

@Getter
public enum ClientType {

    FISICA("fisica"),
    JURIDICA("juridica");

    private final String value;

    ClientType(String value) {
        this.value = value;
    }
}
