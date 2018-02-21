package com.matski.domain.books;

public interface BookNameResolver {
    Book resolveBookFor(BookName bookName);
}
