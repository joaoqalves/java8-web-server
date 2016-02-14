package net.joaoqalves.model.page;

import com.google.gson.annotations.Expose;
import net.joaoqalves.core.HttpCoreUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.regex.Pattern;

@Entity
@Table(name = "pages")
public class Page {

    @Id
    @GeneratedValue
    @Expose
    private Integer id;
    @Expose
    private String page;
    private Pattern pagePattern;

    public Page() {
    }

    public Page(String page) {
        this.page = page;
        this.pagePattern = HttpCoreUtils.createRoutePattern(page).getKey();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
        this.pagePattern = HttpCoreUtils.createRoutePattern(page).getKey();
    }

    public Pattern getPagePattern() {
        return pagePattern;
    }

    public boolean matches(final String path) {
        return this.pagePattern.matcher(path).matches();
    }

}
