package net.joaoqalves.routes;

import net.joaoqalves.core.Constants;
import net.joaoqalves.core.HttpCoreUtils;
import net.joaoqalves.core.route.HttpStatus;
import net.joaoqalves.core.route.Route;
import net.joaoqalves.core.session.Session;
import net.joaoqalves.core.view.HtmlViewHandler;
import net.joaoqalves.model.user.User;
import net.joaoqalves.model.user.UserNotFoundException;
import net.joaoqalves.services.AuthenticationService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthenticationRoutes {

    private HtmlViewHandler htmlViewHandler = new HtmlViewHandler();
    private AuthenticationService authenticationService;

    public AuthenticationRoutes(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public Route login = new Route("/login", (httpExchange) -> {
        Optional<Session<String, User>> httpSession = Optional.ofNullable(
                (Session)httpExchange.getAttribute(Constants.SESSION_ATTRIBUTE));
        if(httpSession.isPresent()) {
            HttpCoreUtils.forward(httpExchange,
                    httpSession.get().getUser().getHomepage(),
                    HttpStatus.FOUND);
        } else {
            Map<String, String> variables = new HashMap<>();
            String createSessionAction =  "/sessions/create";
            String redirectTo = HttpCoreUtils.getFirstParameter(httpExchange, "redirect");
            createSessionAction = redirectTo.isEmpty() ? createSessionAction : createSessionAction + "?redirect=" + redirectTo;
            variables.put("postaction", createSessionAction);
            HttpCoreUtils.sendResponse(httpExchange,
                    htmlViewHandler.resolve("auth/login", variables),
                    HttpStatus.OK);
        }
    }, true);


    public Route createSession = new Route("/sessions/create", (httpExchange) -> {
        try {
            User user = authenticationService.authenticate(httpExchange);
            String redirectPage = HttpCoreUtils.getFirstParameter(httpExchange, "redirect");
            HttpCoreUtils.forward(httpExchange, (redirectPage.isEmpty() ? user.getHomepage() : redirectPage), HttpStatus.FOUND);
        } catch (UserNotFoundException userNotFoundException) {
            HttpCoreUtils.forward(httpExchange, "/login", HttpStatus.FOUND);
        }
    }, true);

    public Route destroySession = new Route("/sessions/destroy", (httpExchange) -> {
        authenticationService.logout(httpExchange);
        HttpCoreUtils.forward(httpExchange, "/login", HttpStatus.FOUND);
    }, true);

}
