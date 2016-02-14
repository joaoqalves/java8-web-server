package net.joaoqalves.dao;

import com.github.jmnarloch.hstreams.StreamSession;
import net.joaoqalves.core.auth.IAuthenticationRepository;
import net.joaoqalves.model.user.User;

import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<String, User> implements IAuthenticationRepository<User> {

    public UserDAO(final StreamSession session) {
        super(session, User.class);
    }

    public UserDAO(final StreamSession session, final Class<User> clazz) {
        super(session, clazz);
    }

    public Optional<User> findByUsernameAndPassword(final String username, final String password) {
        Optional<User> user = session.getOptional(User.class, username);
        return user.filter(u -> u.getPassword().equals(password));
    }
}
