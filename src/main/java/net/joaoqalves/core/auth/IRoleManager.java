package net.joaoqalves.core.auth;

public interface IRoleManager<T> {
    boolean canAccess(final String path, final T user);
}
