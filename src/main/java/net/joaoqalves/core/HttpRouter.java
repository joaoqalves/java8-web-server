package net.joaoqalves.core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.joaoqalves.core.route.ContentType;
import net.joaoqalves.core.route.HttpMethod;
import net.joaoqalves.core.route.HttpStatus;
import net.joaoqalves.core.route.Route;
import net.joaoqalves.core.view.IViewHandler;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class HttpRouter implements HttpHandler {

    private Set<Route> routes = new HashSet<>();
    private Map<String, Route> acceptedRoutes = new HashMap<>();
    private Route notFound;

    public HttpRouter(final Route notFound) {
        this.notFound = notFound;
    }

    public void addRoute(final HttpMethod httpMethod, final ContentType contentType,
                         final Route route) {
        acceptedRoutes.put(httpMethod.toString() + contentType.toString(), route);
        routes.add(route);
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    public void handle(final HttpExchange httpExchange) throws IOException {
        final String httpMethod = httpExchange.getRequestMethod().toUpperCase();
        final List<String> acceptHeader = HttpCoreUtils.getAcceptHeaders(httpExchange);
        final String requestURI = httpExchange.getRequestURI().toString();

        if(!acceptHeader.contains(ContentType.DEFAULT.toString())
                && acceptHeader.stream().noneMatch(element -> acceptedRoutes.containsKey(httpMethod + element))) {
            notFound.getHandler().handle(httpExchange);
        } else {
            Route route = routes.parallelStream()
                    .filter(r -> r.matches(requestURI))
                    .sorted((r1, r2) -> r2.getPath().compareTo(r1.getPath()))
                    .findFirst()
                    .orElse(notFound);
            httpExchange.setAttribute(Constants.PATH_PARAMETERS_ATTRIBUTE,
                    HttpCoreUtils.extractPathParameters(requestURI, route));
            route.getHandler().handle(httpExchange);
        }
    }



}
