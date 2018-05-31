package dk.eatmore.demo.model;

/**
 * Created by ADMIN on 09-09-2016.
 */
public class MenuItemPojo {
    boolean isRestaurantClosed;

    public boolean isRestaurantClosed() {
        return isRestaurantClosed;
    }

    public void setRestaurantClosed(boolean restaurantClosed) {
        isRestaurantClosed = restaurantClosed;
    }

    String productIngredients;
    String produt_no;

    public String getProductIngredients() {
        return productIngredients;
    }

    public void setProductIngredients(String productIngredients) {
        this.productIngredients = productIngredients;
    }

    public String getProdut_no() {
        return produt_no;
    }

    public void setProdut_no(String produt_no) {
        this.produt_no = produt_no;
    }

    public String getExtra_topping_group() {
        return extra_topping_group;
    }

    public void setExtra_topping_group(String extra_topping_group) {
        this.extra_topping_group = extra_topping_group;
    }

    private String extra_topping_group;
    private String produtId;
    private String categoryId;
    private String productName;
    private String productDesc;
    private String prductImage;
    private String productPrice;
    private String isAttribute;
    private String productImageMainUrl;
    private String productThumbnailMainUrl;

    public String getIsAttribute() {
        return isAttribute;
    }

    public void setIsAttribute(String isAttribute) {
        this.isAttribute = isAttribute;
    }

    public String getProdutId() {
        return produtId;
    }

    public void setProdutId(String produtId) {
        this.produtId = produtId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getPrductImage() {
        return prductImage;
    }

    public void setPrductImage(String prductImage) {
        this.prductImage = prductImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImageMainUrl() {
        return productImageMainUrl;
    }

    public void setProductImageMainUrl(String productImageMainUrl) {
        this.productImageMainUrl = productImageMainUrl;
    }

    public String getProductThumbnailMainUrl() {
        return productThumbnailMainUrl;
    }

    public void setProductThumbnailMainUrl(String productThumbnailMainUrl) {
        this.productThumbnailMainUrl = productThumbnailMainUrl;
    }
}
