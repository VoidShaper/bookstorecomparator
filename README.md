# Book store comparator

This is an example application written for the interview.
It is a very simple command line application - to show more of a domain driven design, dependency injection and testing.

## What this app does

Basically, application asks you for the name of the book. It then tries to find the matching ISBN number on Apress site.
After succeeding it will proceed on to check the price of the book

Apress integration utilises the main search offer page to retrieve ISBN (as user can type incomplete name, etc.) and post to api to get the price.
Amazon integration gets the whole page for the price, because using the API requires to register as an associate.

## Design patterns and configurations

The whole architecture of this simple application is meant to show Domain Driven Design approach with so called hexagonal or inverted architecture.
The main benefit of doing this is that we have different parts that can be tested individually.
The design also favours readability, maintainability and immutability of objects and methods with clean code rules in mind.

The code consists of 3 parts:
 1. application - This is where the running main class resides. It also contains bootstrap code and configuration that is also reused in the Integration test.
 
 2. domain - 
 This is where all of the business logic resides. 
 I have outlined 3 different domains that interact with each other: money, books, bookstores. 
 They try to maintain flat structure of hierarchy.
 Note that this package is self-contained and uses only plain Java objects to give maximum expression power to the developer for the maximum readability.
 All other supporting external services are presented here as interfaces and are wired in through dependency injection.
 
 3. infrastructure - This is where interaction with external services occur. Main integration is done with Amazon and Apress via jsoup and unirest clients.
 
## Testing

All of the domain code has been TDDed and most parts have thorough unit tests. 
External connectors have been tested by an Integration tests.
Further testing might be done to test the full running app - see "Further improvements" below.

## Building and running the application

An application is a simple single jar Maven app (so called "fat jar").

To compile after downloading the code type:

`$: mvn package`

This will build a .jar file in the target folder.
To run an application simply type:

`$: java -jar target\book-comparator-1.0-SNAPSHOT.jar`

You will be presented with the prompt:

`$: Enter the book you wish to find:`

Then type something like:

`$: Enter the book you wish to find: Scala for Java Developers`

Which should present you with the offers found and show the best offer at the end:

```
Found following offers for the book:
"Scala for JAva Developers" on bookstore Apress: $19.99
"Scala for JAva Developers" on bookstore Amazon: $29.99
Best chosen offer is from Apress price: $19.99
```

## Further improvements

 - test of the main method - this could be performed to test entire application, however since application calls out to other services, it would be best to parse some config file to reroute calls to some wiremock instance; might be done as later testing stage, i.e. integration testing
 - testing of equals/hashcode methods - verification of equals and hashcode methods could be employed with something like EqualsVerifier. However other test methods are testing this partially already.
 - better error / special condition handling: it could be improved on how errors are handled from each bookstore to add more visibility into what is happening
 - it for now only finds books that are on apress.com, it might support some other name to ISBN resolution in the future
 - convert to web application - with how it has been written it should be very easy to convert to some other type of application, like webapp; the domain, infrastructure and bootstrap parts can be reused in that case with no modification;
