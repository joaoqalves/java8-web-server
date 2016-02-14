package net.joaoqalves.core.view;

import java.io.IOException;
import java.util.Map;

public interface IViewHandler {

    String resolve(final String template, final Map<String, String> variables) throws IOException;

}
