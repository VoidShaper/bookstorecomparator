package com.matski.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.matski.application.BookComparatorBootstrap;
import com.matski.application.BookComparatorConfiguration;
import com.matski.domain.books.Book;
import com.matski.domain.books.BookName;
import com.matski.domain.books.ISBN;
import com.matski.domain.bookstore.BookOffer;
import com.matski.domain.bookstore.BookOfferComparator;
import com.matski.domain.bookstore.BookOfferComparison;
import com.matski.domain.bookstore.BookStoreDetails;
import com.matski.domain.money.Currency;
import com.matski.domain.money.Price;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AmazonApressIntegrationTest {

    private static final BookName TESTED_BOOK = new BookName("Pro Deep Learning with TensorFlow");
    private static final ISBN ISBN = new ISBN("978-1-4842-3096-1");
    private static final String WIREMOCK_BASE_URL = "http://localhost:8080";
    private static final BookOffer EXPECTED_APRESS_OFFER = new BookOffer(
            new Book(
                    TESTED_BOOK,
                    ISBN
            ),
            new Price(new BigDecimal("35.69"), Currency.USD),
            new BookStoreDetails(
                    "Apress",
                    String.format("%s/us/search?query=%s", WIREMOCK_BASE_URL, ISBN)
            )
    );
    private static final BookOffer EXPECTED_AMAZON_OFFER = new BookOffer(
            new Book(
                    TESTED_BOOK,
                    ISBN
            ),
            new Price(new BigDecimal("39.29"), Currency.USD),
            new BookStoreDetails(
                    "Amazon",
                    String.format("%s/s/?field-keywords=%s", WIREMOCK_BASE_URL, ISBN)
            )
    );

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void offersAreRetrievedFromAmazonAndApressAndAreThenCompared() throws Exception {
        BookOfferComparator bookOfferComparator = bootstrapComparator();
        setupAmazonResponses();
        setupApressResponses();

        BookOfferComparison bookOfferComparison = bookOfferComparator.offerComparisonFor(TESTED_BOOK);

        assertThat(bookOfferComparison.allOffers()).containsOnly(EXPECTED_AMAZON_OFFER, EXPECTED_APRESS_OFFER);
        assertThat(bookOfferComparison.bestOffer()).contains(EXPECTED_APRESS_OFFER);
    }

    private void setupApressResponses() throws Exception {
        String apressOfferResponse = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(
                "responses/apress/example-apress-content.html"
        ));
        String apressPriceResponse = IOUtils.toString(
                getClass().getClassLoader().getResourceAsStream(
                        "responses/apress/priceResponse.json"
                )
        );
        wireMockRule.stubFor(get(urlEqualTo("/us/search?query=" + TESTED_BOOK.value().replaceAll(" ", "+")))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody(apressOfferResponse)));
        wireMockRule.stubFor(post(urlEqualTo("/us/product-search/ajax/prices"))
                .withRequestBody(
                        equalToJson(String.format("[{\"id\": \"%s\", \"type\": \"book\"}]", ISBN))
                )
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(apressPriceResponse)));
    }

    private void setupAmazonResponses() throws Exception {
        String amazonResponse = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(
                "responses/amazon/example-amazon-content.html"
        ));
        wireMockRule.stubFor(get(urlEqualTo("/s/?field-keywords=" + ISBN))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody(amazonResponse)));
    }

    private BookOfferComparator bootstrapComparator() {
        BookComparatorConfiguration configuration = new BookComparatorConfiguration(
                WIREMOCK_BASE_URL,
                WIREMOCK_BASE_URL
        );

        return new BookComparatorBootstrap()
                .bootstrapBookOfferComparator(configuration);
    }
}
