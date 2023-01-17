package com.berezanskiy.servletExample.controller;

import com.berezanskiy.servletExample.db.DBServerInstance;
import com.berezanskiy.servletExample.models.User;
import com.berezanskiy.servletExample.utils.Utils;
import io.ebean.Database;
import io.javalin.http.Handler;
import jakarta.servlet.http.HttpServletRequest;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class UserController {
    private static final Database DB = DBServerInstance.getInstance();

    public static Handler getUsers = ctx -> {
        PrintWriter out = ctx.res().getWriter();

        List<User> users = DB.find(User.class).findList();

        if (users.size() == 0) {
            out.write("No users!");
        } else {
            for (com.berezanskiy.servletExample.models.User user : users) {
                out.write(user.toString());
            }
        }
        out.close();
    };

    public static Handler getUser = ctx -> {
        Integer id = Integer.parseInt(ctx.pathParam("id"));
        PrintWriter out = ctx.res().getWriter();

        User user = getUserById(id);

        out.write("<html><body>");
        out.write("<h1>" + user + "</h1>");
        out.write("</body></html>");
        out.close();
    };

    public static Handler createUser = ctx -> {
        String userName;

        HashMap<String, String> userData = Utils.getUserData(ctx);

        if (userData != null) {
            userName = createUser(userData);

            PrintWriter out = ctx.res().getWriter();
            out.write(
                    userName != null
                            ? "User " + userName + " has been created successfully"
                            : "User not created"
            );
            out.close();
        }
    };

    public static Handler updateUser = ctx -> {
        User inputUser = getUserFromRequestBody(ctx.req());
        User user = getUserById(inputUser.getId());
        updateUser(user, inputUser);
    };

    public static Handler deleteUser = ctx -> {
        Integer id = Integer.parseInt(ctx.pathParam("id"));
        deleteUser(id);
    };

    private static User getUserFromRequestBody(HttpServletRequest request) throws IOException {
        // Другой вариант парсинга юзера.
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(request.getReader(), User.class);
    }

    private static void deleteUser(int id) {
        User user = getUserById(id);
        DB.delete(user);
    }

    private static void updateUser(User user, User inputUser) {
        user.setLogin(inputUser.getLogin());
        user.setPassword(inputUser.getPassword());

        DB.update(user);
    }

    private static User getUserById(long id) {
        return DB.find(User.class).where().eq("id", id).findOne();
    }

    private static String createUser(HashMap<String, String> userData) {
        String login = userData.get("login");
        String password = userData.get("password");
        User user = new User(login, password);

        DB.save(user);

        return user.getLogin();
    }
}
