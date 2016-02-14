package net.joaoqalves.core.auth;

import java.io.IOException;

public class NotSupportedAuthenticationStrategy extends IOException {
    public NotSupportedAuthenticationStrategy() {
    }

    public NotSupportedAuthenticationStrategy(final String strategy) {
        super(strategy + " not supported");
    }
}
