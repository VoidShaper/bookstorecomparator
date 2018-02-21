package com.matski.domain.books;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ISBNTest {

    private static final String ISBN_VALUE1 = "2039483209";
    private static final String ISBN_VALUE2 = "8734298749";

    @Test
    public void isbnToStringIsCorrect() {
        assertThat(new ISBN(ISBN_VALUE1).toString()).isEqualTo(ISBN_VALUE1);
        assertThat(new ISBN(ISBN_VALUE2).toString()).isEqualTo(ISBN_VALUE2);
    }
}