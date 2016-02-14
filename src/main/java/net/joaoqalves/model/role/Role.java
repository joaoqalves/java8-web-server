package net.joaoqalves.model.role;

import com.google.gson.annotations.Expose;
import net.joaoqalves.model.page.Page;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    @Expose
    private Integer id;
    @Expose
    private String name;
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "roles_pages",
            joinColumns=@JoinColumn(table = "roles", name = "role_id"),
            inverseJoinColumns=@JoinColumn(table = "pages", name = "page_id"))
    @Expose
    private Set<Page> pages;

    public Role() {
    }

    public Role(String name, Set<Page> pages) {
        this.name = name;
        this.pages = pages;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Page> getPages() {
        return pages;
    }

    public void setPages(Set<Page> pages) {
        this.pages = pages;
    }
}
