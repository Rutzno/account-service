package com.diarpy.accountservice.entities;

import jakarta.persistence.*;

import java.util.Set;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.5
 */

@Entity
@Table(name = "groups")
public class Group {
    @Id
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "userGroups")
    private Set<MyUser> myUsers;

    public Group() {}

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MyUser> getMyUsers() {
        return myUsers;
    }

    public void setMyUsers(Set<MyUser> myUsers) {
        this.myUsers = myUsers;
    }
}