package com.oasd.backend.domain;

public class Country {
    private String ISO;
    private String name;
    private String value;

    public Country(){

    }

    public Country(String ISO, String name) {
        this.ISO = ISO;
        this.name = name;
        this.value = name;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
