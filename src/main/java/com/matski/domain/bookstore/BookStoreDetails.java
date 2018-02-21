package com.matski.domain.bookstore;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class BookStoreDetails {
    private final String name;
    private final String address;

    public BookStoreDetails(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String name() {
        return name;
    }

    public String address() {
        return address;
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
