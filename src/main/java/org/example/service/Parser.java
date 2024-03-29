package org.example.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Parser {
    public String getCurrencyPrice(String name) throws IOException {
        String url = String.format(System.getenv("CURRENCY_URL_PATTERN"), name);
        Document document = getDocumentByURL(url);
        return getTextBySelector(document);
    }

    protected Document getDocumentByURL(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    protected String getTextBySelector(Document document) {
        return document.select(System.getenv("CURRENCY_SELECTOR")).text().trim();
    }
}
