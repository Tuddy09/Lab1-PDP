package Domain;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    List<Product> products;
    double TotalPrice;

    public Bill() {
        products = new ArrayList<>();
        TotalPrice = 0;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addMoney(double money) {
        TotalPrice += money;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    @Override
    public String toString() {
        return "Bill has been issued successfully!" + '\n' + "TotalPrice {$" + TotalPrice + "}";
    }
}
