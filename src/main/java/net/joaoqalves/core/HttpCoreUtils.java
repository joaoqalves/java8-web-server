package net.joaoqalves.core;

import com.sun.net.httpserver.HttpExchange;
import javafx.util.Pair;
import net.joaoqalves.core.auth.Cookie;
import net.joaoqalves.core.route.HttpStatus;
import net.joaoqalves.core.route.Route;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HttpCoreUtils {

    public static List<String> getParameter(final HttpExchange httpExchange, final String key) {
        Optional<Map<String, List<String>>> parameters = Optional.ofNullable((HashMap<String, List<String>>)
                httpExchange.getAttribute(Constants.PARAMETERS_ATTRIBUTE));
        return parameters.orElse(new HashMap<>()).getOrDefault(key, Collections.singletonList(""));
    }

    public static String getFirstParameter(final HttpExchange httpExchange, final String key) {
        return getParameter(httpExchange, key).get(0);
    }

    public static String getPathParameter(final HttpExchange httpExchange, final String key) {
        Optional<Map<String, String>> parameters = Optional.ofNullable((HashMap<String, String>)
                httpExchange.getAttribute(Constants.PATH_PARAMETERS_ATTRIBUTE));
        return parameters.orElse(new HashMap<>()).getOrDefault(key, "");
    }

    public static Map<String, String> extractPathParameters(final String requestURI, final Route route) {
        Map<String, String> parameters = new HashMap<>();
        if(route.getRoutePattern() == null) {
            return parameters;
        }
        Matcher m = route.getRoutePattern().matcher(requestURI);
        if(m.find()) {
            for(int i = 0; i < route.getParameters().size(); i++) {
                parameters.put(route.getParameters().get(i), m.group(i+1));
            }
        }
        return parameters;
    }

    public static String extractAuthHeaders(final HttpExchange httpExchange) {
        return httpExchange.getRequestHeaders()
                .getOrDefault(Constants.AUTHORIZATION_HEADER, Collections.singletonList(""))
                .get(0);
    }

    public static Map<String, String> extractCookieHeaders(final HttpExchange httpExchange) {
        return httpExchange.getRequestHeaders()
                .getOrDefault(Constants.COOKIE_HEADER, Collections.emptyList())
                .stream()
                .map(cookie -> Arrays.asList(cookie.split(";")))
                .flatMap(Collection::stream)
                .map(cookie -> cookie.split("="))
                .collect(Collectors.toMap(key -> key[0].trim(), value-> value[1]));
    }

    public static List<String> getAcceptHeaders(final HttpExchange httpExchange) {
        return httpExchange.getRequestHeaders()
                .getOrDefault(Constants.ACCEPT_HEADER, Collections.emptyList())
                .parallelStream()
                .map(header -> Arrays.asList(header.split(",")))
                .flatMap(Collection::stream)
                .map(element -> element.split(";"))
                .map(accept -> accept[0])
                .collect(Collectors.toList());
    }

    public static void forward(final HttpExchange httpExchange, final String url, final int httpStatus)
            throws IOException {
        httpExchange.getResponseHeaders().add(Constants.LOCATION_HEADER, url);
        httpExchange.sendResponseHeaders(httpStatus, -1);
    }

    public static void sendResponse(final HttpExchange httpExchange, final String response,
                                    final int httpStatus) throws IOException {
        httpExchange.sendResponseHeaders(httpStatus, response.length());
        httpExchange.getResponseBody().write(response.getBytes());
        httpExchange.getResponseBody().close();
    }

    public static String removeEndSlash(final String path) {
        if(path != null && !path.isEmpty() && path.charAt(path.length() - 1) == '/') {
            return path.substring(0, path.length() - 1);
        }

        return path;
    }

    // Naive strategy. Probably needs improvement
    public static Pair<Pattern, List<String>> createRoutePattern(final String path) {
        if(path == null || path.isEmpty()) {
            return new Pair<>(null, Collections.emptyList());
        }

        String[] tokens = path.split("/");
        List<String> parameters = new ArrayList<>();
        for(int i = 0; i < tokens.length; i++) {
            if(tokens[i].isEmpty())
                continue;
            if(tokens[i].startsWith(":")) {
                parameters.add(tokens[i].substring(1));
                tokens[i] = "([a-zA-Z0-9]+)";
            } else if(tokens[i].equals("*")) {
                tokens[i] = "?[a-zA-Z0-9:/]*";
            }
        }
        return new Pair<>(Pattern.compile("^" + String.join("/", tokens) + "/?\\??[[a-zA-Z0-9]=[a-zA-Z0-9/]]*$"), parameters);
    }

    public static void setCookie(final HttpExchange httpExchange, final Cookie cookie) {
        setCookie(httpExchange, cookie, null);
    }

    public static void setCookie(final HttpExchange httpExchange, final Cookie cookie, final String expireDate) {
        StringBuilder sb = new StringBuilder();
        sb.append(cookie.getKey()).append("=").append(cookie.getValue()).append(";")
                .append("Path=").append(cookie.getPath()).append(";");

        if(expireDate != null) {
            sb.append("expires=").append(expireDate);
        }
        httpExchange.getResponseHeaders().add("Set-Cookie", sb.toString());
    }

}
