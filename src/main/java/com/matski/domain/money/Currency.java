package com.matski.domain.money;

import java.util.Arrays;

public enum Currency {
    USD("$", "$%s"),
    GBP("£", "£%s"),
    EURO("€", "%s €");

    private final String symbol;
    private final String printPattern;

    Currency(String symbol, String printPattern) {
        this.symbol = symbol;
        this.printPattern = printPattern;
    }

    public static Currency bySymbol(String symbol) {
        Currency[] values = values();
        return Arrays.stream(values)
                .filter(currency -> symbol.equals(currency.symbol))
                .findFirst()
                .orElseThrow(() -> new NoCurrencyMatchingSymbolException(symbol));
    }

    public String printPattern() {
        return printPattern;
    }
}
