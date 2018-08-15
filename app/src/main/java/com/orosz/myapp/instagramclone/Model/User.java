package com.orosz.myapp.instagramclone.Model;

public class User {

    //Since we are using this json structure for our DB we need to keep the variable names the same
    //the primari key is the phone number
    private String Name;
    private String Password;

    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
