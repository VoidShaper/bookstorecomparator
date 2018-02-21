package com.matski.infractructure.unirest;

import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import io.joshworks.restclient.http.Unirest;

public class UnirestConnector {
    public HttpResponse<JsonNode> postRequestFor(String address, String body) {
        return Unirest.post(address)
                .header("Content-Type", "application/json")
                .body(body)
                .asJson();
    }
}
