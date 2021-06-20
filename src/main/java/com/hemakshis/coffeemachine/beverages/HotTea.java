package com.hemakshis.coffeemachine.beverages;

import com.hemakshis.coffeemachine.ingredients.Ingredient;
import com.hemakshis.coffeemachine.constants.BeverageTypes;

import java.util.List;

public class HotTea extends Beverage {

    public HotTea(List<Ingredient> ingredients) {
        super(ingredients);
    }

    @Override
    public String getName() {
        return BeverageTypes.HOT_TEA.getBeverageType();
    }
}
