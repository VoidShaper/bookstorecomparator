package com.matski.infractructure.unirest;

import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import io.joshworks.restclient.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnirestConnector {
    public HttpResponse<JsonNode> postRequestFor(String address, String body) {
            return Unirest.post(address)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asJson();
    }
}
