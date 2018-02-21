package com.matski.domain.money;

public class NoCurrencyMatchingSymbolException extends RuntimeException {
    public NoCurrencyMatchingSymbolException(String symbol) {
        super(String.format("There is no currency matching symbol %s", symbol));
    }
}
