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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.nio.charset.MalformedInputException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class DocQueryParserImpl implements DocQueryParser {

    private static final Logger log = LoggerFactory.getLogger(DocQueryParserImpl.class);

    private final List<Proxy> proxies = List.of(
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress("91.124.109.60", 50100))
    );

    public DocQueryParserImpl() {
    }

    @Override
    public Elements getElements(String url, String cssQuery) throws IOException {
        Optional<Document> doc = getDocument(url);

        return doc.map(document -> document.select(cssQuery)).orElse(null);

    }

    @Override
    public Optional<Document> getDocument(String url) throws IOException {

        try {
            log.info("Fetching {} directly...", url);
            return Optional.of(fetch(url, null));
        } catch (IOException e) {
            log.warn("Direct fetch failed for {}: {}", url, e.getMessage());
        }

        // пробуем через список прокси
        for (Proxy proxy : proxies) {
            try {
                log.info("Trying proxy {} for {}", proxy.address(), url);
                return Optional.of(fetch(url, proxy));
            } catch (IOException e) {
                log.warn("Proxy {} failed for {}: {}", proxy.address(), url, e.getMessage());
            }
        }

        log.error("All proxies failed for {}", url);
        return Optional.empty();


    }

    private Document fetch(String url, Proxy proxy) throws IOException {
        var connection = Jsoup.connect(url)
                .timeout(30000)
                .ignoreHttpErrors(true)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/113.0.0.0 Safari/537.36")
                .header("Accept-Language", "ru-RU,ru;q=0.9,en;q=0.8")
                .header("Accept", "text/html");

        if (proxy != null) {
            connection.proxy(proxy);
        }

        return connection.get();
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

