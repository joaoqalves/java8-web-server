package net.joaoqalves.core.route;

public enum ContentType {
    DEFAULT("*/*"),
    TEXT_HTML("text/html"),
    FORM_DATA("application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public static ContentType fromString(final String contentType) {
        if (contentType != null) {
            for (ContentType objType : ContentType.values()) {
                if (contentType.equalsIgnoreCase(objType.contentType)) {
                    return objType;
                }
            }
        }
        return null;
    }

    public boolean equals(String contentType) {
        return (contentType != null && this.contentType.equals(contentType));
    }

    public String toString() {
        return this.contentType;
    }

}
