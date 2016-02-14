package net.joaoqalves.core.auth;

public class Cookie {

    private String key;
    private String value;
    private String path;

    public Cookie() {
    }

    public Cookie(String key, String value, String path) {
        this.key = key;
        this.value = value;
        this.path = path;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
