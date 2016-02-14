package net.joaoqalves.core;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import javafx.util.Pair;
import net.joaoqalves.core.route.Route;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpCoreUtilsTest {

    @Mock
    private HttpExchange httpExchange;

    @Test
    public void getParameter() {
        HashMap<String, List<String>> expected = new HashMap<>();
        String testParam = "param1";
        List<String> testParamValues = Arrays.asList("val1", "val2");

        expected.put(testParam, testParamValues);
        when(httpExchange.getAttribute("parameters")).thenReturn(expected);
        assertEquals(testParamValues, HttpCoreUtils.getParameter(httpExchange, testParam));
    }

    @Test
    public void getFirstParameter() {
        HashMap<String, List<String>> expected = new HashMap<>();
        String testParam = "param1";
        List<String> testParamValues = Arrays.asList("val1", "val2");

        expected.put(testParam, testParamValues);
        when(httpExchange.getAttribute("parameters")).thenReturn(expected);
        assertEquals(testParamValues.get(0), HttpCoreUtils.getFirstParameter(httpExchange, testParam));
    }

    @Test
    public void getPathParameter() {
        HashMap<String, String> expected = new HashMap<>();
        String testParam = "username";
        String testParamValue = "pep_guardiola";

        expected.put(testParam, testParamValue);
        when(httpExchange.getAttribute("pathParameters")).thenReturn(expected);
        assertEquals(testParamValue, HttpCoreUtils.getPathParameter(httpExchange, testParam));
    }

    @Test
    public void extractPathParameters() {
        Map<String, String> expected = new HashMap<>();
        expected.put("uid", "1");
        expected.put("mid", "30");
        expected.put("lid", "50");

        String requestURI = "/users/1/messages/30/line/50";
        Route route = new Route("/users/:uid/messages/:mid/line/:lid", null);

        assertEquals(expected, HttpCoreUtils.extractPathParameters(requestURI, route));
    }

    @Test
    public void extractAuthHeaders() {
        String expectedValue = "Basic 1234";
        Headers expectedHeaders = new Headers();
        expectedHeaders.add(Constants.AUTHORIZATION_HEADER, expectedValue);

        when(httpExchange.getRequestHeaders()).thenReturn(expectedHeaders);
        assertEquals(expectedValue, HttpCoreUtils.extractAuthHeaders(httpExchange));
    }

    @Test
    public void extractCookieHeaders() {
        List<String> cookies = Collections.singletonList("Path=/;JSESSIONID=1a2b3c4d5e");
        Map<String, String> expectedValue = new HashMap<>();
        expectedValue.put("Path", "/");
        expectedValue.put("JSESSIONID", "1a2b3c4d5e");
        Headers expectedHeaders = new Headers();
        expectedHeaders.put(Constants.COOKIE_HEADER, cookies);

        when(httpExchange.getRequestHeaders()).thenReturn(expectedHeaders);
        assertEquals(expectedValue, HttpCoreUtils.extractCookieHeaders(httpExchange));
    }

    @Test
    public void removeEndSlashInURI() {
        String requestURI = "/users/";
        String requestURIWithoutSlash = "/users";

        assertEquals(requestURIWithoutSlash, HttpCoreUtils.removeEndSlash(requestURI));
    }

    @Test
    public void createRoutePatternWithWildcard() {
        String path = "/users/details/*";
        Pattern expected = Pattern.compile("^/users/details/?[a-zA-Z0-9:/]*/?\\??[[a-zA-Z0-9]=[a-zA-Z0-9/]]*$");
        List<String> expectedParams = Collections.emptyList();

        Pair<Pattern, List<String>> result =  HttpCoreUtils.createRoutePattern(path);
        assertEquals(expected.toString(), HttpCoreUtils.createRoutePattern(path).getKey().toString());
        assertEquals(expectedParams,result.getValue());
    }

    @Test
    public void createRoutePatternWithPathParameter() {
        String path = "/users/details/:userid/";
        Pattern expected = Pattern.compile("^/users/details/([a-zA-Z0-9]+)/?\\??[[a-zA-Z0-9]=[a-zA-Z0-9/]]*$");
        List<String> expectedParams = Arrays.asList("userid");

        Pair<Pattern, List<String>> result =  HttpCoreUtils.createRoutePattern(path);
        assertEquals(expected.toString(),result.getKey().toString());
        assertEquals(expectedParams,result.getValue());
    }

    @Test
    public void createRoutePatternWithWildcardAndPathParameter() {
        String path = "/users/details/:userid/*";
        Pattern expected = Pattern
                .compile("^/users/details/([a-zA-Z0-9]+)/?[a-zA-Z0-9:/]*/?\\??[[a-zA-Z0-9]=[a-zA-Z0-9/]]*$");
        List<String> expectedParams = Arrays.asList("userid");

        Pair<Pattern, List<String>> result =  HttpCoreUtils.createRoutePattern(path);
        assertEquals(expected.toString(), result.getKey().toString());
        assertEquals(expectedParams,result.getValue());
    }

    @Test
    public void createRoutePatternForEmptyRoute() {
        assertNull(HttpCoreUtils.createRoutePattern("").getKey());
    }

}
