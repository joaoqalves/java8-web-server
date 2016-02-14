package net.joaoqalves.core.session;

public interface ISessionRepository<T1, T2> {

    Session<T1, T2> put(T1 id, Session<T1, T2> obj);
    Session<T1, T2> get(T1 id);
    Session<T1, T2> remove(T1 id);

}
