package helpdesk;

import io.gatling.javaapi.core.ChainBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class ChangeTicketStatus extends Steps{

    static ChainBuilder login = group("Login").on(feed(csv("users.csv").circular()).exec(login("#{username}", "#{password}")));

    static ChainBuilder changeTicketStatus = group("Change Status")
            .on(
              exec(openTicket)
              .exec(
                      http("/tickets/__ticketId__/update/").post("/tickets/#{ticketId}/update/")
                              .formParam("comment", RandomStringUtils.randomAlphabetic(20))
                              .formParam("new_status", "#{newStatus}")
                              .formParam("public", 1)
                              .formParam("csrfmiddlewaretoken", "#{csrf}")
                              .check(substring("Changed Status from "))
                      )
            );
}
