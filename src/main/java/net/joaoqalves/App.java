package net.joaoqalves;

import com.github.jmnarloch.hstreams.StreamSession;
import com.github.jmnarloch.hstreams.StreamSessionFactory;
import com.github.jmnarloch.hstreams.Streams;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import net.joaoqalves.core.auth.AuthenticationStrategy;
import net.joaoqalves.core.auth.BasicAuthAuthenticationManager;
import net.joaoqalves.core.auth.IAuthenticationManager;
import net.joaoqalves.core.session.IDateService;
import net.joaoqalves.core.session.IIdService;
import net.joaoqalves.core.session.SessionManager;
import net.joaoqalves.dao.PageDAO;
import net.joaoqalves.dao.RoleDAO;
import net.joaoqalves.dao.UserDAO;
import net.joaoqalves.filters.AuthFilter;
import net.joaoqalves.filters.ParameterFilter;
import net.joaoqalves.filters.RoleFilter;
import net.joaoqalves.model.user.User;
import net.joaoqalves.routes.AuthenticationRoutes;
import net.joaoqalves.routes.PagesRoutes;
import net.joaoqalves.routes.UsersRoutes;
import net.joaoqalves.services.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.net.InetSocketAddress;

public class App {

    public static void main(String[] args) throws Exception {

        // Prepared to handle future arguments like port, session expire time, etc
        AppArguments appArguments = new AppArguments();

        // Hibernate Session
        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        StreamSessionFactory factory = Streams.wrap(sessionFactory);
        StreamSession session = factory.openSession();

        // DAO
        UserDAO userDAO = new UserDAO(session);
        RoleDAO roleDAO = new RoleDAO(session);
        PageDAO pageDAO = new PageDAO(session);

        // Create Users
        AppUtils.createUsers(session);

        // Create Session Manager
        IDateService dateService = new DateService();
        IIdService<String> idService = new UuidIdService();
        SessionManager<String, User>  userSessionManager = new SessionManager<>(dateService,
                idService,
                new SessionInMemoryRepository(),
                appArguments.getSessionExpiration());


        // Create Authentication Filter
        AuthFilter<String, User> authFilter = new AuthFilter<>(userSessionManager, AppUtils.COOKIE_NAME);
        IAuthenticationManager<User> authenticationManager = new BasicAuthAuthenticationManager<>(userDAO);
        authFilter.addAuthenticationManager(AuthenticationStrategy.BASIC, authenticationManager);

        // Create Services
        AuthenticationService authenticationService = new AuthenticationService(userDAO, userSessionManager);
        UserService userService = new UserService(userDAO, roleDAO, pageDAO);
        RoleService roleService = new RoleService();

        // Create Routes
        AuthenticationRoutes authenticationRoutes = new AuthenticationRoutes(authenticationService);
        PagesRoutes pagesRoutes = new PagesRoutes();
        UsersRoutes usersRoutes = new UsersRoutes(userService);
        AppRoutes routes = new AppRoutes(authenticationRoutes, pagesRoutes, usersRoutes);

        // Create Role Filter
        RoleFilter<String, User> roleFilter = new RoleFilter<>(roleService, routes.getRouter().getRoutes(),
                AppUtils.forbiddenRoute(), authenticationRoutes.login);

        // Create server
        HttpServer server = HttpServer.create(new InetSocketAddress(appArguments.getPort()), 0);
        HttpContext context = server.createContext(appArguments.getContext(), routes.getRouter());

        // Add Filters to context
        context.getFilters().add(authFilter);
        context.getFilters().add(roleFilter);
        context.getFilters().add(new ParameterFilter());
        server.setExecutor(null);
        server.start();

    }

}
