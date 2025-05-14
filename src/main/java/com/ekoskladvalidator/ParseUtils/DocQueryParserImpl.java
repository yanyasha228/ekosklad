package com.ekoskladvalidator.ParseUtils;

import com.ekoskladvalidator.CustomExceptions.NotValidQueryException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.MalformedInputException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class DocQueryParserImpl implements DocQueryParser {

    private static final Logger log = LoggerFactory.getLogger(DocQueryParserImpl.class);

    public DocQueryParserImpl() {
    }

    @Override
    public Elements getElements(String url, String cssQuery) throws IOException {
        Optional<Document> doc = getDocument(url);

        return doc.map(document -> document.select(cssQuery)).orElse(null);

    }

    @Override
    public Optional<Document> getDocument(String url) throws IOException {

        Document doc = null;
///Have to change in-block try-catch to Spring @Retry


        try {
            doc = Jsoup.connect(url).timeout(30000).ignoreHttpErrors(true)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/113.0.0.0 Safari/537.36")
                    .header("Accept-Language", "ru-RU,ru;q=0.9,en;q=0.8")
                    .header("Accept", "text/html")
                    .get();
        } catch (SocketTimeoutException socketTimeoutException) {
            log.error("Timeout socket try again FIRST ATTEMPT");
            try {
                doc = Jsoup.connect(url).timeout(30000).ignoreHttpErrors(true)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                "Chrome/113.0.0.0 Safari/537.36")
                        .header("Accept-Language", "ru-RU,ru;q=0.9,en;q=0.8")
                        .header("Accept", "text/html")
                        .get();
            } catch (SocketTimeoutException sE) {
                log.error("Timeout socket try again SECOND ATTEMPT");
                try {
                    doc = Jsoup.connect(url).timeout(30000).ignoreHttpErrors(true)
                            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                    "Chrome/113.0.0.0 Safari/537.36")
                            .header("Accept-Language", "ru-RU,ru;q=0.9,en;q=0.8")
                            .header("Accept", "text/html")
                            .get();
                } catch (SocketTimeoutException sE2) {
                    return Optional.empty();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Returning null");
            return Optional.empty();
        }

        return Optional.of(doc);

    }

    @Override
    public Optional<String> getFirstElementValue(Document document, String cssQuery) throws IOException {

        if (Objects.nonNull(document)) {

            Elements ells = document.select(cssQuery);

            Element ell = ells.first();

            if (ell != null) return Optional.ofNullable(ell.text());
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getStringBuyXpath(Document document, String xPath) throws NotValidQueryException {
        if (xPath.contains("text()")) {
            String str = Xsoup.compile(xPath).evaluate(document).get();
            return Optional.ofNullable(str);
        }
        throw new NotValidQueryException("Xpath has no text() function");

    }

    public Optional<String> getFirstElementValue(String url, String cssQuery) throws IOException {

        Optional<Document> doc = getDocument(url);

        if (doc.isPresent()) {

            Elements ells = doc.get().select(cssQuery);

            Element ell = ells.first();

            if (ell != null) return Optional.ofNullable(ell.text());
        }

        return Optional.empty();
    }

    @Override
    public List<String> getElementsValues(String url, String cssQuery) throws IOException {
        return null;
    }

    @Override
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

