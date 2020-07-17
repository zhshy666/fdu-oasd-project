package com.oasd.backend.domain;

public class Country {
    private String ISO;
    private String name;

    public Country(String ISO, String name) {
        this.ISO = ISO;
        this.name = name;
    }

    public String getISO() {
        return ISO;
    }

    public void setISO(String ISO) {
        this.ISO = ISO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
