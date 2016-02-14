package net.joaoqalves.core.session;


import java.util.Optional;

public class SessionManager<T1, T2> {

    private IDateService dateService;
    private IIdService<T1> idService;
    private ISessionRepository<T1, T2> sessionRepository;
    private long sessionExpiration;

    public SessionManager(final IDateService dateService,
                          final IIdService<T1> idService,
                          final ISessionRepository<T1, T2> sessionRepository,
                          final long sessionExpiration) {
        this.dateService = dateService;
        this.idService = idService;
        this.sessionRepository = sessionRepository;
        this.sessionExpiration = sessionExpiration;
    }

    public Session<T1, T2> createSession(final T2 user) {
        T1 id = idService.generate();
        Session<T1, T2> session = new Session<>(id, user, dateService.getNow(sessionExpiration));
        sessionRepository.put(id, session);
        return session;
    }

    public Optional<Session<T1, T2>> getSession(final T1 id) {
        return Optional.ofNullable(sessionRepository.get(id));
    }

    public Session<T1, T2> extendSession(final Session<T1, T2> session) {
        session.setExpirationDate(dateService.getNow(sessionExpiration));
        return session;
    }

    public Session<T1, T2> removeSession(final T1 id) {
        return sessionRepository.remove(id);
    }

    public IDateService getDateService() {
        return dateService;
    }

    public void setDateService(IDateService dateService) {
        this.dateService = dateService;
    }

    public IIdService<T1> getIdService() {
        return idService;
    }

    public void setIdService(IIdService<T1> idService) {
        this.idService = idService;
    }

    public long getSessionExpiration() {
        return sessionExpiration;
    }

    public void setSessionExpiration(long sessionExpiration) {
        this.sessionExpiration = sessionExpiration;
    }
}
