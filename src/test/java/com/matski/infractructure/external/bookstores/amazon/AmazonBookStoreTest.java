package com.matski.infractructure.external.bookstores.amazon;

import com.matski.domain.books.Book;
import com.matski.domain.books.BookName;
import com.matski.domain.books.ISBN;
import com.matski.domain.bookstore.BookOffer;
import com.matski.domain.bookstore.BookOfferCannotBeRetrievedException;
import com.matski.domain.bookstore.BookStoreDetails;
import com.matski.domain.money.Currency;
import com.matski.domain.money.Price;
import com.matski.infractructure.jsoup.JsoupClient;
import com.matski.infractructure.jsoup.JsoupRetrieveFailed;
import com.matski.infractructure.jsoup.JsoupRetrieveSuccess;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AmazonBookStoreTest {

    private static final ISBN ISBN = new ISBN("902834");
    private static final Book BOOK = new Book(
            new BookName("The Guide To Infinite Fork"),
            ISBN
    );
    private static final String QUERY_PATTERN = "https://www.amazon.com/s/?field-keywords=%s";
    private static final String PRICE_ELEMENT_PATTERN = "#resultItems .a-price .a-offscreen";
    private static final String PRICE_VALUE = "12.98";
    private final JsoupClient jsoupClient = mock(JsoupClient.class);
    private final Duration retryInterval = Duration.ofMillis(5);
    private static final String PAGE_QUERY = String.format(QUERY_PATTERN, ISBN);

    private final AmazonBookStore amazonBookStore = new AmazonBookStore(
            "https://www.amazon.com",
            jsoupClient,
            retryInterval
    );
    private final JsoupRetrieveSuccess priceRetrieveSuccess = mock(JsoupRetrieveSuccess.class);
    private final JsoupRetrieveFailed priceRetrieveFailed = mock(JsoupRetrieveFailed.class);

    @Before
    public void setUp() throws Exception {
        when(priceRetrieveSuccess.elementContent())
                .thenReturn("$" + PRICE_VALUE);
        when(priceRetrieveSuccess.wasSuccessful())
                .thenReturn(true);
        when(priceRetrieveFailed.wasSuccessful())
                .thenReturn(false);
    }

    @Test
    public void amazonOfferIsRetrievedInTheFirstTrial() {
        when(jsoupClient.getPageAndRetrieveElement(PAGE_QUERY, PRICE_ELEMENT_PATTERN))
                .thenReturn(priceRetrieveSuccess);
        BookOffer bookOffer = amazonBookStore.findBookOfferFor(BOOK);

        assertThat(bookOffer).isEqualTo(new BookOffer(
                BOOK,
                new Price(new BigDecimal(PRICE_VALUE), Currency.USD),
                new BookStoreDetails(
                        "Amazon",
                        PAGE_QUERY
                )
        ));

        verify(jsoupClient, times(1))
                .getPageAndRetrieveElement(PAGE_QUERY, PRICE_ELEMENT_PATTERN);
    }

    @Test
    public void amazonOfferIsRetrievedInTheLastTrial() {
        when(jsoupClient.getPageAndRetrieveElement(PAGE_QUERY, PRICE_ELEMENT_PATTERN))
                .thenReturn(priceRetrieveFailed,
                        priceRetrieveFailed,
                        priceRetrieveFailed,
                        priceRetrieveSuccess);
        BookOffer bookOffer = amazonBookStore.findBookOfferFor(BOOK);

        assertThat(bookOffer).isEqualTo(new BookOffer(
                BOOK,
                new Price(new BigDecimal(PRICE_VALUE), Currency.USD),
                new BookStoreDetails(
                        "Amazon",
                        PAGE_QUERY
                )
        ));

        verify(jsoupClient, times(4))
                .getPageAndRetrieveElement(PAGE_QUERY, PRICE_ELEMENT_PATTERN);

    }

    @Test(expected = BookOfferCannotBeRetrievedException.class)
    public void amazonOfferRetrievalFailedAllRetries() {
        when(jsoupClient.getPageAndRetrieveElement(PAGE_QUERY, PRICE_ELEMENT_PATTERN))
                .thenReturn(priceRetrieveFailed,
                        priceRetrieveFailed,
                        priceRetrieveFailed,
                        priceRetrieveFailed);
        amazonBookStore.findBookOfferFor(BOOK);
    }
}