package com.berezanskiy.XO.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.berezanskiy.XO.db.DBServerInstance;
import com.berezanskiy.XO.models.User;
import com.berezanskiy.XO.utils.LoggerFactory;
import com.berezanskiy.XO.utils.Utils;
import jakarta.servlet.http.*;
import org.codehaus.jackson.map.ObjectMapper;

public class UserServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServlet.class);

    private void writeAllUsers(PrintWriter out) {
        LOGGER.log(Level.INFO, "All users start writing");

        List<User> users = DBServerInstance.getInstance().find(User.class).findList();

        if (users.size() == 0) {
            out.write("No users!");
        } else {
            for (com.berezanskiy.XO.models.User user : users) {
                out.write(user.toString());
            }
        }
    }

    private void writeUserInfo(PrintWriter out, Integer id) {
        User user = getUserById(id);

        out.write("<html><body>");
        out.write("<h1>" + user + "</h1>");
        out.write("</body></html>");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = getAccountIdFromPath(request);
        PrintWriter out = response.getWriter();

        if (id == null) {
            writeAllUsers(out);
        } else {
            writeUserInfo(out, id);
        }

        out.close();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        String userName = null;

        LinkedHashMap<String, Object> userData = Utils.getData(reader);

        if (userData != null) {
            userName = createUser(userData);
        }

        PrintWriter pw = response.getWriter();
        pw.write(
                userName != null
                        ? "User " + userName + " has been created successfully"
                        : "User not created"
        );
        pw.close();
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User inputUser = getUserFromRequestBody(request);
        User user = getUserById(inputUser.getId());
        updateUser(user, inputUser);
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Integer id = getAccountIdFromPath(req);

        if (id != null) {
            deleteUser(id);
        }
    }

    /**
     * Получение идентификатора из ссылки.
     */
    private Integer getAccountIdFromPath(HttpServletRequest req) {
        String path = req.getPathInfo();
        String[] pathVariables = path.split("/");
        return Arrays.stream(pathVariables)
                .filter(Objects::nonNull)
                .filter(v -> !Objects.equals(v, ""))
                .map(Integer::parseInt)
                .findFirst()
                .orElse(null);
    }

    private User getUserFromRequestBody(HttpServletRequest request) throws IOException {
        // Другой вариант парсинга юзера.
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(request.getReader(), User.class);
    }

    private void deleteUser(int id) {
        User user = getUserById(id);
        DBServerInstance.getInstance().delete(user);
    }

    private void updateUser(User user, User inputUser) {
        user.setLogin(inputUser.getLogin());
        user.setPassword(inputUser.getPassword());

        DBServerInstance.getInstance().update(user);
    }

    private User getUserById(long id) {
        return DBServerInstance.getInstance().find(User.class).where().eq("id", id).findOne();
    }

    private String createUser(LinkedHashMap<String, Object> userData) {
        Long id = Long.parseLong(userData.get("id").toString());
        String name = userData.get("name").toString();
        String password = userData.get("password").toString();
        User user = new User(id, name, password);

        DBServerInstance.getInstance().save(user);

        return user.getLogin();
    }

    // Парсинг даты.
    private Date getDate(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
