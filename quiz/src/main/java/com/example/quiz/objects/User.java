package com.example.quiz.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private Boolean is_validated;

    @JsonCreator
    public User(
            @JsonProperty("id") Long id,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("role") String role,
            @JsonProperty("is_validated") Boolean is_validated
    ) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.is_validated = is_validated;
    }

    public User(Long id, String fullName, String email, String role, String password) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setIs_validated(Boolean is_validated) {
        this.is_validated = is_validated;
    }

    public String getRole() {
        return role;
    }

    public Boolean getIs_validated() {
        return is_validated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {

        return this.fullName + " " + this.email+ " " + this.role + " " + this.id + " " + this.password;
    }
}
