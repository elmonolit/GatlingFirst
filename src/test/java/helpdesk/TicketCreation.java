package helpdesk;

import helpdesk.scenarioConfigs.Configs;
import io.gatling.javaapi.core.ChainBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class TicketCreation extends Steps {

    static ChainBuilder userLogin = group("Login").on(feed(csv("users.csv").circular()).exec(login("#{username}", "#{password}")));


    static ChainBuilder createTicket = group("Create Ticket").on(exec(
                    http("/tickets/submit/")
                            .get("/tickets/submit/")
            )
            .exec(
                    http("POST /tickets/submit/")
                            .post("/tickets/submit/")
                            .headers(headers_4)
                            .formParam("csrfmiddlewaretoken", "#{csrf}")
                            .formParam("queue", new Random().nextInt(1,2))
                            .formParam("title", RandomStringUtils.randomAlphabetic(10))
                            .formParam("body", RandomStringUtils.randomAlphabetic(50))
                            .formParam("priority", new Random().nextInt(1,5))
//                            .formParam("due_date", )
                            .check(substring(" [Open]"))
        ));

}
