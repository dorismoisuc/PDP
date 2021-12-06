public class Sale implements Runnable {
    private final SupermarketInventory supermarketInventory;

    public Sale(SupermarketInventory supermarketInventory) {
        this.supermarketInventory = supermarketInventory;
    }

    @Override
    public void run() {
        while (supermarketInventory.hasAvailableProducts()) {
            supermarketInventory.sellProducts();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
