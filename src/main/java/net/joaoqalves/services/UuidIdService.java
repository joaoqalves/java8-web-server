package net.joaoqalves.services;

import net.joaoqalves.core.session.IIdService;

import java.util.UUID;

public class UuidIdService implements IIdService<String> {

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
