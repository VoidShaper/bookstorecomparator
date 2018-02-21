package com.matski.domain.money;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceTest {

    @Test
    public void priceIsParsedFromUsdCurrencyString() {
        String priceValue = "823.98";
        Price price = Price.parseFrom("$" + priceValue);

        assertThat(price).isEqualTo(new Price(
                new BigDecimal(priceValue),
                Currency.USD)
        );
    }

    @Test
    public void priceIsParsedFromEurCurrencyString() {
        String priceValue = "543.57";
        Price price = Price.parseFrom(priceValue + " €");

        assertThat(price).isEqualTo(new Price(
                new BigDecimal(priceValue),
                Currency.EURO)
        );
    }

    @Test
    public void priceIsParsedFromGbpCurrencyString() {
        String priceValue = "43.47";
        Price price = Price.parseFrom("£" + priceValue);

        assertThat(price).isEqualTo(new Price(
                new BigDecimal(priceValue),
                Currency.GBP)
        );
    }

    @Test
    public void priceIsFormattedToStringForUsdCurrency() {
        String priceValue = "823.98";
        Price price = Price.parseFrom("$" + priceValue);
        assertThat(price.asFormattedString()).isEqualTo("$" + priceValue);
    }

    @Test
    public void priceIsFormattedToStringForEurCurrency() {
        String priceValue = "543.57";
        Price price = Price.parseFrom(priceValue + " €");
        assertThat(price.asFormattedString()).isEqualTo(priceValue + " €");
    }

    @Test
    public void priceIsFormattedToStringForGbpCurrency() {
        String priceValue = "43.47";
        Price price = Price.parseFrom("£" + priceValue);
        assertThat(price.asFormattedString()).isEqualTo("£" + priceValue);
    }

    @Test
    public void priceIsLowerOrEqualThanAnotherPriceIfValueIsLower() {
        Price price1 = Price.parseFrom("$33.45");
        Price price2 = Price.parseFrom("$34.45");
        assertThat(price1.isLowerOrEqualTo(price2)).isTrue();
    }

    @Test
    public void priceIsLowerOrEqualThanAnotherPriceIfValueIsEqual() {
        Price price1 = Price.parseFrom("$33.45");
        Price price2 = Price.parseFrom("$33.45");
        assertThat(price1.isLowerOrEqualTo(price2)).isTrue();
    }

    @Test
    public void priceIsNotLowerOrEqualThanAnotherPriceIfValueIsHigher() {
        Price price1 = Price.parseFrom("$33.45");
        Price price2 = Price.parseFrom("$32.45");
        assertThat(price1.isLowerOrEqualTo(price2)).isFalse();
    }

    @Test(expected = PricesIncomparableDifferentCurrenciesException.class)
    public void pricesWithDifferentCurrenciesCannotBeCompared() {
        Price price1 = Price.parseFrom("$33.45");
        Price price2 = Price.parseFrom("£32.45");
        price1.isLowerOrEqualTo(price2);
    }
}