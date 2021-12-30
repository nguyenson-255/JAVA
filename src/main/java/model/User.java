package model;


import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String name;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '}';
    }
    public boolean checkAccount(String username,String password){
        if (username.equals(this.username) && password.equals(this.password)){
            return true;
        }
        return false;
    }
}
