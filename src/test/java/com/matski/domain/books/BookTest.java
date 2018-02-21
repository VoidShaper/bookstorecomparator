package com.matski.domain.books;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BookTest {

    private static final ISBN ISBN = new ISBN("2309832109");
    private static final BookName NAME = new BookName("test book 234");

    @Test
    public void returnsCorrectFieldsValues() {
        Book book = new Book(NAME, ISBN);
        assertThat(book.name()).isEqualTo(NAME);
        assertThat(book.isbn()).isEqualTo(ISBN);
    }
}