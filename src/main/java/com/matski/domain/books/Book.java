package com.matski.domain.books;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class Book {
    private final BookName name;
    private final ISBN isbn;

    public Book(BookName name, ISBN isbn) {
        this.name = name;
        this.isbn = isbn;
    }

    public BookName name() {
        return name;
    }

    public ISBN isbn() {
        return isbn;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);

    }
}
