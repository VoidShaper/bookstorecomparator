package com.matski.infractructure.external.bookstores.apress;

import com.matski.domain.bookstore.BookOfferCannotBeRetrievedException;
import io.joshworks.restclient.http.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class ApressPriceParser {
    public static final Logger logger = LoggerFactory.getLogger(ApressPriceParser.class);
    public BigDecimal getPriceValueFrom(JsonNode priceResponse) {
        try {
           return priceResponse
                    .getArray()
                    .getJSONObject(0)
                    .getJSONObject("price")
                    .getBigDecimal("bestPrice");
        } catch (Exception e) {
            logger.warn("Exception while parsing price from apress", e);
           throw new BookOfferCannotBeRetrievedException(ApressBookStoreAndNameResolver.BOOK_STORE_NAME);
        }
    }
}
