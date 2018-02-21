package com.matski.infractructure.external.bookstores.apress;


import com.matski.domain.bookstore.BookOfferCannotBeRetrievedException;
import io.joshworks.restclient.http.JsonNode;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ApressPriceParserTest {

    private static final String EXAMPLE_PRICE_RESPONSE = "responses/apress/priceResponse.json";

    private final ApressPriceParser apressPriceParser = new ApressPriceParser();

    @Test
    public void parsePriceValueFromJsonObjectIsSuccessful() throws Exception{
        String priceResponse = IOUtils.toString(getClass().getClassLoader()
                .getResourceAsStream(EXAMPLE_PRICE_RESPONSE));

        JsonNode priceBody = new JsonNode(priceResponse);

        BigDecimal priceValue = apressPriceParser.getPriceValueFrom(priceBody);

        assertThat(priceValue).isEqualTo(new BigDecimal("35.69"));
    }

    @Test(expected = BookOfferCannotBeRetrievedException.class)
    public void parsePriceValueFromJsonObjectFails() throws Exception{
        JsonNode priceBody = new JsonNode("[]");

        apressPriceParser.getPriceValueFrom(priceBody);
    }
}