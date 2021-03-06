package com.matski.infractructure.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsoupClient {

    public static final Logger logger = LoggerFactory.getLogger(JsoupClient.class);

    public JsoupRetrieveResult getPageAndRetrieveElement(String pageQuery, String elementQuery) {
        try {
            Document document = Jsoup.connect(pageQuery)
                    .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Mobile Safari/537.36")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "en-GB,en;q=0.9,en-US;q=0.8,pl;q=0.7")
                    .timeout(10000)
                    .get();
            Elements elements = document.select(elementQuery);
            if (elements.isEmpty()) {
                return new JsoupRetrieveFailed();
            }
            return new JsoupRetrieveSuccess(elements.first());
        } catch (IOException e) {
            logger.warn("Jsoup client GET query failed {} when trying to parse {}",
                    pageQuery, elementQuery, e);
            return new JsoupRetrieveFailed();
        }
    }
}
