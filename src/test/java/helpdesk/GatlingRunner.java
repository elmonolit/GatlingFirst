package helpdesk;

import io.gatling.app.Gatling;
import io.gatling.app.SimulationClass;
import io.gatling.core.config.GatlingPropertiesBuilder;

public class GatlingRunner {

    public static void main(String[] args) {

// this is where you specify the class you want to run

        String simClass = HelpdeskSimulation.class.getName();

// get the properties for the class

        GatlingPropertiesBuilder props = new GatlingPropertiesBuilder();

        props.simulationClass(simClass);

        Gatling.fromMap(props.build());

    }

}