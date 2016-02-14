package net.joaoqalves.dao;

import com.github.jmnarloch.hstreams.StreamSession;
import net.joaoqalves.model.role.Role;

public class RoleDAO extends AbstractDAO<Integer, Role> {

    public RoleDAO(final StreamSession session) {
        super(session, Role.class);
    }

    public RoleDAO(final StreamSession session, final Class<Role> clazz) {
        super(session, clazz);
    }
}
