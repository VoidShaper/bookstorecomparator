package com.matski.domain.bookstore;


import com.matski.domain.books.Book;
import com.matski.domain.books.BookName;
import com.matski.domain.books.ISBN;
import com.matski.domain.money.Currency;
import com.matski.domain.money.Price;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class BookOfferTest {

    private static final Book BOOK = new Book(
            new BookName("Test Book"),
            new ISBN("23123")
    );
    private static final Price HIGHER_PRICE = new Price(new BigDecimal("22.34"), Currency.USD);
    private static final Price LOWER_PRICE = new Price(new BigDecimal("12.34"), Currency.USD);
    private static final String BOOK_STORE_NAME_1 = "bookStore name1";
    private static final String BOOKSTORE_COM = "bookstore.com";
    private static final BookStoreDetails BOOK_STORE_DETAILS = new BookStoreDetails(BOOK_STORE_NAME_1, BOOKSTORE_COM);

    @Test
    public void bookOfferHasAllTheFields() {
        BookOffer bookOffer = new BookOffer(
                BOOK,
                HIGHER_PRICE,
                BOOK_STORE_DETAILS
        );

        assertThat(bookOffer.book()).isEqualTo(BOOK);
        assertThat(bookOffer.price()).isEqualTo(HIGHER_PRICE);
        assertThat(bookOffer.bookStoreName()).isEqualTo(BOOK_STORE_NAME_1);
        assertThat(bookOffer.bookStoreAddress()).isEqualTo(BOOKSTORE_COM);
    }

    @Test
    public void bookOfferIsBetterThanAnotherOfferWhenItHasLowerPrice() {
        BookOffer bookOffer = new BookOffer(
                BOOK,
                LOWER_PRICE,
                BOOK_STORE_DETAILS
        );

        BookOffer anotherBookOffer = new BookOffer(
                BOOK,
                HIGHER_PRICE,
                BOOK_STORE_DETAILS
        );

        assertThat(bookOffer.isBetterThan(anotherBookOffer)).isTrue();
    }

    @Test
    public void bookOfferIsWorseThanAnotherOfferWhenItHasHigherPrice() {
        BookOffer bookOffer = new BookOffer(
                BOOK,
                HIGHER_PRICE,
                BOOK_STORE_DETAILS
        );

        BookOffer anotherBookOffer = new BookOffer(
                BOOK,
                LOWER_PRICE,
                BOOK_STORE_DETAILS
        );

        assertThat(bookOffer.isBetterThan(anotherBookOffer)).isFalse();
    }
}