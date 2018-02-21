package com.matski.infractructure.external.bookstores.apress;

import com.matski.domain.books.*;
import com.matski.domain.bookstore.BookOffer;
import com.matski.domain.bookstore.BookOfferCannotBeRetrievedException;
import com.matski.domain.bookstore.BookStore;
import com.matski.domain.bookstore.BookStoreDetails;
import com.matski.domain.money.Currency;
import com.matski.domain.money.Price;
import com.matski.infractructure.jsoup.JsoupClient;
import com.matski.infractructure.jsoup.JsoupRetrieveResult;
import com.matski.infractructure.jsoup.JsoupRetrieveSuccess;
import com.matski.infractructure.unirest.UnirestConnector;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;

import java.math.BigDecimal;

public class ApressBookStoreAndNameResolver implements BookStore, BookNameResolver {
    static final String BOOK_STORE_NAME = "Apress";
    private static final String OFFER_QUERY_PATTERN = "%s/us/search?query=%s";
    private static final String PRICE_LOOKUP_URL = "%s/us/product-search/ajax/prices";

    private final String baseUrl;
    private final JsoupClient jsoupClient;
    private final UnirestConnector unirestConnector;
    private final ApressPriceParser apressPriceParser;

    public ApressBookStoreAndNameResolver(String baseUrl,
                                          JsoupClient jsoupClient,
                                          UnirestConnector unirestConnector,
                                          ApressPriceParser apressPriceParser) {
        this.baseUrl = baseUrl;
        this.jsoupClient = jsoupClient;
        this.unirestConnector = unirestConnector;
        this.apressPriceParser = apressPriceParser;
    }

    @Override
    public BookOffer findBookOfferFor(Book book) {
        String productType = "book";
        HttpResponse<JsonNode> priceResponse = unirestConnector.postRequestFor(
                String.format(PRICE_LOOKUP_URL, baseUrl),
                String.format("[{\"id\": \"%s\", \"type\": \"%s\"}]", book.isbn(), productType));
        if (priceResponse.isSuccessful()) {
            BigDecimal priceValue = apressPriceParser.getPriceValueFrom(priceResponse.getBody());
            return new BookOffer(
                    book,
                    new Price(priceValue, Currency.USD),
                    new BookStoreDetails(
                            BOOK_STORE_NAME,
                            offerQueryPattern(book.isbn().toString())
                    )
            );
        }
        throw new BookOfferCannotBeRetrievedException(BOOK_STORE_NAME);
    }

    @Override
    public Book resolveBookFor(BookName bookName) {
        JsoupRetrieveResult retrieveResult = jsoupClient.getPageAndRetrieveElement(
                offerQueryPattern(bookName.value().replaceAll(" ", "+")),
                "#result-list .result-type-book p.price-container"
        );
        if (retrieveResult.wasSuccessful()) {
            JsoupRetrieveSuccess retrieveSuccess = (JsoupRetrieveSuccess) retrieveResult;
            return new Book(
                    bookName,
                    new ISBN(retrieveSuccess.elementAttribute("data-product-id"))
            );
        }
        throw new CannotResolveBookNameException(bookName);
    }

    private String offerQueryPattern(String searchCriteria) {
        return String.format(OFFER_QUERY_PATTERN,
                baseUrl,
                searchCriteria
        );
    }
}
