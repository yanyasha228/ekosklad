package com.ekoskladvalidator.ParseUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.MalformedInputException;

@Component
public class CssQueryParser {

    private static final Logger log = LoggerFactory.getLogger(CssQueryParser.class);

    public CssQueryParser() {
    }

    public String getText(String url, String cssQuery) throws IOException {

        Document doc;

        try {
            doc = Jsoup.connect(url).timeout(30000).ignoreHttpErrors(true).get();
        } catch (MalformedInputException e) {
            return "null";
        }

        Elements ells = doc.select(cssQuery);
        Element ell = ells.first();

        if (ell != null) return ell.text();

        return "null";
    }

}

