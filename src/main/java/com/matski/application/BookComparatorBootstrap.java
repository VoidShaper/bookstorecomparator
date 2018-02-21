package com.matski.application;

import com.matski.domain.bookstore.BookOfferComparator;
import com.matski.infractructure.external.bookstores.amazon.AmazonBookStore;
import com.matski.infractructure.external.bookstores.apress.ApressBookStoreAndNameResolver;
import com.matski.infractructure.external.bookstores.apress.ApressPriceParser;
import com.matski.infractructure.jsoup.JsoupClient;
import com.matski.infractructure.unirest.UnirestConnector;

import java.time.Duration;

import static java.util.Arrays.asList;

public class BookComparatorBootstrap {

    public BookOfferComparator bootstrapBookOfferComparator(BookComparatorConfiguration configuration) {
        JsoupClient jsoupClient = new JsoupClient();
        ApressBookStoreAndNameResolver apressBookStoreAndNameResolver =
                new ApressBookStoreAndNameResolver(configuration.getApressBaseUrl(),
                        jsoupClient,
                        new UnirestConnector(),
                        new ApressPriceParser());
        AmazonBookStore amazonBookStore = new AmazonBookStore(configuration.getAmazonBaseUrl(),
                jsoupClient,
                Duration.ofSeconds(2));

        return new BookOfferComparator(
                apressBookStoreAndNameResolver,
                asList(apressBookStoreAndNameResolver, amazonBookStore)
        );
    }
}
