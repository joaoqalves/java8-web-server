package net.joaoqalves.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import net.joaoqalves.core.Constants;
import net.joaoqalves.core.HttpCoreUtils;
import net.joaoqalves.core.auth.AuthenticationStrategy;
import net.joaoqalves.core.auth.IAuthenticationManager;
import net.joaoqalves.core.auth.NotSupportedAuthenticationStrategy;
import net.joaoqalves.core.session.Session;
import net.joaoqalves.core.session.SessionManager;


import java.io.IOException;
import java.util.*;

public class AuthFilter<T1, T2> extends Filter {

    private Map<AuthenticationStrategy, IAuthenticationManager<T2>> authStrategies = new HashMap<>();
    private SessionManager<T1, T2> sessionManager;
    private String sessionCookieName;

    public AuthFilter(final SessionManager<T1, T2> sessionManager, final String sessionCookieName) {
        this.sessionManager = sessionManager;
        this.sessionCookieName = sessionCookieName;
    }

    public void addAuthenticationManager(final AuthenticationStrategy authenticationStrategy,
                                         final IAuthenticationManager<T2> authenticationManager) {
        authStrategies.put(authenticationStrategy, authenticationManager);
    }

    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {

        Optional<Session<T1, T2>> session = Optional.empty();
        String authHeader = HttpCoreUtils.extractAuthHeaders(httpExchange);
        String[] authHeaderValues = authHeader.split(" ");
        Map<String, String> cookies = HttpCoreUtils.extractCookieHeaders(httpExchange);

        if(cookies.containsKey(sessionCookieName)) {
            session = sessionManager.getSession((T1)cookies.get(sessionCookieName))
                    .filter(s -> s.getExpirationDate().getTime() > sessionManager.getDateService().getNow(0).getTime())
                    .map(s -> sessionManager.extendSession(s));
        } else if(authHeaderValues.length == 2) {
            String strategy = authHeaderValues[0];
            String value = authHeaderValues[1];
            Optional<AuthenticationStrategy> authStrategy = Optional
                    .ofNullable(AuthenticationStrategy.fromString(strategy));
            session = authStrategies
                    .get(authStrategy.orElseThrow(() -> new NotSupportedAuthenticationStrategy(strategy)))
                    .authenticate(value).map(u -> sessionManager.createSession(u));

            httpExchange.setAttribute(Constants.SESSION_ATTRIBUTE, session.orElse(null));
            chain.doFilter(httpExchange);
            session.map(s -> sessionManager.removeSession(s.getId()));
            return;
        }

        httpExchange.setAttribute(Constants.SESSION_ATTRIBUTE, session.orElse(null));
        chain.doFilter(httpExchange);
    }

    public String description() {
        return "Creates Session and authenticates via Basic Auth";
    }
}
