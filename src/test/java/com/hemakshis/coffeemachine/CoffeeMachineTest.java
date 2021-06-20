package com.hemakshis.coffeemachine;

import com.hemakshis.coffeemachine.beverages.Beverage;
import com.hemakshis.coffeemachine.constants.BeverageTypes;
import com.hemakshis.coffeemachine.constants.IngredientTypes;
import com.hemakshis.coffeemachine.ingredients.Ingredient;
import com.hemakshis.coffeemachine.ingredients.IngredientMap;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CoffeeMachineTest {

	private CoffeeMachine coffeeMachine;

	@Before
	public void setUp() throws ParseException, IOException {
		InputReader inputReader = new InputReader("/home/hemakshis/Projects/Low Level Design/coffee-machine/src/main/resources/input.json");

		Long outlets = inputReader.getOutlets();
		IngredientMap machineIngredients = inputReader.getMachineIngredients();
		Map<BeverageTypes, Beverage> machineBeverages = inputReader.getMachineBeverages();

		coffeeMachine = new CoffeeMachine("Chai Point", outlets);
		coffeeMachine.setBeverages(machineBeverages);
		coffeeMachine.setIngredients(machineIngredients);
	}

	@After
	public void tearDown() throws InterruptedException {
		coffeeMachine.shutdown();
	}

	@Test
	public void serve() throws InterruptedException {
		assertEquals("1. Black Tea\n2. Green Tea\n3. Hot Coffee\n4. Hot Tea\n",
				coffeeMachine.getBeverageList());
		List<BeverageTypes> drinks = Arrays.asList(
				BeverageTypes.HOT_COFFEE,
				BeverageTypes.HOT_TEA,
				BeverageTypes.GREEN_TEA,
				BeverageTypes.HOT_COFFEE,
				BeverageTypes.BLACK_TEA);

		// There are limited outlets (2 only), at a time two orders will be taken. As soon as one order is
		// completed the next order in the queue will start to progress.
		for (BeverageTypes drink: drinks)
			assertTrue(coffeeMachine.serve(drink));

		coffeeMachine.awaitCompletion(10000L);

		assertEquals("Ingredient\tQuantity Left\nHot Milk:\t400\nHot Water:\t300\n",
				coffeeMachine.getIngredientsThatNeedRefill().toString());

		assertFalse(coffeeMachine.serve(BeverageTypes.HOT_TEA)); // request rejected because coffee machine is running low on ingredients

		coffeeMachine.multiRefill(Arrays.asList(
				new Ingredient(IngredientTypes.HOT_MILK, 1000L),
				new Ingredient(IngredientTypes.HOT_WATER, 1000L),
				new Ingredient(IngredientTypes.GREEN_MIXTURE, 500L)));

		assertTrue(coffeeMachine.serve(BeverageTypes.HOT_TEA)); // request succeeds because ingredients were refilled
	}

}
