import com.berezanskiy.servletExample.Main;
import com.berezanskiy.servletExample.db.DBServerInstance;
import com.berezanskiy.servletExample.models.User;
import io.ebean.Database;
import io.ebean.Transaction;
import io.ebean.annotation.Transactional;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class AppTest {
    private static Javalin app;
    private static String baseUrl;
    private static Database db;
    private static Transaction transaction;
    private static final String USERS_API_PATH = "/v1.0/users/";

    @BeforeAll
    public static void beforeAll() {
        app = Main.getApp();
        app.start(0);
        int port = app.port();
        db = DBServerInstance.getInstance();
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @BeforeEach
    void beforeEach() {
        transaction = db.beginTransaction();
    }

    @AfterEach
    void afterEach() {
       transaction.rollback();
       transaction.end();
    }

    @Test
    void testRoot() {
        HttpResponse<String> response = Unirest.get(baseUrl).asString();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testUsers() {
        HttpResponse<String> response = Unirest
                .get(baseUrl + USERS_API_PATH)
                .asString();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testCreateUser_negative() {
        HttpResponse<String> response = Unirest.post(baseUrl + USERS_API_PATH).asString();

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testCreateUser() {
        String login = "testLogin";
        String password = "testPassword";

        HttpResponse<String> response = Unirest
                .post(baseUrl + USERS_API_PATH)
                .field("login", login)
                .field("password", password)
                .asString();

        assertThat(response.getStatus()).isEqualTo(200);

        User createdUser = db
                .find(User.class)
                .where()
                .eq("login", login)
                .eq("password", password)
                .findOne();

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getLogin()).isEqualTo(login);
        assertThat(createdUser.getPassword()).isEqualTo(password);
    }
}
