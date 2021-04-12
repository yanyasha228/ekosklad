package com.ekoskladvalidator.Models.Enums;

public enum QueryType {
    X_PATH, CSS_QUERY;

    public String getAlias() {
        switch (name()) {
            case "X_PATH":
                return "XPath";
            case "CSS_QUERY":
                return "CssQuery(селектор)";
        }
        return "";
    }
}
