package com.matski.domain.bookstore;

import com.matski.domain.books.Book;
import com.matski.domain.books.BookName;
import com.matski.domain.books.ISBN;
import com.matski.domain.money.Currency;
import com.matski.domain.money.Price;
import org.junit.Test;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class BookOfferComparisonTest {

    public static final Book COOKING_WITH_AJAX = new Book(
            new BookName("Cooking with AJAX"),
            new ISBN("435")
    );
    private final BookOffer worstOffer = new BookOffer(
            COOKING_WITH_AJAX,
            new Price(new BigDecimal("47.34"), Currency.USD),
            new BookStoreDetails(
                    "bookstore1",
                    "bookstore1.com/book"
            )
    );
    private final BookOffer bestOffer = new BookOffer(
            COOKING_WITH_AJAX,
            new Price(new BigDecimal("37.12"), Currency.USD),
            new BookStoreDetails(
                    "bookstore1",
                    "bookstore1.com/book"
            )
    );
    private final BookOffer middleOffer = new BookOffer(
            COOKING_WITH_AJAX,
            new Price(new BigDecimal("37.34"), Currency.USD),
            new BookStoreDetails(
                    "bookstore1",
                    "bookstore1.com/book"
            )
    );

    @Test
    public void noOfferReturnedWhenTheListIsEmpty() {
        assertThat(new BookOfferComparison(emptyList()).bestOffer())
                .isEmpty();

    }

    @Test
    public void chooseBestOfferThatIsBetterThanAllOthersWhenItIsTheFirstOne() {
        assertThat(new BookOfferComparison(asList(bestOffer, worstOffer, middleOffer))
                .bestOffer())
                .contains(bestOffer);
    }

    @Test
    public void chooseBestOfferThatIsBetterThanAllOthersWhenItIsTheMiddleOne() {
        assertThat(new BookOfferComparison(asList(worstOffer, bestOffer, middleOffer))
                .bestOffer())
                .contains(bestOffer);
    }

    @Test
    public void chooseBestOfferThatIsBetterThanAllOthersWhenItIsTheLastOne() {
        assertThat(new BookOfferComparison(asList(worstOffer, middleOffer, bestOffer))
                .bestOffer())
                .contains(bestOffer);
    }

    @Test
    public void allOffersReturnsAllFoundOffers() {
        assertThat(new BookOfferComparison(asList(worstOffer, middleOffer, bestOffer))
                .allOffers())
                .containsOnly(
                        worstOffer,
                        middleOffer,
                        bestOffer
                );
    }
}