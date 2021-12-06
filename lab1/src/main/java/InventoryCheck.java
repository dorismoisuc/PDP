public class InventoryCheck implements Runnable {
    private final SupermarketInventory supermarketInventory;

    public InventoryCheck(SupermarketInventory supermarketInventory) {
        this.supermarketInventory = supermarketInventory;
    }

    @Override
    public void run() {
        while (supermarketInventory.hasAvailableProducts()) {
            supermarketInventory.runInventoryCheck();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        supermarketInventory.runFinalInventoryCheck();
    }
}
