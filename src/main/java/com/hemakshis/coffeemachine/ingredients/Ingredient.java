package com.hemakshis.coffeemachine.ingredients;

import com.hemakshis.coffeemachine.constants.IngredientTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ingredient {
    private final IngredientTypes name;
    private Long quantity;

    public Ingredient(final IngredientTypes name, final Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
