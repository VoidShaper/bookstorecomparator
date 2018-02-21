package com.matski.infractructure.external.bookstores.apress;

import com.matski.domain.bookstore.BookOfferCannotBeRetrievedException;
import io.joshworks.restclient.http.JsonNode;

import java.math.BigDecimal;

public class ApressPriceParser {
    public BigDecimal getPriceValueFrom(JsonNode priceResponse) {
        try {
           return priceResponse
                    .getArray()
                    .getJSONObject(0)
                    .getJSONObject("price")
                    .getBigDecimal("bestPrice");
        } catch (Exception e) {
           throw new BookOfferCannotBeRetrievedException(ApressBookStoreAndNameResolver.BOOK_STORE_NAME);
        }
    }
}
