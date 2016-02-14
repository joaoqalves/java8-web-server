package net.joaoqalves.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import net.joaoqalves.AppUtils;
import net.joaoqalves.core.Constants;
import net.joaoqalves.core.HttpCoreUtils;
import net.joaoqalves.core.auth.IRoleManager;
import net.joaoqalves.core.route.HttpStatus;
import net.joaoqalves.core.route.Route;
import net.joaoqalves.core.session.Session;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleFilter<T1, T2> extends Filter {

    private IRoleManager<T2> roleManager;
    private Set<Route> routes;
    private Set<Route> anonymousRoutes;
    private Route forbiddenRoute;
    private Route loginRoute;


    public RoleFilter(final IRoleManager<T2> roleService,
                      final Set<Route> routes,
                      final Route forbiddenRoute,
                      final Route loginRoute) {
        this.roleManager = roleService;
        this.routes = routes;
        this.anonymousRoutes = routes.parallelStream().filter(Route::isAnonymous).collect(Collectors.toSet());
        this.forbiddenRoute = forbiddenRoute;
        this.loginRoute = loginRoute;
    }

    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        final String requestURI = httpExchange.getRequestURI().toString();

        if(anonymousRoutes.parallelStream().anyMatch(route -> route.matches(requestURI))) {
            chain.doFilter(httpExchange);
        } else {
            Optional<Session<T1, T2>> session = Optional.ofNullable((Session<T1, T2>)httpExchange
                    .getAttribute(Constants.SESSION_ATTRIBUTE));

            Optional<Route> forbidden = session
                    .map(s -> roleManager.canAccess(requestURI, s.getUser()))
                    .filter(access -> !access &&
                            routes.parallelStream().anyMatch(r -> r.matches(requestURI)))
                    .map(route -> forbiddenRoute);

            if(session.isPresent() && !forbidden.isPresent()) {
                chain.doFilter(httpExchange);
            } else if(forbidden.isPresent()) {
                forbidden.get().getHandler().handle(httpExchange);
            } else if(!session.isPresent() &&
                    routes.parallelStream().anyMatch(r -> r.matches(requestURI) && !r.isAnonymous())) {
                HttpCoreUtils.forward(httpExchange, loginRoute.getPath() + "?redirect="+requestURI, HttpStatus.FOUND);
            } else if(requestURI.equals("/")) {
                HttpCoreUtils.forward(httpExchange, loginRoute.getPath(), HttpStatus.FOUND);
            } else {
                // NOT FOUND
                chain.doFilter(httpExchange);
            }
        }

    }

    public String description() {
        return "Check user roles";
    }
}
