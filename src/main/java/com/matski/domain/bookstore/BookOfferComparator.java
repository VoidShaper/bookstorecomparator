package com.matski.domain.bookstore;

import com.google.common.collect.ImmutableList;
import com.matski.domain.books.Book;
import com.matski.domain.books.BookName;
import com.matski.domain.books.BookNameResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class BookOfferComparator {
    private final BookNameResolver bookNameResolver;
    private final ImmutableList<BookStore> bookStores;

    private static final Logger logger = LoggerFactory.getLogger(BookOfferComparator.class);

    public BookOfferComparator(BookNameResolver bookNameResolver,
                               List<BookStore> bookStores) {
        this.bookNameResolver = bookNameResolver;
        this.bookStores = ImmutableList.copyOf(bookStores);
    }

    public BookOfferComparison offerComparisonFor(BookName bookName) {
        final Book book = bookNameResolver.resolveBookFor(bookName);
        List<BookOffer> bookOffers = bookStores.stream()
                .map(bookStore -> bookStore.findBookOfferFor(book))
                .collect(Collectors.toList());

        logger.debug("Found offers: {}", bookOffers);

        return new BookOfferComparison(bookOffers);
    }
}
