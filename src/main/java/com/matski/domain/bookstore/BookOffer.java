package com.matski.domain.bookstore;

import com.matski.domain.books.Book;
import com.matski.domain.money.Price;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class BookOffer {
    private final Book book;
    private final Price price;
    private final BookStoreDetails bookStoreDetails;

    public BookOffer(Book book, Price price, BookStoreDetails bookStoreDetails) {
        this.book = book;
        this.price = price;
        this.bookStoreDetails = bookStoreDetails;
    }

    public boolean isBetterThan(BookOffer anotherOffer) {
        return price.isLowerOrEqualTo(anotherOffer.price);
    }

    public Book book() {
        return book;
    }

    public Price price() {
        return price;
    }

    public String bookStoreName() {
        return bookStoreDetails.name();
    }

    public String bookStoreAddress() {
        return bookStoreDetails.address();
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
