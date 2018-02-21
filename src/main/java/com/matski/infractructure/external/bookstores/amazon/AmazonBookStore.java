package com.matski.infractructure.external.bookstores.amazon;

import com.matski.domain.books.Book;
import com.matski.domain.bookstore.BookOffer;
import com.matski.domain.bookstore.BookOfferCannotBeRetrievedException;
import com.matski.domain.bookstore.BookStore;
import com.matski.domain.bookstore.BookStoreDetails;
import com.matski.domain.money.Price;
import com.matski.infractructure.jsoup.JsoupClient;
import com.matski.infractructure.jsoup.JsoupRetrieveResult;
import com.matski.infractructure.jsoup.JsoupRetrieveSuccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;

public class AmazonBookStore implements BookStore {

    private static final Logger logger = LoggerFactory.getLogger(AmazonBookStore.class);

    private static final int MAX_RETRY = 4;
    private static final String BOOK_STORE_NAME = "Amazon";
    private static final String QUERY_PATTERN = "%s/s/?field-keywords=%s";
    private static final String PRICE_ELEMENT_PATTERN = "#resultItems .a-price .a-offscreen";
    private final String baseUrl;
    private final JsoupClient jsoupClient;
    private final Duration retryInterval;

    public AmazonBookStore(String baseUrl,
                           JsoupClient jsoupClient,
                           Duration retryInterval) {
        this.baseUrl = baseUrl;
        this.jsoupClient = jsoupClient;
        this.retryInterval = retryInterval;
    }

    @Override
    public BookOffer findBookOfferFor(Book book) {

        int trial = 0;
        while (trial < MAX_RETRY) {
            Optional<BookOffer> bookOffer = findBookOfferInternal(book);

            if (bookOffer.isPresent()) {
                return bookOffer.get();
            }

           logger.info("Trial {} failed: reattempting soon", trial);
            waitForInterval();
            ++trial;
        }
        throw new BookOfferCannotBeRetrievedException(BOOK_STORE_NAME);
    }

    private void waitForInterval() {
        try {
            Thread.sleep(retryInterval.toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Optional<BookOffer> findBookOfferInternal(Book book) {

        String pageQuery = String.format(QUERY_PATTERN, baseUrl, book.isbn());
        JsoupRetrieveResult pageQueryResult = jsoupClient
                .getPageAndRetrieveElement(pageQuery, PRICE_ELEMENT_PATTERN);

        if (pageQueryResult.wasSuccessful()) {
            String elementContent = ((JsoupRetrieveSuccess) pageQueryResult).elementContent();
            return Optional.of(new BookOffer(
                    book,
                    Price.parseFrom(elementContent),
                    new BookStoreDetails(
                            BOOK_STORE_NAME,
                            pageQuery
                    )
            ));
        }
        return Optional.empty();
    }
}
