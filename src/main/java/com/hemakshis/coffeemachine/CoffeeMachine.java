package com.hemakshis.coffeemachine;

import com.hemakshis.coffeemachine.beverages.Beverage;
import com.hemakshis.coffeemachine.constants.BeverageTypes;
import com.hemakshis.coffeemachine.constants.IngredientTypes;
import com.hemakshis.coffeemachine.ingredients.Ingredient;
import com.hemakshis.coffeemachine.ingredients.IngredientMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

@ToString
public class CoffeeMachine {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeMachine.class);

    @Getter
    private final String name;
    @Getter
    private final int outlets;
    @Getter
    private final ExecutorService coffeeMakerThreadExecutor;

    @Getter @Setter
    private Map<BeverageTypes, Beverage> beverages;
    @Getter @Setter
    private IngredientMap ingredients;
    @Getter @Setter
    private boolean isRefilling = false;
    @Getter @Setter
    IngredientMap ingredientsThatNeedRefill = new IngredientMap(new HashMap<>());

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public CoffeeMachine(final String name, final Long outlets) {
        this.name = name;
        this.outlets = outlets.intValue();
        this.coffeeMakerThreadExecutor = Executors.newFixedThreadPool(this.outlets);
    }

    /**
     * Returns a list of beverages supported by the coffee machine in a nice printable format
     * @return String of beverages in alphabetic sequence
     */
    public String getBeverageList() {
        List<BeverageTypes> nameList = new LinkedList<>(beverages.keySet());
        nameList.sort(Comparator.comparing(BeverageTypes::getBeverageType));
        String list = "";

        for (int i = 0; i < nameList.size(); i++) {
            list = list.concat((i+1) + ". " + nameList.get(i).getBeverageType() + "\n");
        }

        return list;
    }

    /**
     * The main method which takes beverageType as argument and starts a new thread to actually start making the beverage
     * @param beverageType The type of beverage that user wants to be served
     * @return boolean if serving beverage is possible or not
     */
    public boolean serve(BeverageTypes beverageType) {
        if (ingredientsThatNeedRefill.size() > 0) {
            logger.info("Coffee machine is running low on ingredients. Please refill before requesting any drinks.");
        } else if (isRefilling) {
            logger.info("Coffee machine is being refilled, please wait.");
        } else {
            this.coffeeMakerThreadExecutor.execute(submitTask(beverageType));
            return true;
        }

        return false;
    }

    /**
     * The actual task performed by each thread, first check if the beverage asked is supported by the coffee machine or not.
     * Then if the beverage is makeable it will make it and thread will sleep for some time
     * @param beverageType BeverageType that needs to be made and served
     * @return Runnable object
     */
    private Runnable submitTask(BeverageTypes beverageType) {
        return () -> {
            if (beverages.containsKey(beverageType)) {
                Beverage beverage = beverages.get(beverageType);
                if (validateAndMake(beverage)) {
                    logger.info("Starting to prepare your `{}` at time: {} on machine outlet no.: {}",
                            beverageType.getBeverageType(), dtf.format(LocalDateTime.now()), Thread.currentThread().getId());
                    try {
                        long waitTime = getWaitTime(5000, 12000); // wait for 5 to 12 seconds
                        logger.info("Your `{}` will be ready in: {}ms on machine outlet no. {}",
                                beverageType.getBeverageType(),  waitTime, Thread.currentThread().getId());
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info(beverages.get(beverageType).serve());
                }
            } else {
                logger.info("Coffee Machine doesn't support `{}`.", beverageType.getBeverageType());
            }
        };
    }

    public boolean shutdown() throws InterruptedException {
        this.coffeeMakerThreadExecutor.shutdown();
        return this.awaitCompletion(10000L);
    }

    public boolean awaitCompletion(Long time) throws InterruptedException {
        return this.coffeeMakerThreadExecutor.awaitTermination(time, TimeUnit.MILLISECONDS);
    }

    /**
     * Refill multiple ingredients at once
     * @param newIngredients List of ingredients that need to be refilled
     * @throws InterruptedException
     */
    synchronized public void multiRefill(List<Ingredient> newIngredients) throws InterruptedException {
        for (Ingredient ing: newIngredients) {
            refill(ing.getName(), ing.getQuantity());
        }
    }

    /**
     * Refills one ingredient. Increments the ingredient amount in the machine by the quantity provided. Adds the new
     * ingredients to the IngredientMap if it's a new ingredient
     * @param ingredientType Ingredient type that needs to be refilled
     * @param quantity Quantity of the ingredients being refilled
     * @throws InterruptedException
     */
    synchronized public void refill(IngredientTypes ingredientType, Long quantity) throws InterruptedException {
        setRefilling(true);
        if (!ingredients.isIngredientSupported(ingredientType)) { // New ingredient
            ingredients.addNewIngredient(ingredientType, new Ingredient(ingredientType, quantity));
        } else {
            ingredients.updateIngredientQuantity(ingredientType, quantity);
            ingredientsThatNeedRefill.remove(ingredientType);
        }
        Thread.sleep(getWaitTime(2000, 5000)); // wait for 2 to 5 seconds
        logger.info("Refilled `{}` and new quantity is: {} at time: {}",
                ingredientType.getIngredientType(), ingredients.getIngredientQuantity(ingredientType), dtf.format(LocalDateTime.now()));
        setRefilling(false);
    }

    /**
     * Checks if a it's possible to make the beverage with the available ingredients in the machine by checking if each needed
     * ingredient is present and machine has the minimum quantity to make it.
     * @param beverage Beverage that the user wants
     * @return boolean whether it's possible to make the beverage or not
     */
    synchronized private boolean validateAndMake(Beverage beverage) {
        for (Ingredient ing: beverage.getIngredients()) {
            if (!ingredients.isIngredientSupported(ing.getName())) {
                logger.info("`{}` cannot be prepared because `{}` is not available", beverage.getName(), ing.getName().getIngredientType());
                return false;
            }
            if (ing.getQuantity() > ingredients.getIngredientQuantity(ing.getName())) {
                logger.info("`{}` cannot be prepared because `{}` is not sufficient", beverage.getName(), ing.getName().getIngredientType());
                this.ingredientsThatNeedRefill.addNewIngredient(ing.getName(), ing);
                return false;
            }
        }

        // Start making the beverage
        make(beverage);
        return true;
    }

    /**
     * Decrements the ingredient quantity in the IngredientMap
     * @param beverage Beverage that the user wants
     */
    synchronized private void make(Beverage beverage) {
        for (Ingredient ing: beverage.getIngredients()) {
            ingredients.updateIngredientQuantity(ing.getName(), -ing.getQuantity());
        }
    }

    /**
     * Helper method to get a random wait time for the given min and max range
     * @param min Minimum time to wait in milliseconds
     * @param max Maximum time to wait in milliseconds
     * @return long a random value between min and max
     */
    private long getWaitTime(int min, int max) {
        Random random = new Random();
        return random.longs(min, max).findFirst().getAsLong();
    }
}
