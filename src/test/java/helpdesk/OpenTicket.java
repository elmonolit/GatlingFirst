package helpdesk;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;

import java.util.List;
import java.util.Random;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class OpenTicket extends Steps{

    static ChainBuilder login = group("Login").on(feed(csv("users.csv").circular()).exec(login("#{username}", "#{password}")));


    static ChainBuilder takeTicket = doIf(session -> session.getString("assignedFlag").equals("Unassigned"))
            .then(
                    exec(http("/tickets/__ticketId__/?take")
                                    .get("/tickets/#{ticketId}/?take")
//                                .check(substring("Unassigned").notExists())
                    )
            );

    static ChainBuilder openAndTakeTicket = group("OpenAndTakeTicket").on(
            exec(openTicket, takeTicket)
    );
}
