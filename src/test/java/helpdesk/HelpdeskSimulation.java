package helpdesk;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import ru.tinkoff.load.javaapi.protocol.JdbcProtocolBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static ru.tinkoff.load.javaapi.JdbcDsl.DB;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class HelpdeskSimulation extends Simulation {
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;


    static {
        try {
            Properties props = new Properties();
            InputStream propsStream = HelpdeskSimulation.class.getClassLoader().getResourceAsStream("simulation.properties");
            props.load(propsStream);
            dbUrl = props.getProperty("dbUrl");
            dbUser = props.getProperty("dbUser");
            dbPassword = props.getProperty("dbPassword");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static  HttpProtocolBuilder httpProtocol = http.proxy(Proxy("127.0.0.1", 8888))
            .baseUrl("http://178.208.66.193:23232")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
            .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/114.0");

    private static JdbcProtocolBuilder jdbcProtocol = DB()
            .url("jdbc:postgresql://178.208.66.193:23222/random_things")
            .username("emt")
            .password("RandomPass")
            .maximumPoolSize(23)
            .protocolBuilder();
//    private ScenarioBuilder scn = AdminLogin.adminLogin();
    private ScenarioBuilder scn = scenario("Create User").repeat(20).on(exec(UserCreation.adminLogin,UserCreation.createUser));
//    private ScenarioBuilder scn = scenario("Create Ticket").repeat(10).on(exec(TicketCreation.userLogin, TicketCreation.createTicket));
//    private ScenarioBuilder scn = scenario("Take Ticket").repeat(1).on(exec(OpenTicket.login, OpenTicket.openAndTakeTicket));
//    private ScenarioBuilder scn = scenario("Change Ticket Status").repeat(1).on(exec(ChangeTicketStatus.login, ChangeTicketStatus.changeTicketStatus));
//    private ScenarioBuilder scn = scenario("Delete Ticket").repeat(1).on(exec(DeleteTicket.login, DeleteTicket.deleteTicket));
//    private ScenarioBuilder scn = scenario("Filter Tickets").repeat(1).on(exec(Filtration.login, Filtration.filtration));

  {
	  setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol, jdbcProtocol);
  }
}
