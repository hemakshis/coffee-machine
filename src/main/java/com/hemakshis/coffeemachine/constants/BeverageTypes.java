package com.hemakshis.coffeemachine.constants;

import lombok.Getter;

public enum BeverageTypes {
    HOT_COFFEE("Hot Coffee"), HOT_TEA("Hot Tea"), GREEN_TEA("Green Tea"), BLACK_TEA("Black Tea");

    @Getter
    private String beverageType;

    BeverageTypes(String beverageType) {
        this.beverageType = beverageType;
    }
}
