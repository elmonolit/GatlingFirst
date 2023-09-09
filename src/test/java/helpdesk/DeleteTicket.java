package helpdesk;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;

import java.util.List;
import java.util.Random;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class DeleteTicket extends Steps{

    static ChainBuilder login = group("Login").on(feed(csv("users.csv").circular()).exec(login("#{username}", "#{password}")));

    static ChainBuilder deleteTicket = group("Delete Ticket").on(
            exec(
                    http("/tickets/update/").post("/tickets/update/")
                            .formParam("csrfmiddlewaretoken", "#{csrf}")
                            .formParam("ticketTable_length", "25")
                            .formParam("ticket_id","#{duplicateId}")
                            .formParam("action", "delete")
            )
    );
}