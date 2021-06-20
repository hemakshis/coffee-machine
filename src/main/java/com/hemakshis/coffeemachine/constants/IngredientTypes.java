package com.hemakshis.coffeemachine.constants;

import lombok.Getter;

public enum IngredientTypes {
    HOT_MILK("Hot Milk"),
    HOT_WATER("Hot Water"),
    GREEN_MIXTURE("Green Mixture"),
    SUGAR_SYRUP("Sugar Syrup"),
    GINGER_SYRUP("Ginger Syrup"),
    TEA_LEAVES_SYRUP("Tea Leaves Syrup"),
    COFFEE_BEANS("Coffee Beans");

    @Getter
    private String ingredientType;

    IngredientTypes(String ingredientType) {
        this.ingredientType = ingredientType;
    }
}
