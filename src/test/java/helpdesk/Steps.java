package helpdesk;

import helpdesk.scenarioConfigs.Configs;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;

import java.util.List;
import java.util.Random;

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
                                .check(jmesPath("data[*].id").ofList().saveAs("ticketList"))
//                                .check(jmesPath("data[?status=='Reopened'].id").ofList().saveAs("duplicateList"))
                                .check(jsonPath("$.data[?(@.status=='Reopened')].id").findRandom().saveAs("duplicateId"))
                        )
                );
    }

    protected static ChainBuilder openTicket = exec(session -> {
        List<Integer> ticketIdList = session.getList("ticketList");
        Integer ticketId = ticketIdList.get(new Random().nextInt(ticketIdList.size()));
        Session newSession = session.set("ticketId", ticketId);
        return newSession;
    })
            .exec(http("/tickets/__ticket_id__/")
                    .get("/tickets/#{ticketId}/")
                    .check(
                            regex("<td>(.*?)\s<strong>").findRandom().optional().saveAs("assignedFlag"),
                            regex("label\\sclass=\\\"radio-inline\\\".*value=\\'(.*?)\\'").find(0).saveAs("newStatus")
                    )
            );
}
