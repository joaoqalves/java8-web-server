package net.joaoqalves;

public class AppArguments {

    private int port = 8080;
    private String context = "/";
    private long sessionExpiration = 5*1000*60; // 5 minutes

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public long getSessionExpiration() {
        return sessionExpiration;
    }

    public void setSessionExpiration(long sessionExpiration) {
        this.sessionExpiration = sessionExpiration;
    }
}
