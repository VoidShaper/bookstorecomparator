package com.matski.infractructure.jsoup;

import org.jsoup.nodes.Element;

public class JsoupRetrieveSuccess implements JsoupRetrieveResult {

    private final Element element;

    public JsoupRetrieveSuccess(Element element) {
        this.element = element;
    }

    @Override
    public boolean wasSuccessful() {
        return true;
    }

    public String elementContent() {
        return element.text();
    }

    public String elementAttribute(String attribute) {
        return element.attr(attribute);
    }
}
