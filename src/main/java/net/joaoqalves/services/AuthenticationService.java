package net.joaoqalves.services;

import com.sun.net.httpserver.HttpExchange;
import net.joaoqalves.AppUtils;
import net.joaoqalves.core.Constants;
import net.joaoqalves.core.HttpCoreUtils;
import net.joaoqalves.core.route.HttpStatus;
import net.joaoqalves.core.session.Session;
import net.joaoqalves.core.session.SessionManager;
import net.joaoqalves.dao.UserDAO;
import net.joaoqalves.model.user.User;
import net.joaoqalves.model.user.UserNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class AuthenticationService {

    private UserDAO userDAO;
    private SessionManager<String, User> sessionManager;

    public AuthenticationService(final UserDAO userDAO, final SessionManager<String, User> sessionManager) {
        this.userDAO = userDAO;
        this.sessionManager = sessionManager;
    }

    public User authenticate(final HttpExchange httpExchange) throws UserNotFoundException, IOException {
        String username = HttpCoreUtils.getFirstParameter(httpExchange, "username");
        String password = HttpCoreUtils.getFirstParameter(httpExchange, "password");

        User user = userDAO.findByUsernameAndPassword(username, password).orElseThrow(UserNotFoundException::new);
        AppUtils.setCookies(httpExchange, sessionManager.createSession(user));
        return user;
    }

    public void logout(final HttpExchange httpExchange) throws IOException {
        Optional<Session<String, User>> session = Optional.ofNullable((Session) httpExchange.getAttribute(Constants.SESSION_ATTRIBUTE));
        session.map(s -> sessionManager.removeSession(s.getId()));
        AppUtils.removeCookies(httpExchange);
    }

}
