package com.netanel.talk2me.pojo;

public class User {
    private String id;
    private String name;
    private String last;
    private String photo;
    private String email;
    private String phone;
    private boolean isAppInstalled;

    public User() {
    }

    public User(String id, String name, String last, String photo, String email, String phone, boolean isAppInstalled) {
        this.id = id;
        this.name = name;
        this.last = last;
        this.photo = photo;
        this.email = email;
        this.phone = phone;
        this.isAppInstalled = isAppInstalled;
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

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAppInstalled() {
        return isAppInstalled;
    }

    public void setAppInstalled(boolean appInstalled) {
        isAppInstalled = appInstalled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", last='" + last + '\'' +
                ", photo='" + photo + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", isAppInstalled=" + isAppInstalled +
                '}';
    }
}
