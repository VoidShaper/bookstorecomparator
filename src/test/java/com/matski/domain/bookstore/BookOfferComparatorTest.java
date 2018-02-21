package com.matski.domain.bookstore;


import com.google.common.collect.ImmutableList;
import com.matski.domain.books.Book;
import com.matski.domain.books.BookName;
import com.matski.domain.books.BookNameResolver;
import com.matski.domain.books.ISBN;
import com.matski.domain.money.Currency;
import com.matski.domain.money.Price;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookOfferComparatorTest {

    private static final BookName TEST_BOOK_NAME = new BookName("test book name");
    private static final ISBN ISBN = new ISBN("21341234");
    private static final Book BOOK = new Book(
            TEST_BOOK_NAME,
            ISBN
    );
    private static final BookOffer BOOK_OFFER_1 = new BookOffer(
            BOOK,
            new Price(new BigDecimal("213.34"), Currency.USD),
            new BookStoreDetails(
                    "testStore1",
                    "testStore.com/book/" + ISBN
            )
    );
    private static final BookOffer BOOK_OFFER_2 = new BookOffer(
            BOOK,
            new Price(new BigDecimal("223.76"), Currency.USD),
            new BookStoreDetails(
                    "testStore2",
                    "anotherStore.com/book/" + ISBN
            )
    );
    private final BookNameResolver bookNameResolver = mock(BookNameResolver.class);
    private final BookStore testStore1 = mock(BookStore.class);
    private final BookStore testStore2 = mock(BookStore.class);
    private final List<BookStore> bookStores = ImmutableList.of(
            testStore1,
            testStore2
    );

    private final BookOfferComparator bookOfferComparator = new BookOfferComparator(
            bookNameResolver,
            bookStores
    );

    @Test
    public void getsBookOfferComparisonUsingIsbnAndAllBookStores() {
        when(bookNameResolver.resolveBookFor(TEST_BOOK_NAME))
                .thenReturn(BOOK);

        when(testStore1.findBookOfferFor(BOOK))
                .thenReturn(BOOK_OFFER_1);
        when(testStore2.findBookOfferFor(BOOK))
                .thenReturn(BOOK_OFFER_2);

        BookOfferComparison bookOfferComparison = bookOfferComparator.offerComparisonFor(TEST_BOOK_NAME);

        assertThat(bookOfferComparison).isEqualTo(new BookOfferComparison(
                ImmutableList.of(BOOK_OFFER_1, BOOK_OFFER_2)
        ));
    }
}