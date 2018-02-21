package com.matski.application;

import com.matski.domain.books.BookName;
import com.matski.domain.bookstore.BookOffer;
import com.matski.domain.bookstore.BookOfferComparator;
import com.matski.domain.bookstore.BookOfferComparison;

import java.util.Collection;
import java.util.Scanner;

public class BookComparatorApp {

    public static void main(String[] args) {
        BookComparatorConfiguration configuration = new BookComparatorConfiguration(
                "https://www.apress.com",
                "https://www.amazon.com"
        );

        BookOfferComparator bookOfferComparator = new BookComparatorBootstrap()
                .bootstrapBookOfferComparator(configuration);

        readNameAndPresentTheBestOfferUsing(bookOfferComparator);
    }

    private static void readNameAndPresentTheBestOfferUsing(BookOfferComparator bookOfferComparator) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the book you wish to find: ");
        BookName bookName = new BookName(scanner.nextLine());

        try {
            BookOfferComparison bookOfferComparison = bookOfferComparator.offerComparisonFor(bookName);

            brieflyPresent(bookOfferComparison.allOffers());

            bookOfferComparison.bestOffer()
                    .ifPresent(BookComparatorApp::presentBestOffer);
        } catch (Exception e) {
            System.out.println("Cannot present offers due to: " + e.getMessage());
        }
    }

    private static void brieflyPresent(Collection<BookOffer> bookOffers) {
        System.out.println("Found following offers for the book:");
        for (BookOffer bookOffer : bookOffers) {
            System.out.println(String.format(
                    "\"%s\" on bookstore %s: %s",
                    bookOffer.book().name(),
                    bookOffer.bookStoreName(),
                    bookOffer.price().asFormattedString()
                    )
            );
        }
    }

    private static void presentBestOffer(BookOffer bookOffer) {
        System.out.println(String.format("Best chosen offer is from %s price: %s",
                bookOffer.bookStoreName(),
                bookOffer.price().asFormattedString()));
        System.out.println(String.format("The address where you can buy the book: %s",
                bookOffer.bookStoreAddress()));
    }
}
