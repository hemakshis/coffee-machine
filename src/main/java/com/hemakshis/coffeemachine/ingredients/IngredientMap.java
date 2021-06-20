package com.hemakshis.coffeemachine.ingredients;

import com.hemakshis.coffeemachine.constants.BeverageTypes;
import com.hemakshis.coffeemachine.constants.IngredientTypes;
import lombok.Getter;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IngredientMap {
    @Getter
    private Map<IngredientTypes, Ingredient> ingredientMap;

    public IngredientMap(Map<IngredientTypes, Ingredient> ingredientMap) {
        this.ingredientMap = ingredientMap;
    }

    public void addNewIngredient(IngredientTypes ingredientType, Ingredient ingredient) {
        ingredientMap.put(ingredientType, ingredient);
    }

    public Boolean isIngredientSupported(IngredientTypes ingredientType) {
        return ingredientMap.containsKey(ingredientType);
    }

    public Long getIngredientQuantity(IngredientTypes ingredientType) {
        return ingredientMap.get(ingredientType).getQuantity();
    }

    public void setIngredientQuantity(IngredientTypes ingredientType, Long quantity) {
        Ingredient ingredient = new Ingredient(ingredientType, quantity);
        ingredientMap.put(ingredientType, ingredient);
    }

    public void updateIngredientQuantity(IngredientTypes ingredientType, Long quantity) {
        Long newQuantity = ingredientMap.get(ingredientType).getQuantity() + quantity;
        ingredientMap.get(ingredientType).setQuantity(newQuantity);
    }

    public int size() {
        return ingredientMap.size();
    }

    public Ingredient remove(IngredientTypes ingredientType) {
        return ingredientMap.remove(ingredientType);
    }

    public String toString() {
        String value = "";
        List<IngredientTypes> ingredientTypes = new LinkedList<>(ingredientMap.keySet());
        ingredientTypes.sort(Comparator.comparing(IngredientTypes::getIngredientType));
        value = value.concat("Ingredient\tQuantity Left\n");
        for (IngredientTypes it: ingredientTypes) {
            value = value.concat(it.getIngredientType() + ":\t" + ingredientMap.get(it).getQuantity()+"\n");
        }

        return value;
    }
}
