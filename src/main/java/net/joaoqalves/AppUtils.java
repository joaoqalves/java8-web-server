package net.joaoqalves;

import com.github.jmnarloch.hstreams.StreamSession;
import com.sun.net.httpserver.HttpExchange;
import net.joaoqalves.core.Constants;
import net.joaoqalves.core.HttpCoreUtils;
import net.joaoqalves.core.auth.Cookie;
import net.joaoqalves.core.route.HttpStatus;
import net.joaoqalves.core.route.Route;
import net.joaoqalves.core.session.Session;
import net.joaoqalves.core.view.HtmlViewHandler;
import net.joaoqalves.core.view.IViewHandler;
import net.joaoqalves.model.page.Page;
import net.joaoqalves.model.role.Role;
import net.joaoqalves.model.user.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class AppUtils {

    // Cookies
    public static final String COOKIE_NAME = "JSESSIONID";
    public static final DateFormat EXPIRES_DATE_FORMAT = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz");

    public static Route unauthorizedRoute() {
        return new Route(null, (httpExchange) -> {
            HttpCoreUtils.sendResponse(httpExchange, HtmlViewHandler.resolve("error/401", null), HttpStatus.UNAUTHORIZED);
        }, true);
    }

    public static Route forbiddenRoute() {
        return new Route(null, (httpExchange) -> {
            HttpCoreUtils.sendResponse(httpExchange, HtmlViewHandler.resolve("error/403", null), HttpStatus.FORBIDDEN);
        }, true);
    }

    public static Route notFoundRoute() {
        return new Route(null, (httpExchange) -> {
            HttpCoreUtils.sendResponse(httpExchange, HtmlViewHandler.resolve("error/404", null), HttpStatus.NOT_FOUND);
        }, true);
    }

    public static void setCookies(final HttpExchange httpExchange, final Session<String, User> session) {
        Cookie c = new Cookie(COOKIE_NAME, session.getId(), "/");
        HttpCoreUtils.setCookie(httpExchange, c);
    }

    public static void removeCookies(final HttpExchange httpExchange) {
        Cookie c = new Cookie(COOKIE_NAME, "", "/");
        HttpCoreUtils.setCookie(httpExchange, c, EXPIRES_DATE_FORMAT.format(new Date(0)));
    }

    public static void createUsers(final StreamSession session) {

        // CREATE ROLES

        // ADMIN
        Role roleAdmin = new Role("role_admin", new HashSet<>(Collections.singletonList(new Page("/users/*"))));
        session.save(roleAdmin);
        // Read REST
        Page allUsers = new Page("/users");
        Page userDetails = new Page("/users/:username");
        Role readRest = new Role("role_read_rest", new HashSet<>(Arrays.asList(allUsers, userDetails)));
        session.save(readRest);
        // Page 1
        Page page1 = new Page("/page1");
        Role rolePage1 = new Role("role_page1", new HashSet<>(Collections.singletonList(page1)));
        session.save(rolePage1);
        // Page 2
        Page page2 = new Page("/page2");
        Role rolePage2 = new Role("role_page2", new HashSet<>(Collections.singletonList(page2)));
        session.save(rolePage2);
        // Page 3
        Page page3 = new Page("/page22");
        Role rolePage3 = new Role("role_page3", new HashSet<>(Collections.singletonList(page3)));
        session.save(rolePage3);


        // CREATE USERS

        // admin
        User admin = new User("admin", "nimda");
        admin.setHomepage("/users");
        admin.setRoles(new HashSet<>(Collections.singletonList(roleAdmin)));
        session.save(admin);

        // user1
        User user1 = new User("user1", "1user");
        user1.setHomepage("/page1");
        user1.setRoles(new HashSet<>(Arrays.asList(readRest, rolePage1)));
        session.save(user1);

        // user2
        User user2 = new User("user2", "2user");
        user2.setHomepage("/page2");
        user2.setRoles(new HashSet<>(Arrays.asList(readRest, rolePage2)));
        session.save(user2);

        // user3
        User user3 = new User("user3", "3user");
        user3.setHomepage("/page3");
        user3.setRoles(new HashSet<>(Arrays.asList(readRest, rolePage3)));
        session.save(user3);

        session.flush();
    }

}
