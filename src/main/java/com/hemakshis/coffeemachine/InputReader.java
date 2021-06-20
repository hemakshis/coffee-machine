package com.hemakshis.coffeemachine;

import com.hemakshis.coffeemachine.beverages.Beverage;
import com.hemakshis.coffeemachine.beverages.BeverageFactory;
import com.hemakshis.coffeemachine.constants.BeverageTypes;
import com.hemakshis.coffeemachine.constants.IngredientTypes;
import com.hemakshis.coffeemachine.ingredients.Ingredient;
import com.hemakshis.coffeemachine.ingredients.IngredientMap;
import org.apache.commons.text.WordUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InputReader {
    private final String filePath;
    private final JSONParser jsonParser;
    private JSONObject inputFileObject;

    public InputReader(final String filePath) throws ParseException, IOException {
        this.filePath = filePath;
        this.jsonParser = new JSONParser();
        this.inputFileObject = readFromFile();
    }

    private JSONObject readFromFile() throws ParseException, IOException {
        return (JSONObject) this.jsonParser.parse(new FileReader(this.filePath));
    }

    public Long getOutlets() {
        return (Long) ((JSONObject) ((JSONObject) this.inputFileObject.get("machine")).get("outlets")).get("count_n");
    }

    public IngredientMap getMachineIngredients() {
        Map<String, Long> machineIngredientsObject = (Map<String, Long>) ((JSONObject) this.inputFileObject
                .get("machine"))
                .get("total_items_quantity");
        Map<IngredientTypes, Ingredient> machineIngredients = new HashMap<>();
        machineIngredientsObject.forEach((k, v) -> {
            IngredientTypes ingredientType = convertToIngredientType(k);
            Ingredient ingredient = new Ingredient(ingredientType, v);
            machineIngredients.put(ingredientType, ingredient);
        });

        return new IngredientMap(machineIngredients);
    }

    public Map<BeverageTypes, Beverage> getMachineBeverages() {
        JSONObject beveragesObject = (JSONObject) ((JSONObject) this.inputFileObject
                .get("machine"))
                .get("beverages");
        Map<BeverageTypes, Beverage> machineBeverages = new HashMap<>();
        // forEach beverage traverse through the list of ingredients and create object
        beveragesObject.forEach((k, v) -> {
            Map<String, Long> beverageIngredientsObject = (Map<String, Long>) v;
            List<Ingredient> beverageIngredients = new LinkedList<>();
            beverageIngredientsObject.forEach((ik, iv) -> {
                Ingredient ingredient = new Ingredient(convertToIngredientType(ik), iv);
                beverageIngredients.add(ingredient);
            });
            Beverage beverage = BeverageFactory.getBeverage(convertToBeverageType((String) k), beverageIngredients);
            machineBeverages.put(convertToBeverageType((String) k), beverage);
        });

        return machineBeverages;
    }

    private IngredientTypes convertToIngredientType(String value) {
        return IngredientTypes.valueOf(value.toUpperCase());
    }

    private BeverageTypes convertToBeverageType(String value) {
        return BeverageTypes.valueOf(value.toUpperCase());
    }
}
