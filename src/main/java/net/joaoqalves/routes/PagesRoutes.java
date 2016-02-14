package net.joaoqalves.routes;

import net.joaoqalves.core.Constants;
import net.joaoqalves.core.HttpCoreUtils;
import net.joaoqalves.core.route.HttpStatus;
import net.joaoqalves.core.route.Route;
import net.joaoqalves.core.session.Session;
import net.joaoqalves.core.view.HtmlViewHandler;
import net.joaoqalves.model.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PagesRoutes {

    private HtmlViewHandler htmlViewHandler = new HtmlViewHandler();

    public Route page1 = new Route("/page1", (httpExchange) -> {
        Session<String, User> httpSession =
                (Session)httpExchange.getAttribute(Constants.SESSION_ATTRIBUTE);
        Map<String, String> variables = new HashMap<>();
        variables.put("username", httpSession.getUser().getUsername());
        HttpCoreUtils.sendResponse(httpExchange, htmlViewHandler.resolve("pages/page1", variables), HttpStatus.OK);
    });

    public Route page2 = new Route("/page2", (httpExchange) -> {
        Session<String, User> httpSession =
                (Session)httpExchange.getAttribute(Constants.SESSION_ATTRIBUTE);
        Map<String, String> variables = new HashMap<>();
        variables.put("username", httpSession.getUser().getUsername());
        HttpCoreUtils.sendResponse(httpExchange, htmlViewHandler.resolve("pages/page1", variables), HttpStatus.OK);
    });

    public Route page3 = new Route("/page3", (httpExchange) -> {
        Session<String, User> httpSession =
                (Session)httpExchange.getAttribute(Constants.SESSION_ATTRIBUTE);
        Map<String, String> variables = new HashMap<>();
        variables.put("username", httpSession.getUser().getUsername());
        HttpCoreUtils.sendResponse(httpExchange, htmlViewHandler.resolve("pages/page1", variables), HttpStatus.OK);
    });

}
