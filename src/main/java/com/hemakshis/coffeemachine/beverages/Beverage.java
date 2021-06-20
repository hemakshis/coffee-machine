package com.hemakshis.coffeemachine.beverages;

import com.hemakshis.coffeemachine.ingredients.Ingredient;
import com.hemakshis.coffeemachine.utils.CurrentTime;
import lombok.Getter;

import java.util.List;

public abstract class Beverage {
    @Getter
    private final List<Ingredient> ingredients;

    public Beverage(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public abstract String getName();

    public String serve() {
        String currentTime = new CurrentTime().getCurrentTime();
        return String.format("Your `%s` is served at time %s from outlet no. %s.", getName(), currentTime, Thread.currentThread().getId());
    }
}
