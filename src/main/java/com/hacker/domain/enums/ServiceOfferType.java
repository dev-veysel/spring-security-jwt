package com.hacker.domain.enums;

public enum ServiceOfferType {

    SERVICE_A("Service A"),
    SERVICE_B("Service B");

    private String name;

    ServiceOfferType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}