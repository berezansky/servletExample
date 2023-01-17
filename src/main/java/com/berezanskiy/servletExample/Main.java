package com.berezanskiy.servletExample;

import com.berezanskiy.servletExample.controller.RootController;
import com.berezanskiy.servletExample.controller.UserController;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {
        Javalin app = getApp();

        app.start(getPort());
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        });

        addRoutes(app);
        app.before(ctx -> ctx.attribute("ctx", ctx));

        return app;
    }

    private static void addRoutes(Javalin app) {
        app.get("/", RootController.welcome);
        app.routes(() -> {
            path("/v1.0/users/", () -> {
                get(UserController.getUsers);
                post(UserController.createUser);
                put(UserController.updateUser);
                path("{id}/", () -> {
                    get(UserController.getUser);
                    delete(UserController.deleteUser);
                });
            });
        });
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "8080");
        return Integer.parseInt(port);
    }
}
