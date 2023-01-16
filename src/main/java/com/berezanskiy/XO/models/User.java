package com.berezanskiy.XO.models;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User extends BaseModel {
    private String login;
    private String password;

    public User() {
    }

    public User(Long id, String login, String password) {
        super();
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return id + ". " + login + " " + password + "\n";
    }
}
