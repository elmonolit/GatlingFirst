package helpdesk;

import helpdesk.scenarioConfigs.Configs;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;
import org.apache.commons.lang3.RandomStringUtils;
import io.gatling.core.Predef.*;
import ru.tinkoff.load.jdbc.Predef;
import ru.tinkoff.load.jdbc.actions.actions;
import ru.tinkoff.load.jdbc.actions.actions.Columns;
import scala.collection.Seq;
import static ru.tinkoff.load.javaapi.JdbcDsl.*;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class UserCreation extends Steps {
    static ChainBuilder adminLogin = group("Login").on(login(Configs.adminLogin, adminPass));

    static ChainBuilder createUser = group("User Creation").on(exec(http("/system_settings/").get("/system_settings/"))
            .exec(http("/admin/auth/user/").get("/admin/auth/user/"))
            .exec(http("/admin/auth/user/add/").get("/admin/auth/user/add/"))
            .exec(session -> {
                Session newSession = session.set("username", "guser_" +  RandomStringUtils.randomAlphabetic(5))
                        .set("password", RandomStringUtils.randomAlphabetic(10));
                return newSession;
            })
            .exec(http("POST /admin/auth/user/add/")
                    .post("/admin/auth/user/add/")
                    .headers(headers_3)
                    .formParam("csrfmiddlewaretoken", "#{csrf}")
                    .formParam("username", "#{username}")
                    .formParam("password1", "#{password}")
                    .formParam("password2", "#{password}")
                    .formParam("_save", "Save")
                    .check(regex("\\/admin\\/auth\\/user\\/(\\d+?)\\/change").find(0).saveAs("userId"))
            )
            .exec(http("POST /admin/auth/user/_userId_/change/\"")
                    .post("/admin/auth/user/#{userId}/change/")
                    .headers(headers_3)
                    .formParam("csrfmiddlewaretoken", "#{csrf}")
                    .formParam("username", "#{username}")
                    .formParam("first_name", RandomStringUtils.randomAlphabetic(5))
                    .formParam("last_name",   RandomStringUtils.randomAlphabetic(5))
                    .formParam("email", RandomStringUtils.randomAlphabetic(5) + "@gmail.com")
                    .formParam("is_active", "on")
                    .formParam("is_staff", "on")
                    .formParam("last_login_0", "")
                    .formParam("last_login_1", "")
                    .formParam("date_joined_0", "2023-08-18")
                    .formParam("date_joined_1", "21:42:44")
                    .formParam("initial-date_joined_0", "2023-08-18")
                    .formParam("initial-date_joined_1", "21:42:44")
                    .formParam("_save", "Save")
                    .check(substring("was changed successfully"))
            )
            .doIf(session -> !session.isFailed()).then(
                    exec(session -> {
                        Utils.writeUsersToCSV(session);
                        return session;
                    })
                    .exec(
                            jdbc("al")
                            .insertInto("helpdesk_credentials", "username", "password")
                            .values(Map.of(
                                    "username", "#{username}", "password", "#{password}"
                            ))
                    )
            )
    );
}
