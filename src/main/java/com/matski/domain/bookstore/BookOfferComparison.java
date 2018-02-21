package com.matski.domain.bookstore;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Optional;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class BookOfferComparison {
    private final ImmutableList<BookOffer> allOffers;

    public BookOfferComparison(Collection<BookOffer> allOffers) {
        this.allOffers = ImmutableList.copyOf(allOffers);
    }

    public Optional<BookOffer> bestOffer() {
        return allOffers.stream()
                .max((offer1, offer2) -> offer1.isBetterThan(offer2) ? 1 : -1);
    }

    public Collection<BookOffer> allOffers() {
        return allOffers;
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
