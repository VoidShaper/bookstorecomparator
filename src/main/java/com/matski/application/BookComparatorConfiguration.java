package com.matski.application;

public class BookComparatorConfiguration {

    private final String apressBaseUrl;
    private final String amazonBaseUrl;

    public BookComparatorConfiguration(String apressBaseUrl, String amazonBaseUrl) {
        this.apressBaseUrl = apressBaseUrl;
        this.amazonBaseUrl = amazonBaseUrl;
    }

    public String getApressBaseUrl() {
        return apressBaseUrl;
    }

    public String getAmazonBaseUrl() {
        return amazonBaseUrl;
    }
}
