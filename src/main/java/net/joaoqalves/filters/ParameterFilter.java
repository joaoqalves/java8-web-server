package net.joaoqalves.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import net.joaoqalves.core.Constants;
import net.joaoqalves.core.route.ContentType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

/**
 * Based on http://whowish-programming.blogspot.com.es/2011/04/get-post-parameters-from-java-http.html
 */
public class ParameterFilter extends Filter {

    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        parseGetParameters(httpExchange);
        parsePostParameters(httpExchange);
        chain.doFilter(httpExchange);
    }

    private void parseGetParameters(HttpExchange exchange)
            throws UnsupportedEncodingException {

        Map<String, List<String>> parameters = new HashMap<>();
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
        exchange.setAttribute(Constants.PARAMETERS_ATTRIBUTE, parameters);
    }

    private void parsePostParameters(HttpExchange exchange)
            throws IOException {

        String contentType = exchange.getRequestHeaders()
                .getOrDefault(Constants.CONTENT_TYPE_HEADER, Collections.singletonList(""))
                .get(0);

        if ("post".equalsIgnoreCase(exchange.getRequestMethod())
                && ContentType.fromString(contentType) == ContentType.FORM_DATA) {
            Map<String, List<String>> parameters = (Map)exchange.getAttribute(Constants.PARAMETERS_ATTRIBUTE);
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);
        }
    }

    private void parseQuery(String query, Map<String, List<String>> parameters)
            throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");

            for (String pair : pairs) {
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    parameters.get(key).add(value);
                } else {
                    parameters.put(key, new ArrayList<>(Collections.singletonList(value)));
                }
            }
        }
    }

    public String description() {
        return "Parses GET and POST parameters";
    }
}


