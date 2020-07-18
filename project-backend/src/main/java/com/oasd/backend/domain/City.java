package com.oasd.backend.domain;

public class City {
    private int id;
    private String name;
    private String value;

    public City() {
    }

    public City(int id, String name) {
        this.id = id;
        this.name = name;
        this.value = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
