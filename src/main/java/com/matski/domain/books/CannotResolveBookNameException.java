package com.matski.domain.books;

public class CannotResolveBookNameException extends RuntimeException {
    public CannotResolveBookNameException(BookName bookName) {
        super(String.format("Cannot resolve book name to ISBN for %s", bookName.value()));
    }
}
