package helpdesk;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;

public class Filtration extends Steps{

    static ChainBuilder login = group("Login").on(feed(csv("users.csv").circular()).exec(login("#{username}", "#{password}")));

    static ChainBuilder filtration = group("Filter").on(exec(session -> {
        System.out.println("filter");return session;
    }));
}
