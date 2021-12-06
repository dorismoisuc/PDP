import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SupermarketInventory {
    private static final Logger LOG = LogManager.getLogger(SupermarketInventory.class);

    private final Map<Product, Integer> productsQuantityMap;
    private final List<Bill> billsList;
    private final AtomicLong revenue;
    private final AtomicLong initialQuantity = new AtomicLong(0);
    private float startTime = 0f;
    private float endTime = 0f;

    public SupermarketInventory() {
        this.revenue = new AtomicLong(0);
        this.billsList = Collections.synchronizedList((new LinkedList<>()));
        this.productsQuantityMap = new ConcurrentHashMap<>();
    }

    public void addProduct(Product product, Integer quantity) {
        productsQuantityMap.merge(product, quantity, Integer::sum);
        startTime = System.nanoTime() / 1000000;
        LOG.info("added product " + product.getName() + " with quantity " + quantity);
        this.initialQuantity.getAndAdd(quantity);
    }

    /**
     * randomly sell a product, in a random quantity
     */
    public void sellProducts() {
        List<Product> availableProducts = this.getAvailableProducts();
        if (availableProducts.size() == 0) {
            return;
        }

        Random random = new Random();
        int randomProductIndex = random.nextInt(availableProducts.size());
        Product product = availableProducts.get(randomProductIndex);
        // synchronize on the product we want to sell
        synchronized (product) {
            int oldQuantity = productsQuantityMap.get(product);
            if (oldQuantity == 0) {
                LOG.info("No more products of this type");
                endTime = System.nanoTime() / 1000000;
                StringBuilder time = new StringBuilder();
                float duration =  (endTime - startTime) / 1000;
                String result = String.format("%.3f",duration);
                time.append("Execution time: ").append(result).append(" seconds");
                LOG.info(time);
            }
            // sell a random quantity between 1 and 20, but <= the avaiable stock
            int soldQuantity = Math.min(random.nextInt(20) + 1, oldQuantity);
            int newQuantity = oldQuantity - soldQuantity;
            productsQuantityMap.put(product, newQuantity);
            synchronized (revenue) {
                revenue.set(revenue.get() + (long) product.getPrice() * soldQuantity);
            }
            synchronized (billsList) {
                billsList.add(new Bill(product, soldQuantity));
            }
            StringBuilder productsLog = new StringBuilder();
            productsLog.append(Thread.currentThread().getName()).append(" Client buying ").append(soldQuantity).append("x ").append(product.getName());
            LOG.info(productsLog);
        }
    }

    /**
     * get a list of available products (with available quantity > 0)
     */
    private List<Product> getAvailableProducts() {
        return this.productsQuantityMap.entrySet().stream().filter(p -> p.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public boolean hasAvailableProducts() {
        return this.getAvailableProducts().size() != 0;
    }

    public long getSoldProductsValue() {
        AtomicLong value = new AtomicLong(0);
        synchronized (billsList) {
            billsList.forEach(b -> value.addAndGet(b.getTotalValue()));
        }
        return value.get();
    }


    public long getInitialQuantity() {
        return initialQuantity.get();
    }


    public long getSoldProductsQuantity() {
        AtomicLong value = new AtomicLong(0);
        synchronized (billsList) {
            billsList.forEach(b -> value.addAndGet(b.getTotalQuantity()));
        }
        return value.get();
    }

    public void runInventoryCheck() {
        synchronized (revenue) {
            LOG.info("Performing inventory check..");
            long soldProductsValue = getSoldProductsValue();
            if (soldProductsValue != revenue.get()) {
                LOG.error("Inventory check failed!");
                return;
            }
            LOG.info("Inventory check successful!");
        }
    }

    public void runFinalInventoryCheck() {
        synchronized (revenue) {
            LOG.info("Performing final inventory check..");
            long soldProductsValue = getSoldProductsValue();
            long soldProductsQuantity = getSoldProductsQuantity();
            if (soldProductsValue != revenue.get() || soldProductsQuantity != getInitialQuantity()) {
                LOG.error("Final inventory check failed!");
                return;
            }
            LOG.info("Final inventory check successful!");
        }
    }
}
