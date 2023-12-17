package com.example.quiz.objects;

public class User {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private Boolean is_validated;

    public User(Long id, String fullName, String email, String role,String password) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.password = password;
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
