package com.codingo.model;

public class User {
    private String name;
    private String email;
    private String photo;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public String getEnrolled() {
        if (enrolled == null) enrolled = "";
        return enrolled;
    }

    public void setEnrolled(String enrolled) {
        if (enrolled == null) enrolled = "";
        this.enrolled = enrolled;
    }

    private String enrolled;
    private String id;

    public String getId() {
        if (id == null) id = "";
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

    public String getEmail() {
        if (email == null) email = "";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
