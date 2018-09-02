package com.beseech.model;

import java.util.UUID;

public class User {
    private String _password;
    private String _email;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    private String _id;

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    private String _firstName;
    private String _lastName;

    public String getToken() {
        return _token;
    }

    public void setToken(String token) {
        _token = token;
    }

    private String _token;

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public void setState(String email, String password) {
        _email = email;
        _password = password;
    }

    public User() {

    }

    public User(String email, String password) {
        setState(email, password);
    }

    public User(String uid) {
        generateState(uid);
    }

    public void generateState(String uid) {
        setEmail("e" + uid.replaceAll("-", "_") + "@autotest.com");
        setPassword(uid.replaceAll("-", ""));
        setFirstName("FN-" + uid);
        setLastName("LN-" + uid);
    }
}
