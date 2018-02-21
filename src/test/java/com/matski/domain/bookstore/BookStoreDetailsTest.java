package com.matski.domain.bookstore;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BookStoreDetailsTest {

    private static final String NAME = "Amazon";
    private static final String ADDRESS = "amazon.com/book/123";

    @Test
    public void bookStoreDetailsNameAndAddressAreCorrect() {
        BookStoreDetails bookStoreDetails = new BookStoreDetails(NAME, ADDRESS);
        assertThat(bookStoreDetails.name()).isEqualTo(NAME);
        assertThat(bookStoreDetails.address()).isEqualTo(ADDRESS);
    }
}