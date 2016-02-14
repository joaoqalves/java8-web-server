package net.joaoqalves.model.user;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class NewUser {

    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private Set<Integer> roles;
    @Expose
    private String homepage;

    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Integer> getRoles() {
        return roles;
    }

    public void setRoles(Set<Integer> roles) {
        this.roles = roles;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
}
