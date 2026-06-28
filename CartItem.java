public class CartItem {

    String name;
    double price;
    int qty;

    public CartItem(String name, double price, int qty) {
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public double getTotal() {
        return price * qty;
    }

    public void increaseQty() {
        qty++;
    }

    public void decreaseQty() {
        if (qty > 1) {
            qty--;
        }
    }
}