package june.internship;

public class ProductList {

String productId,name,price,unit,description,qty,totalPrice;
int image;
boolean isAddedCart = false;
boolean isAddedWishlist = false;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isAddedCart() {
        return isAddedCart;
    }

    public void setAddedCart(boolean addedCart) {
        isAddedCart = addedCart;
    }

    public boolean isAddedWishlist() {
        return isAddedWishlist;
    }

    public void setAddedWishlist(boolean addedWishlist) {
        isAddedWishlist = addedWishlist;
    }
}
