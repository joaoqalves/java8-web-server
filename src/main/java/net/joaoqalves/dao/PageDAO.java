package net.joaoqalves.dao;

import com.github.jmnarloch.hstreams.StreamSession;
import net.joaoqalves.model.page.Page;

import java.util.Optional;

public class PageDAO extends AbstractDAO<Integer, Page> {

    public PageDAO(StreamSession session) {
        super(session, Page.class);
    }

    public PageDAO(StreamSession session, Class<Page> clazz) {
        super(session, clazz);
    }

    public Optional<Page> findByMatchingRoute(final String page) {
        return getAll().parallelStream().filter(p -> p.matches(page)).findFirst();
    }

}
