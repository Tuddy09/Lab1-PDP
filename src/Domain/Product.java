package Domain;

import Exceptions.QuantityException;

public class Product {
    String Name;
    double Price;
    int Quantity;

    public Product(String Name, double Price, int Quantity) {
        this.Name = Name;
        this.Price = Price;
        this.Quantity = Quantity;
    }

    public String getName() {
        return Name;
    }

    public double getPrice() {
        return Price;
    }


    synchronized void decrementQuantity(int quantity) throws QuantityException {
        if (quantity > this.Quantity) {
            throw new QuantityException();
        }
        this.Quantity -= quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Name='" + Name + '\'' +
                ", Price=" + Price +
                ", Quantity=" + Quantity +
                '}';
    }
}
