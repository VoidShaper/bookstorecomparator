package com.matski.domain.money;

import java.math.BigDecimal;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class Price {
    private final BigDecimal value;
    private final Currency currency;

    public Price(BigDecimal value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    public static Price parseFrom(String price) {
        String value = price.replaceAll("[\\$£€]", "").trim();
        String symbol = price.replaceAll("[.0-9]", "").trim();
        return new Price(new BigDecimal(value), Currency.bySymbol(symbol));
    }

    public boolean isLowerOrEqualTo(Price anotherPrice) {
        if(anotherPrice.currency != this.currency) {
            throw new PricesIncomparableDifferentCurrenciesException();
        }
        return value.compareTo(anotherPrice.value) <= 0;
    }

    public String asFormattedString() {
        return String.format(currency.printPattern(), value.toString());
    }

    public BigDecimal value() {
        return value;
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
