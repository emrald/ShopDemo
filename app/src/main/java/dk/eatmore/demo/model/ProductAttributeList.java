package dk.eatmore.demo.model;

import java.util.ArrayList;

/**
 * Created by ADMIN on 23-09-2016.
 */
public class ProductAttributeList {
    public ArrayList<ProductAttributeValue> getProductAttributeValues() {
        return productAttributeValues;
    }

    public void setProductAttributeValues(ArrayList<ProductAttributeValue> productAttributeValues) {
        this.productAttributeValues = productAttributeValues;
    }

    ArrayList<ProductAttributeValue> productAttributeValues;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title,display_type;

    public String getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(String display_type) {
        this.display_type = display_type;
    }

    @Override
    public String toString() {
        return "ProductAttributeList{" +
                "productAttributeValues=" + productAttributeValues +
                ", title='" + title + '\'' +
                '}';
    }

    public ArrayList<ExtraToppeningCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<ExtraToppeningCategory> categories) {
        this.categories = categories;
    }

    private ArrayList<ExtraToppeningCategory> categories;

}
