package dk.eatmore.demo.model;

/**
 * Created by sachi on 3/14/2017.
 */

public class ShippingChargeObject {

    /**
     * from_pd : 0
     * to_pd : 5
     * price : 100
     */

    private String from_pd;
    private String to_pd;
    private String price;

    public String getFrom_pd() {
        return from_pd;
    }

    public void setFrom_pd(String from_pd) {
        this.from_pd = from_pd;
    }

    public String getTo_pd() {
        return to_pd;
    }

    public void setTo_pd(String to_pd) {
        this.to_pd = to_pd;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
