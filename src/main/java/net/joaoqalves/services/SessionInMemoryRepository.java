package net.joaoqalves.services;

import net.joaoqalves.core.session.ISessionRepository;
import net.joaoqalves.core.session.Session;
import net.joaoqalves.model.user.User;

import java.util.HashMap;
import java.util.Map;

public class SessionInMemoryRepository implements ISessionRepository<String, User> {

    private Map<String, Session<String, User>> activeSessions = new HashMap<>();

    public Session<String, User> put(final String id, final Session<String, User> obj) {
        return activeSessions.put(id, obj);
    }

    public Session<String, User> get(final String id) {
        return activeSessions.getOrDefault(id, null);
    }

    public Session<String, User> remove(final String id) {
        return activeSessions.remove(id);
    }
}
