package com.matski.domain.bookstore;

import com.matski.domain.books.Book;

public interface BookStore {

    BookOffer findBookOfferFor(Book book);
}
