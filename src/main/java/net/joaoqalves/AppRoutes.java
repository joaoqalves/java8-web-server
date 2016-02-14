package net.joaoqalves;

import net.joaoqalves.core.HttpRouter;
import net.joaoqalves.core.route.ContentType;
import net.joaoqalves.core.route.HttpMethod;
import net.joaoqalves.routes.AuthenticationRoutes;
import net.joaoqalves.routes.PagesRoutes;
import net.joaoqalves.routes.UsersRoutes;

public class AppRoutes {

    private HttpRouter router = new HttpRouter(AppUtils.notFoundRoute());

    public AppRoutes(final AuthenticationRoutes authenticationRoutes, final PagesRoutes pagesRoutes,
                     final UsersRoutes usersRoutes) {
        // Login
        router.addRoute(HttpMethod.GET, ContentType.DEFAULT, authenticationRoutes.login);
        router.addRoute(HttpMethod.POST, ContentType.DEFAULT, authenticationRoutes.createSession);
        router.addRoute(HttpMethod.GET, ContentType.DEFAULT, authenticationRoutes.destroySession);
        // Pages
        router.addRoute(HttpMethod.GET, ContentType.DEFAULT, pagesRoutes.page1);
        router.addRoute(HttpMethod.GET, ContentType.DEFAULT, pagesRoutes.page2);
        router.addRoute(HttpMethod.GET, ContentType.DEFAULT, pagesRoutes.page3);
        // ReST API
        router.addRoute(HttpMethod.GET, ContentType.APPLICATION_JSON, usersRoutes.list);
        router.addRoute(HttpMethod.GET, ContentType.APPLICATION_JSON, usersRoutes.get);
        router.addRoute(HttpMethod.POST, ContentType.APPLICATION_JSON, usersRoutes.create);
        router.addRoute(HttpMethod.PUT, ContentType.APPLICATION_JSON, usersRoutes.update);
        router.addRoute(HttpMethod.DELETE, ContentType.APPLICATION_JSON, usersRoutes.delete);
    }

    public HttpRouter getRouter() {
        return router;
    }

    public void setRouter(HttpRouter router) {
        this.router = router;
    }
}
