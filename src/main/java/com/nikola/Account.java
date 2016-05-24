package com.nikola;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola on 23/05/2016.
 */

@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;


    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account() {

    }

    public Account(Account acc) {
        this.id = acc.id;
        this.username = acc.username;
        this.password = acc.password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
