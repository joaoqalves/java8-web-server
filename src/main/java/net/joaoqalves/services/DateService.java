package net.joaoqalves.services;

import net.joaoqalves.core.session.IDateService;

import java.util.Date;

public class DateService implements IDateService {

    public Date getNow(long millis) {
        return new Date(new Date().getTime() + millis);
    }
}
