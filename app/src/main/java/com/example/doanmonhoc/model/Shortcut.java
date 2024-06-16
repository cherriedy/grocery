package com.example.doanmonhoc.model;

public class Shortcut {
    private String name;
    private int resource;
    private int color;

    public Shortcut(String name, int resource, int color) {
        this.name = name;
        this.resource = resource;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
