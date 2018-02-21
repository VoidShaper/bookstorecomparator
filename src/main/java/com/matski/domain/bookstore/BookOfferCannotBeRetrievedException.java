package com.matski.domain.bookstore;

public class BookOfferCannotBeRetrievedException extends RuntimeException {
    public BookOfferCannotBeRetrievedException(String bookStoreName) {
        super(String.format("Book offer could not be retrieved from book store: %s", bookStoreName));
    }
}
