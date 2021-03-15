package com.ekoskladvalidator.ParseUtils;

import com.ekoskladvalidator.CustomExceptions.NotValidQueryException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DocQueryParser {

    Optional<String> getFirstElementValue(String url, String cssQuery) throws IOException;

    Elements getElements(String url, String cssQuery) throws IOException;

    Optional<Document> getDocument(String url) throws IOException;

    Optional<String> getFirstElementValue(Document document, String cssQuery) throws IOException;

    Optional<String> getStringBuyXpath(Document document,  String xPath) throws NotValidQueryException;

    List<String> getElementsValues(String url, String cssQuery) throws IOException;

    String getText(String url, String cssQuery) throws IOException;
}
