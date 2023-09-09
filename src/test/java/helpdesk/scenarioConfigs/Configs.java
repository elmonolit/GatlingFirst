package helpdesk.scenarioConfigs;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.http.HttpDsl.Proxy;
import static io.gatling.javaapi.http.HttpDsl.http;

public  class Configs {


    protected static Map<CharSequence, String> headers_0 = Map.of("Upgrade-Insecure-Requests", "1");

    protected static String adminLogin = "admin";
    protected static String adminPass = "admindev";

    protected static Map<CharSequence, String> headers_2 = Map.ofEntries(
            Map.entry("Origin", "http://178.208.66.193:23232"),
            Map.entry("Upgrade-Insecure-Requests", "1")
    );

    protected static Map<CharSequence, String> headers_3 = Map.ofEntries(
            Map.entry("Accept", "application/json, text/javascript, */*; q=0.01"),
            Map.entry("X-Requested-With", "XMLHttpRequest")
    );

    protected static Map<CharSequence,String> headers_4 = Map.ofEntries(
            Map.entry("Content-Type", "multipart/form-data")
    );

}
