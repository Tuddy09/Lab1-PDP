import Domain.Inventory;
import Domain.Product;
import Exceptions.ProductNotFound;
import Exceptions.QuantityException;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * The Application class manages an inventory system, simulating the sales process
 * and periodically checking the inventory to ensure its consistency. It allows the user
 * to specify the number of threads and the number of operations each thread performs.
 */
public class Application {
    private Inventory inventory;

    private void generateInventoryStocks() {
        inventory = new Inventory();
        List<String> products = Arrays.asList("Apple", "Banana", "Pear", "Pineapple", "Strawberry");
        List<Double> prices = Arrays.asList(1.5, 2.5, 3.5, 4.5, 5.5);
        List<Integer> quantities = Arrays.asList(10000000, 10000000, 10000000, 10000000, 10000000);
        List<Product> productsList = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product product = new Product(products.get(i), prices.get(i), quantities.get(i));
            productsList.add(product);
        }
        inventory.setProducts(productsList);
    }

    public void start() {
        this.generateInventoryStocks();
        Random random = new Random();
        List<String> products = inventory.getProductNames();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the number of threads:");
        int numberOfThreads = scanner.nextInt();
        System.out.println("Please input how many operations for each thread:");
        int numberOfOperations = scanner.nextInt();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(this::checkInventoryThread, 0, 1, java.util.concurrent.TimeUnit.SECONDS);
        for (int i = 0; i < numberOfOperations; i++) {
            for (int j = 0; j < numberOfThreads; j++) {
                executorService.submit(() -> {
                    Map<String, Integer> sales = new HashMap<>();
                    int firstProduct = random.nextInt(products.size());
                    int secondProduct = random.nextInt(products.size());
                    while (firstProduct == secondProduct) {
                        secondProduct = random.nextInt(products.size());
                    }
                    sales.put(products.get(firstProduct), random.nextInt(3) + 1);
                    sales.put(products.get(secondProduct), random.nextInt(3) + 1);
                    try {
                        inventory.sellProducts(sales);
                    } catch (ProductNotFound | QuantityException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        executorService.shutdown();
        scheduledExecutorService.shutdown();
        try {
            // Wait until all tasks are finished
            if (!executorService.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        inventory.checkInventory();
        System.out.println(inventory.toString());
        System.out.println("Done!");
        scanner.close();
    }

    private void checkInventoryThread() {
        inventory.checkInventory();
    }


    public Application() {

    }
}
