package com.matski.domain.money;

public class PricesIncomparableDifferentCurrenciesException extends RuntimeException {
    public PricesIncomparableDifferentCurrenciesException() {
        super("Cannot compare prices of different currencies");
    }
}
