package net.joaoqalves.core.view;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Pattern;

public class HtmlViewHandler {

    public static String resolve(final String template) throws IOException {
        return resolve(template, null);
    }

    public static String resolve(final String template, final Map<String, String> variables) throws IOException {
        final InputStreamReader inputStreamReader = new InputStreamReader(
                HtmlViewHandler.class.getClassLoader().getResourceAsStream(template + ".html"));

        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        final StringBuilder html = new StringBuilder();
        while (bufferedReader.ready()) {
            html.append(bufferedReader.readLine());
        }
        bufferedReader.close();

        String htmlString = html.toString();
        if(variables != null && variables.size() > 0) {
            for(Map.Entry<String, String> var: variables.entrySet()) {
                htmlString = Pattern.compile("\\{\\{" + var.getKey() + "\\}\\}").matcher(html).replaceAll(var.getValue());
            }
        }
        return htmlString;
    }

}
