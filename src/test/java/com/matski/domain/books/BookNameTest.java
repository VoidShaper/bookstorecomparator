package com.matski.domain.books;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BookNameTest {

    public static final String VALUE = "book name 1234";

    @Test
    public void bookNameValueIsCorrect() {
        BookName bookName = new BookName(VALUE);
        assertThat(bookName.value()).isEqualTo(VALUE);
    }

    @Test
    public void bookNameToString() {
        BookName bookName = new BookName(VALUE);
        assertThat(bookName.toString()).isEqualTo(VALUE);
    }
}