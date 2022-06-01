package com.codingo.model;

public class Course {
    String id;
    String name;
    Integer image;

    public Course(String id, String name, Integer image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Course() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
