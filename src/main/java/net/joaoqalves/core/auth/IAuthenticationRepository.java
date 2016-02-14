package net.joaoqalves.core.auth;

import java.util.Optional;

public interface IAuthenticationRepository<T> {

    Optional<T> findByUsernameAndPassword(final String username, final String password);

}
