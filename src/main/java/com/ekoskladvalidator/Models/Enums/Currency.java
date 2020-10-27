package com.ekoskladvalidator.Models.Enums;

public enum Currency {

    UAH, USD, EUR;

    public String getCurrencySymbol() {
        String curSymb = "";
        switch (name()) {
            case "UAH":
                curSymb = "₴";
                break;
            case "USD":
                curSymb = "$";
                break;
            case "EUR":
                curSymb = "€";
                break;
        }
        return curSymb;
    }
}
