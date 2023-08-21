package helpdesk;

import helpdesk.scenarioConfigs.Configs;
import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Steps extends Configs {

    protected static ChainBuilder login(String username, String password){
        return exec(http("/").get("/").headers(headers_0))
                .exec(http("/login/?next=/").get("/login/?next=/").headers(headers_0)
                        .check(regex("csrfmiddlewaretoken\"\svalue=\"(.*?)\"")
                                .find(0)
                                .saveAs("csrf")
                        )
                )
                .exec(http("/login/").post("/login/").headers(headers_2)
                        .formParam("username", username)
                        .formParam("password", password)
                        .formParam("next", "/")
                        .formParam("csrfmiddlewaretoken", "#{csrf}")
                        .check(regex("datatables_ticket_list\\/(.*?)\\\"")
                                .find(0)
                                .saveAs("dtlUrl")
                        ).check(regex("csrfmiddlewaretoken\"\svalue=\"(.*?)\"")
                                .find(0)
                                .saveAs("csrf")
                        )
                        .check(substring("Logout"))
                        .resources(http("/datatables_ticket_list/__URL__")
                                .get("/datatables_ticket_list/#{dtlUrl}")
                                .headers(headers_3)
                        )
                );
    }
}
