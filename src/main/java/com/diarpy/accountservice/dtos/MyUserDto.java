package com.diarpy.accountservice.dtos;

import com.diarpy.accountservice.entities.MyUser;
import com.diarpy.accountservice.entities.Group;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * @author Mack_TB
 * @since 23/06/2024
 * @version 1.0.6
 */

public class MyUserDto {
    private Long id;
    @JsonProperty("name")
    private String firstName;
    @JsonProperty("lastname")
    private String lastName;
    private String email;
    private Set<String> roles;

    public MyUserDto(MyUser myUser) {
        this.id = myUser.getId();
        this.firstName = myUser.getFirstName();
        this.lastName = myUser.getLastName();
        this.email = myUser.getEmail();
        this.roles = getRoles(myUser);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getRoles(MyUser myUser) {
        Set<String> result = new TreeSet<>(); // use treeSet to have a sorted set
        for (Group group : myUser.getUserGroups()) {
            result.add(group.getName());
        }
        return result;
    }
}