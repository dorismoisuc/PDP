import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final List<Product> productList = new LinkedList<>();

    public static void main(String[] args) {
        SupermarketInventory supermarketInventory = new SupermarketInventory();
        InventoryCheck inventoryCheck = new InventoryCheck(supermarketInventory);
        createProducts(75);
        addProductsToSupermarket(supermarketInventory);
        runClients(supermarketInventory);
        runInventoryCheck(inventoryCheck);

    }

    private static void runInventoryCheck(InventoryCheck inventoryCheck) {
        new Thread(inventoryCheck).start();
    }

    private static void runClients(SupermarketInventory supermarketInventory) {
        int numberOfClients = 1000;

        for (int i = 0; i < numberOfClients; i++) {
            new Thread(new Sale(supermarketInventory)).start();
        }
    }

    private static void addProductsToSupermarket(SupermarketInventory supermarketInventory) {
        Random random = new Random();
        int minQuantity = 1000;
        int maxQuantity = 2000;

        // add each product with a random quantity between minQuantity and maxQuantity
        productList.forEach(product -> supermarketInventory.addProduct(product, random.nextInt(maxQuantity - minQuantity) + minQuantity));
    }

    /**
     * create numberOfProducts products with price random (1-100) and add them to product list
     *
     */
    private static void createProducts(int numberOfProducts) {
        Random random = new Random();
        for (int i = 0; i < numberOfProducts; i++) {
            productList.add(new Product("Product" + i, random.nextInt(100) +1));
        }
    }
}
