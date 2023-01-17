package com.berezanskiy.servletExample.controller;

import io.javalin.http.Handler;

public class RootController {
    public static Handler welcome = ctx -> {
        ctx.render("public/index.html");
    };
}
