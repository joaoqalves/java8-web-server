package net.joaoqalves.model.user;


import com.google.gson.annotations.Expose;
import net.joaoqalves.model.page.Page;
import net.joaoqalves.model.role.Role;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Expose
    private String username;
    @Expose(serialize = false)
    private String password;
    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(name = "users_roles",
            joinColumns=@JoinColumn(table = "users", name = "username"),
            inverseJoinColumns=@JoinColumn(table = "roles", name = "role_id"))
    @Expose
    private Set<Role> roles;
    @Expose
    private String homepage;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
}
