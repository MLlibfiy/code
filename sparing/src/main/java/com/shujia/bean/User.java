package com.shujia.bean;

public class User {

    private String usernmae;

    private String password;

    public User(String usernmae, String password) {
        this.usernmae = usernmae;
        this.password = password;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "usernmae='" + usernmae + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsernmae() {
        return usernmae;
    }

    public void setUsernmae(String usernmae) {
        this.usernmae = usernmae;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
