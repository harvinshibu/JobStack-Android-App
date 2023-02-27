package com.harvin.placementapp.Model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class AdsDatabase implements Serializable {

    private String id,idUser;
    private String email,title,description,position,period,location,salary,phone,url,jobCategory;
    public AdsDatabase() {

    }

    public AdsDatabase(String id,String email, String title, String description, String position, String period, String location, String phone, String salary, String url,String jobCatogery,String idUser) {
        this.id=id;
        this.email=email;
        this.title=title;
        this.description=description;
        this.position=position;
        this.period=period;
        this.location=location;
        this.phone=phone;
        this.salary=salary;
        this.url=url;
        this.jobCategory=jobCatogery;
        this.idUser=idUser;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}

