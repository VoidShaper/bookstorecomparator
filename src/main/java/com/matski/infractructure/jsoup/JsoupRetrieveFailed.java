package com.matski.infractructure.jsoup;

public class JsoupRetrieveFailed implements JsoupRetrieveResult {
    @Override
    public boolean wasSuccessful() {
        return false;
    }
}
