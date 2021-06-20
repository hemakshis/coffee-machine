package com.hemakshis.coffeemachine.beverages;

import com.hemakshis.coffeemachine.constants.BeverageTypes;
import com.hemakshis.coffeemachine.ingredients.Ingredient;

import java.util.List;

public class BeverageFactory {
    public static Beverage getBeverage(BeverageTypes beverageType, List<Ingredient> ingredients) {
        switch (beverageType) {
            case HOT_COFFEE: return new HotCoffee(ingredients);
            case HOT_TEA: return new HotTea(ingredients);
            case BLACK_TEA: return new BlackTea(ingredients);
            case GREEN_TEA: return new GreenTea(ingredients);
            default: return null;
        }
    }
}
