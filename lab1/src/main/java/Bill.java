import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Bill {
    private final Map<Product, Integer> productsQuantityMap;

    public Bill(Map<Product, Integer> productsQuantityMap) {
        this.productsQuantityMap = new ConcurrentHashMap<>(productsQuantityMap);
    }

    public Bill(Product product, int desiredQuantity) {
        this.productsQuantityMap = new ConcurrentHashMap<>();
        productsQuantityMap.put(product, desiredQuantity);
    }

    public Long getTotalValue() {
        AtomicLong totalValue = new AtomicLong(0);
        productsQuantityMap.forEach((p, q) -> totalValue.set(totalValue.get() + (long) p.getPrice() * q));
        return totalValue.get();
    }

    public Long getTotalQuantity() {
        AtomicLong totalValue = new AtomicLong(0);
        productsQuantityMap.forEach((p, q) -> totalValue.set(totalValue.get() + q));
        return totalValue.get();
    }
}
