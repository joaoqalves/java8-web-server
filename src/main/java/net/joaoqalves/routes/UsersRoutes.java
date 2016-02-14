package net.joaoqalves.routes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.joaoqalves.core.HttpCoreUtils;
import net.joaoqalves.core.route.HttpStatus;
import net.joaoqalves.core.route.Route;
import net.joaoqalves.model.EntityNotValidException;
import net.joaoqalves.model.HttpExceptionWrapper;
import net.joaoqalves.model.page.InvalidPageException;
import net.joaoqalves.model.role.InvalidRoleException;
import net.joaoqalves.model.user.*;
import net.joaoqalves.services.UserService;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

public class UsersRoutes {

    private UserService userService;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public UsersRoutes(UserService userService) {
        this.userService = userService;
    }

    public Route list = new Route("/users", (httpExchange -> {
        List<User> users = userService.getAll();
        HttpCoreUtils.sendResponse(httpExchange, gson.toJson(users), HttpStatus.OK);
    }));

    public Route get = new Route("/users/:username", (httpExchange -> {
        String username = HttpCoreUtils.getPathParameter(httpExchange, "username");
        Optional<User> user = userService.getOne(username);
        if(user.isPresent()) {
            HttpCoreUtils.sendResponse(httpExchange, gson.toJson(user.get()), HttpStatus.OK);
        } else {
            HttpCoreUtils.sendResponse(httpExchange, gson.toJson(user), HttpStatus.NOT_FOUND);
        }
    }));

    public Route create = new Route("/users/admin/create", (httpExchange -> {
        JsonReader reader = new JsonReader(new InputStreamReader(httpExchange.getRequestBody(), "UTF-8"));
        NewUser newUser = gson.fromJson(reader, NewUser.class);
        try {
            User user = userService.create(newUser);
            HttpCoreUtils.sendResponse(httpExchange, gson.toJson(user), HttpStatus.OK);
        } catch (EntityNotValidException | InvalidRoleException | InvalidPageException e) {
            HttpExceptionWrapper exWrapper = new HttpExceptionWrapper(e.getMessage());
            HttpCoreUtils.sendResponse(httpExchange, gson.toJson(exWrapper), HttpStatus.BAD_REQUEST);
        } catch (UserAlreadyExistsException e) {
            HttpExceptionWrapper exWrapper = new HttpExceptionWrapper(e.getMessage());
            HttpCoreUtils.sendResponse(httpExchange, gson.toJson(exWrapper), HttpStatus.CONFLICT);
        }
    }));

    public Route update = new Route("/users/admin/:username/update", (httpExchange -> {
        String username = HttpCoreUtils.getPathParameter(httpExchange, "username");
        JsonReader reader = new JsonReader(new InputStreamReader(httpExchange.getRequestBody(), "UTF-8"));
        UpdateUser updateUser = gson.fromJson(reader, UpdateUser.class);
        try {
            User user = userService.update(username, updateUser);
            HttpCoreUtils.sendResponse(httpExchange, gson.toJson(user), HttpStatus.OK);
        } catch (InvalidRoleException | InvalidPageException e) {
            HttpExceptionWrapper exWrapper = new HttpExceptionWrapper(e.getMessage());
            HttpCoreUtils.sendResponse(httpExchange, gson.toJson(exWrapper), HttpStatus.BAD_REQUEST);
        } catch (UserNotExists e) {
            HttpExceptionWrapper exWrapper = new HttpExceptionWrapper(e.getMessage());
            HttpCoreUtils.sendResponse(httpExchange, gson.toJson(exWrapper), HttpStatus.NOT_FOUND);
        }
    }));

    public Route delete = new Route("/users/admin/:username/delete", (httpExchange -> {
        String username = HttpCoreUtils.getPathParameter(httpExchange, "username");
        try {
            User user = userService.delete(username);
            HttpCoreUtils.sendResponse(httpExchange, gson.toJson(user), HttpStatus.OK);
        } catch (UserNotExists e) {
            HttpExceptionWrapper exWrapper = new HttpExceptionWrapper(e.getMessage());
            HttpCoreUtils.sendResponse(httpExchange, gson.toJson(exWrapper), HttpStatus.NOT_FOUND);
        }
    }));

}
