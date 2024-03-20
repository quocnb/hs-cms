import java.util.Scanner;

/**
 * Coffee Ingredient Definition Class
 */
class CoffeeIngredient {
    private int water;
    private int milk;
    private int coffeeBeans;

    public CoffeeIngredient(int water, int milk, int coffeeBeans) {
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
    }

    public int getWater() {
        return water;
    }

    public int getMilk() {
        return milk;
    }

    public int getCoffeeBeans() {
        return coffeeBeans;
    }

    public void minus(CoffeeIngredient ingredient) {
        this.water -= ingredient.water;
        this.milk -= ingredient.milk;
        this.coffeeBeans -= ingredient.coffeeBeans;
    }

    public void add(int water, int milk, int coffeeBeans) {
        this.water += water;
        this.milk += milk;
        this.coffeeBeans += coffeeBeans;
    }
}

record CoffeeCup(CoffeeIngredient ingredient, int price) {

    /**
     * For one espresso, the coffee machine needs 250 ml of water and 16 g of coffee beans.
     * It costs $4.
     *
     * @return CoffeeCup instance
     */
    public static CoffeeCup espresso() {
        var ingredient = new CoffeeIngredient(250, 0, 16);
        return new CoffeeCup(ingredient, 4);
    }

    /**
     * For a latte, the coffee machine needs 350 ml of water, 75 ml of milk, and 20 g of coffee beans.
     * It costs $7.
     *
     * @return CoffeeCup instance
     */
    public static CoffeeCup latte() {
        var ingredient = new CoffeeIngredient(350, 75, 20);
        return new CoffeeCup(ingredient, 7);
    }

    /**
     * For a cappuccino, the coffee machine needs 200 ml of water, 100 ml of milk, and 12 g of coffee beans.
     * It costs $6.
     *
     * @return CoffeeCup instance
     */
    public static CoffeeCup cappuccino() {
        var ingredient = new CoffeeIngredient(200, 100, 12);
        return new CoffeeCup(ingredient, 6);
    }
}

/**
 * Coffee Machine
 */
class Machine {
    private final CoffeeIngredient ingredient;
    private int money;
    private int disposableCups;

    public Machine(CoffeeIngredient ingredient, int money, int disposableCups) {
        this.ingredient = ingredient;
        this.money = money;
        this.disposableCups = disposableCups;
    }

    public void sell(CoffeeCup cup) {
        if (ingredient.getWater() < cup.ingredient().getWater()) {
            System.out.println("Sorry, not enough water!");
            return;
        }
        if (ingredient.getMilk() < cup.ingredient().getMilk()) {
            System.out.println("Sorry, not enough milk!");
            return;
        }
        if (ingredient.getCoffeeBeans() < cup.ingredient().getCoffeeBeans()) {
            System.out.println("Sorry, not enough coffee beans!");
            return;
        }
        if (disposableCups < 1) {
            System.out.println("Sorry, not enough disposable cup!");
            return;
        }
        // Increase money
        money += cup.price();
        // Reduce ingredient
        ingredient.minus(cup.ingredient());
        // Reduce disposable cups
        disposableCups -= 1;
        System.out.println("I have enough resources, making you a coffee!");
    }

    public void fill(int water, int milk, int coffeeBeans, int disposableCups) {
        ingredient.add(water, milk, coffeeBeans);
        this.disposableCups += disposableCups;
    }

    public int take() {
        int takeMoney = this.money;
        this.money = 0;
        return takeMoney;
    }

    public void printRemaining() {
        System.out.println();
        System.out.println(this);
    }

    @Override
    public String toString() {
        String format = "The coffee machine has:\n" +
                "%d ml of water\n" +
                "%d ml of milk\n" +
                "%d g of coffee beans\n" +
                "%d disposable cups\n" +
                "$%d of money";
        return String.format(format,
                ingredient.getWater(),
                ingredient.getMilk(),
                ingredient.getCoffeeBeans(),
                disposableCups,
                money);
    }
}

/**
 * Main class
 */
public class CoffeeMachine {
    public static void main(String[] args) {
        // $550, 400 ml of water, 540 ml of milk, 120 g of coffee beans, and 9 disposable cups.
        Scanner sc = new Scanner(System.in);

        // Setup Machine
        Machine machine = new Machine(new CoffeeIngredient(400, 540, 120),
                550,
                9);
        // Coffee Cup List
        CoffeeCup[] coffeeCups = new CoffeeCup[] {CoffeeCup.espresso(), CoffeeCup.latte(), CoffeeCup.cappuccino()};

        boolean stop = false;
        while (!stop) {
            // Ask user for mode
            System.out.print("Write action (buy, fill, take, remaining, exit): \n" + "> ");
            switch (sc.next()) {
                case "buy": {
                    // Buy mode
                    System.out.print("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino: , back - to main menu: \n" + "> ");
                    String buyMode = sc.next();
                    if ("back".equals(buyMode)) {
                        // Back to main menu
                        break;
                    }
                    CoffeeCup cup = coffeeCups[Integer.parseInt(buyMode) - 1];
                    machine.sell(cup);
                }
                break;
                case "fill": {
                    // Fill mode
                    System.out.println();
                    System.out.print("Write how many ml of water you want to add: "  + "\n> ");
                    int water = sc.nextInt();
                    System.out.print("Write how many ml of milk you want to add: "  + "\n> ");
                    int milk = sc.nextInt();
                    System.out.print("Write how many grams of coffee beans you want to add: "  + "\n> ");
                    int coffeeBeans = sc.nextInt();
                    System.out.print("Write how many disposable cups you want to add: "  + "\n> ");
                    int disposableCups = sc.nextInt();
                    machine.fill(water, milk, coffeeBeans, disposableCups);
                }
                break;
                case "take": {
                    // Take mode
                    // Gave all money and print message
                    System.out.println();
                    System.out.printf("I gave you $%d\n", machine.take());
                }
                break;
                case "remaining": {
                    // Remaining mode
                    // Display machine's info
                    machine.printRemaining();
                }
                break;
                case "exit": {
                    // Exit mode
                    // Quit app
                    stop = true;
                }
                break;
            }
            if (!stop) {
                System.out.println();
            }
        }
    }
}
