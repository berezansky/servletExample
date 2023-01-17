package com.berezanskiy.servletExample.utils;

import com.berezanskiy.servletExample.pojo.Error;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.JavalinValidation;
import io.javalin.validation.ValidationError;
import io.javalin.validation.Validator;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static HashMap<String, String> getUserData(Context ctx) throws IOException {
        Validator<String> loginValidator = ctx.formParamAsClass("login", String.class)
                .check(value -> {
                    if (value != null) {
                        return value.length() > 0;
                    }
                    return false;
                }, "Login cannot be empty")
                .check(value -> value.length() > 7, "Amount of chars in login should be greater than 7");

        Validator<String> passwordValidator = ctx.formParamAsClass("password", String.class)
                .check(value -> {
                    if (value != null) {
                        return value.length() > 0;
                    }
                    return false;
                }, "Password cannot be empty")
                .check(value -> value.length() > 8, "Password should be greater than 8");

        Map<String, List<ValidationError<?>>> errors = JavalinValidation.collectErrors(loginValidator, passwordValidator);

        if (!errors.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Error> errorList = errors
                    .values()
                    .stream()
                    .flatMap(List::stream)
                            .map(v -> new Error(v.getMessage()))
                                    .collect(Collectors.toList());

            ctx.res().setStatus(HttpStatus.UNPROCESSABLE_CONTENT.getCode());
            ctx.res().getWriter().write(String.valueOf(objectMapper.writeValueAsString(errorList)));
            ctx.res().getWriter().close();

            return null;
        }

        HashMap<String, String> userData = new HashMap<>();

        userData.put("login", loginValidator.get());
        userData.put("password", passwordValidator.get());

        return userData;
    }
}
