package com.harvin.placementapp.Model;

import java.io.Serializable;

public class Users {
    String id,email,name,phone,status;

    public Users(String id, String email, String name, String phone,String status) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.status=status;
    }

    public Users() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
