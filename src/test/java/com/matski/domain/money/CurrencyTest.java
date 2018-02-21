package com.matski.domain.money;

import org.junit.Test;

import static com.matski.domain.money.Currency.EURO;
import static com.matski.domain.money.Currency.GBP;
import static com.matski.domain.money.Currency.USD;
import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyTest {

    @Test
    public void getUsdCurrencyBySymbol() {
        assertThat(Currency.bySymbol("$")).isEqualTo(USD);
    }

    @Test
    public void getGbpCurrencyBySymbol() {
        assertThat(Currency.bySymbol("£")).isEqualTo(Currency.GBP);
    }

    @Test
    public void getEurCurrencyBySymbol() {
        assertThat(Currency.bySymbol("€")).isEqualTo(Currency.EURO);
    }

    @Test(expected = NoCurrencyMatchingSymbolException.class)
    public void gettingCurrencyByUnknownSymbolThrowsException() {
        assertThat(Currency.bySymbol("D")).isEqualTo(USD);
    }

    @Test
    public void printPatternsForCurrenciesAreCorrect() {
        assertThat(USD.printPattern()).isEqualTo("$%s");
        assertThat(GBP.printPattern()).isEqualTo("£%s");
        assertThat(EURO.printPattern()).isEqualTo("%s €");
    }
}