package net.joaoqalves.dao;

import com.github.jmnarloch.hstreams.StreamSession;
import org.hibernate.HibernateException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class AbstractDAO<T1 extends Serializable, T2> {

    // Not elegant. Don't know a better way to do it
    private Class<T2> clazz;
    protected StreamSession session;

    public AbstractDAO(final StreamSession session, final Class<T2> clazz) {
        this.clazz = clazz;
        this.session = session;
    }

    public List<T2> getAll() {
        return session.createCriteria(clazz).list();
    }

    public Optional<T2> getOne(final T1 id) {
        return session.getOptional(clazz, id);
    }

    public T2 save(T2 obj) throws HibernateException {
        session.save(obj);
        session.flush();
        return obj;
    }

    public Optional<T2> delete(final String username) {
        Optional<T2> obj = session.getOptional(clazz, username);
        obj.ifPresent(o -> session.delete(o));
        session.flush();
        return obj;
    }

}
