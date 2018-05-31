package dk.eatmore.demo.model;

/**
 * Created by ADMIN on 03-01-2017.
 */

public class ExtraToppingPriceCalculation {

    String price;
    boolean isSelected;

    @Override
    public String toString() {
        return "ExtraToppingPriceCalculation{" +
                "price='" + price + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
