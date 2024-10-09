package Domain;

import Exceptions.ProductNotFound;
import Exceptions.QuantityException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Inventory {
    List<Product> Products;
    List<Bill> Bills;
    double TotalMoney;
    Lock lock = new ReentrantLock();

    public Inventory() {
        Products = new ArrayList<>();
        Bills = new ArrayList<>();
        TotalMoney = 0;
    }

    public void setProducts(List<Product> Products) {
        this.Products = Products;
    }

    public List<String> getProductNames() {
        return Products.stream().map(Product::getName).toList();
    }

    public Product sellProduct(String productName, int quantity) throws QuantityException, ProductNotFound {
        Product product = Products.stream().filter(p -> p.getName().equals(productName)).findFirst().orElse(null);
        if (product == null) {
            throw new ProductNotFound();
        }
        product.decrementQuantity(quantity);
        return product;
    }

    public void sellProducts(Map<String, Integer> products) throws ProductNotFound, QuantityException {
        Bill newBill = new Bill();
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            Product product = sellProduct(productName, quantity);
            newBill.addProduct(product);
            newBill.addMoney(product.getPrice() * quantity);
        }
        lock.lock();
        try {
            Bills.add(newBill);
            TotalMoney += newBill.getTotalPrice();
        } finally {
            lock.unlock();
        }
    }

    public void checkInventory() {
        double totalMoneyFromBills = 0;
        lock.lock();
        try {
            for (Bill bill : Bills) {
                totalMoneyFromBills += bill.getTotalPrice();
            }
            if (totalMoneyFromBills != TotalMoney) {
                System.out.println("Inventory check was made: money mismatch!");
            } else {
                System.out.println("Inventory check was made: everything is OK!");
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "Products=" + Products +
                ", TotalMoney=" + TotalMoney +
                '}';
    }
}
