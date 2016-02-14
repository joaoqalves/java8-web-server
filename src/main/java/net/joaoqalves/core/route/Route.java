package net.joaoqalves.core.route;


import javafx.util.Pair;
import net.joaoqalves.core.HttpCoreUtils;

import java.util.List;
import java.util.regex.Pattern;

public class Route {

    private String path;
    private Pattern routePattern;
    private List<String> parameters;
    private IHttpHandler handler;
    private boolean anonymous;

    public Route(final String path, final IHttpHandler handler) {
        this(path, handler, false);
    }

    public Route(final String path, final IHttpHandler handler, final boolean anonymous) {
        this.path = HttpCoreUtils.removeEndSlash(path);
        Pair<Pattern, List<String>> patternParameters = HttpCoreUtils.createRoutePattern(this.path);
        this.routePattern = patternParameters.getKey();
        this.parameters = patternParameters.getValue();
        this.handler = handler;
        this.anonymous = anonymous;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = HttpCoreUtils.removeEndSlash(path);
        Pair<Pattern, List<String>> patternParameters = HttpCoreUtils.createRoutePattern(this.path);
        this.routePattern = patternParameters.getKey();
        this.parameters = patternParameters.getValue();
    }

    public Pattern getRoutePattern() {
        return routePattern;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public IHttpHandler getHandler() {
        return handler;
    }

    public void setHandler(IHttpHandler handler) {
        this.handler = handler;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean matches(final String route) {
        return this.routePattern.matcher(route).find();
    }

}
