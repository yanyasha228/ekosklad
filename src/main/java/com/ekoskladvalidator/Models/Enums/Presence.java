package com.ekoskladvalidator.Models.Enums;

public enum Presence {
    available, not_available, order, waiting;

    public String getAlias() {
        switch (name()) {
            case "available":
                return "В наличии";
            case "not_available":
                return "Нет в наличии";
            case "order":
                return "Под заказ";
            case "waiting":
                return "Временно недоступен";
        }
        return "";
    }

}
