package june.internship;

public class ProductList {

String productId,name,price,unit,description;
int image;
boolean isAddedCart = false;
boolean isAddedWishlist = false;

    public boolean isAddedWishlist() {
        return isAddedWishlist;
    }

    public void setAddedWishlist(boolean addedWishlist) {
        isAddedWishlist = addedWishlist;
    }

    public boolean isAddedCart() {
        return isAddedCart;
    }

    public void setAddedCart(boolean addedCart) {
        isAddedCart = addedCart;
    }

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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
