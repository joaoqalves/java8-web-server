package net.joaoqalves.core.auth;


public enum AuthenticationStrategy {
    BASIC("Basic"),
    TOKEN("Bearer");

    private final String authenticationStrategy;

    AuthenticationStrategy(String authenticationStrategy) {
        this.authenticationStrategy = authenticationStrategy;
    }

    public static AuthenticationStrategy fromString(String strategy) {
        if (strategy != null) {
            for (AuthenticationStrategy objType : AuthenticationStrategy.values()) {
                if (strategy.equalsIgnoreCase(objType.authenticationStrategy)) {
                    return objType;
                }
            }
        }
        return null;
    }

    public boolean equals(String authenticationStrategy) {
        return (authenticationStrategy != null && this.authenticationStrategy.equals(authenticationStrategy));
    }

    public String toString() {
        return this.authenticationStrategy;
    }
}
