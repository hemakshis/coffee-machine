package com.hemakshis.coffeemachine.beverages;

import com.hemakshis.coffeemachine.ingredients.Ingredient;
import com.hemakshis.coffeemachine.constants.BeverageTypes;
import com.hemakshis.coffeemachine.utils.CurrentTime;
import lombok.Getter;

import java.util.List;

public class BlackTea extends Beverage {

    public BlackTea(List<Ingredient> ingredients) {
        super(ingredients);
    }

    @Override
    public String getName() {
        return BeverageTypes.BLACK_TEA.getBeverageType();
    }
}
