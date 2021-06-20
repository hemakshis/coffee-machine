package com.hemakshis.coffeemachine;

import com.hemakshis.coffeemachine.beverages.Beverage;
import com.hemakshis.coffeemachine.constants.BeverageTypes;
import com.hemakshis.coffeemachine.ingredients.Ingredient;
import com.hemakshis.coffeemachine.ingredients.IngredientMap;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class CoffeeMachineApplication {
	public static void main(String[] args) throws ParseException, IOException, InterruptedException {
		// Read the input from the file in src/main/resources/input.json
		InputReader inputReader = new InputReader("/home/hemakshis/Projects/Low Level Design/coffee-machine/src/main/resources/input.json");

		// Get all the details regarding the machine like - no. of outlets, ingredients and beverages it can make
		Long outlets = inputReader.getOutlets();
		IngredientMap machineIngredients = inputReader.getMachineIngredients();
		Map<BeverageTypes, Beverage> machineBeverages = inputReader.getMachineBeverages();

		// Initialize a coffee machine with name and no. of outlets and then set the ingredients and beverages
		CoffeeMachine coffeeMachine = new CoffeeMachine("Chai Point", outlets);
		coffeeMachine.setBeverages(machineBeverages);
		coffeeMachine.setIngredients(machineIngredients);

		// Display screen to show options that are available with the coffee machine.
		Display display = new Display(coffeeMachine);
		display.screen();
	}
}
