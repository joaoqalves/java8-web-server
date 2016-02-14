package net.joaoqalves.core.auth;


import java.util.Optional;

public interface IAuthenticationManager<T> {

    Optional<T> authenticate(final String value);

}
