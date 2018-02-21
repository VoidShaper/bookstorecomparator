package com.matski.infractructure.external.bookstores.apress;


import com.matski.domain.books.Book;
import com.matski.domain.books.BookName;
import com.matski.domain.books.CannotResolveBookNameException;
import com.matski.domain.books.ISBN;
import com.matski.domain.bookstore.BookOffer;
import com.matski.domain.bookstore.BookOfferCannotBeRetrievedException;
import com.matski.domain.bookstore.BookStoreDetails;
import com.matski.domain.money.Currency;
import com.matski.domain.money.Price;
import com.matski.infractructure.jsoup.JsoupClient;
import com.matski.infractructure.jsoup.JsoupRetrieveSuccess;
import com.matski.infractructure.unirest.UnirestConnector;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApressBookStoreAndNameResolverTest {

    private static final BookName BOOK_NAME = new BookName("Java for Cattle Shavers");
    private static final ISBN ISBN = new ISBN("1231239898");
    private static final Book JAVA_FOR_CATTLE_SHAVERS = new Book(
            BOOK_NAME,
            ISBN
    );
    private static final String EXPECTED_REQUEST = String.format("[{\"id\": \"%s\", \"type\": \"book\"}]",
            JAVA_FOR_CATTLE_SHAVERS.isbn());
    private static final String PRICE_LOOKUP_URL = "https://www.apress.com/us/product-search/ajax/prices";
    private static final String OFFER_QUERY_PATTERN = "https://www.apress.com/us/search?query=%s";
    private static final BigDecimal PRICE_VALUE = new BigDecimal(567.84);
    private static final boolean SUCCESSFUL = true;
    private static final boolean NOT_SUCCESSFUL = false;
    private final JsoupClient jsoupClient = mock(JsoupClient.class);
    private final UnirestConnector unirestConnector = mock(UnirestConnector.class);
    private final ApressPriceParser apressPriceParser = mock(ApressPriceParser.class);
    private final ApressBookStoreAndNameResolver apressBookStoreAndNameResolver
            = new ApressBookStoreAndNameResolver("https://www.apress.com", jsoupClient, unirestConnector, apressPriceParser);

    @Test
    public void retrieveBookOfferWhenCallAndParsingAreCorrect() {
        JsonNode responseBody = whenRequestReturnsResponseThatIs(SUCCESSFUL);

        when(apressPriceParser.getPriceValueFrom(responseBody))
                .thenReturn(PRICE_VALUE);

        BookOffer bookOffer = apressBookStoreAndNameResolver.findBookOfferFor(JAVA_FOR_CATTLE_SHAVERS);

        assertThat(bookOffer).isEqualTo(new BookOffer(
                JAVA_FOR_CATTLE_SHAVERS,
                new Price(PRICE_VALUE, Currency.USD),
                new BookStoreDetails(
                        "Apress",
                        String.format(OFFER_QUERY_PATTERN, JAVA_FOR_CATTLE_SHAVERS.isbn())
                )
        ));
    }

    @Test(expected = BookOfferCannotBeRetrievedException.class)
    public void throwExceptionWhenResponseISNotSuccessful() {
        whenRequestReturnsResponseThatIs(NOT_SUCCESSFUL);

        apressBookStoreAndNameResolver.findBookOfferFor(JAVA_FOR_CATTLE_SHAVERS);
    }

    @Test
    public void resolveBookByName() {
        whenApressLookupResponseIs(SUCCESSFUL);

        Book book = apressBookStoreAndNameResolver.resolveBookFor(BOOK_NAME);

        assertThat(book).isEqualTo(JAVA_FOR_CATTLE_SHAVERS);
    }

    @Test(expected = CannotResolveBookNameException.class)
    public void resolveBookByNameFailsWhenResponseIsNotSuccessful() {
        whenApressLookupResponseIs(NOT_SUCCESSFUL);

        Book book = apressBookStoreAndNameResolver.resolveBookFor(BOOK_NAME);

        assertThat(book).isEqualTo(JAVA_FOR_CATTLE_SHAVERS);
    }

    private void whenApressLookupResponseIs(boolean success) {
        JsoupRetrieveSuccess retrieveResult = mock(JsoupRetrieveSuccess.class);
        when(jsoupClient.getPageAndRetrieveElement(
                String.format(OFFER_QUERY_PATTERN, BOOK_NAME.value().replaceAll(" ", "+")),
                "#result-list .result-type-book p.price-container"
        )).thenReturn(retrieveResult);
        when(retrieveResult.elementAttribute("data-product-id"))
                .thenReturn(ISBN.toString());
        when(retrieveResult.wasSuccessful()).thenReturn(success);
    }

    private JsonNode whenRequestReturnsResponseThatIs(boolean success) {
        HttpResponse httpResponse = mock(HttpResponse.class);
        JsonNode responseBody = mock(JsonNode.class);
        when(httpResponse.isSuccessful()).thenReturn(success);
        when(httpResponse.getBody()).thenReturn(responseBody);

        when(unirestConnector.postRequestFor(PRICE_LOOKUP_URL, EXPECTED_REQUEST))
                .thenReturn(httpResponse);
        return responseBody;
    }
}