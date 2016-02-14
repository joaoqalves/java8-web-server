package net.joaoqalves.core.session;

import java.util.Date;

public class Session<T1, T2> {

    private T1 id;
    private T2 user;
    private Date expirationDate;

    public Session(T1 id, T2 user, Date expirationDate) {
        this.id = id;
        this.user = user;
        this.expirationDate = expirationDate;
    }

    public T1 getId() {
        return id;
    }

    public void setId(T1 id) {
        this.id = id;
    }

    public T2 getUser() {
        return user;
    }

    public void setUser(T2 user) {
        this.user = user;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
