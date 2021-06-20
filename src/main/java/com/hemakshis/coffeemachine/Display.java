package com.hemakshis.coffeemachine;

import com.hemakshis.coffeemachine.constants.BeverageTypes;
import com.hemakshis.coffeemachine.constants.IngredientTypes;
import com.hemakshis.coffeemachine.ingredients.Ingredient;
import com.hemakshis.coffeemachine.ingredients.IngredientMap;

import java.util.*;

public class Display {
    private final CoffeeMachine coffeeMachine;

    public Display(final CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    public void screen() throws InterruptedException {
        System.out.println("Welcome to " + coffeeMachine.getName() + "!");
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("********************\n" +
                    "1. Menu\n" +
                    "2. Get a drink\n" +
                    "3. Refill ingredients\n" +
                    "4. Show ingredients stock\n" +
                    "5. Refill all needed ingredients\n" +
                    "6. Exit\n" +
                    "********************");
            String option = sc.nextLine();
            boolean exit = false;
            switch (option) {
                case "1":
                    System.out.println(this.coffeeMachine.getBeverageList());
                    break;
                case "2":
                    System.out.print("Enter drink name: ");
                    String drink = sc.nextLine();
                    this.coffeeMachine.serve(BeverageTypes.valueOf(drink));
                    break;
                case "3":
                    System.out.print("Ingredient name: ");
                    String ingredient = sc.nextLine();
                    System.out.print("Quantity: ");
                    Long quantity = sc.nextLong();
                    System.out.println("Adding " + ingredient + " and quantity " + quantity);
                    this.coffeeMachine.refill(IngredientTypes.valueOf(ingredient), quantity);
                    break;
                case "4":
                    System.out.println(this.coffeeMachine.getIngredients().toString());
                    break;
                case "5":
                    System.out.println("Enter quantity beside each needed ingredient");
                    List<Ingredient> refill = new LinkedList<>();
                    for (Map.Entry s: this.coffeeMachine.getIngredientsThatNeedRefill().getIngredientMap().entrySet()) {
                        System.out.print(s.getKey() + ": ");
                        Long refillAmt = sc.nextLong();
                        refill.add(new Ingredient(IngredientTypes.valueOf(s.getKey().toString()), refillAmt));
                    }
                    this.coffeeMachine.multiRefill(refill);
                    break;
                case "6":
                    System.out.println("Exiting coffee machine.");
                    this.coffeeMachine.shutdown();
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid operation.");
                    break;
            }

            if (exit)
                break;
        }
    }
}
