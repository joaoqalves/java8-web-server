package net.joaoqalves.core.route;


import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

@FunctionalInterface
public interface IHttpHandler {

    void handle(final HttpExchange httpExchange) throws IOException;

}
