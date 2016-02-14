package net.joaoqalves.core.auth;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Optional;

public class BasicAuthAuthenticationManager<T> implements IAuthenticationManager<T> {

    private IAuthenticationRepository<T> authenticationRepository;

    public BasicAuthAuthenticationManager(IAuthenticationRepository<T> authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    public Optional<T> authenticate(final String value) {
        Optional<String> decoded = Optional.empty();
        try {
            decoded = Optional.of(new String(Base64.getDecoder().decode(value), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // Substitute by logger
        }

        String[] splitted = decoded.map(x -> x.split(":")).orElse(new String[]{"", ""});
        return authenticationRepository.findByUsernameAndPassword(splitted[0], splitted[1]);
    }

}
