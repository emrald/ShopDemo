package dk.eatmore.demo.model;

/**
 * Created by ADMIN on 25-11-2016.
 */

public class PaymentOption {
    String payentImage,payentName,paymentId;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPayentImage() {
        return payentImage;
    }

    public void setPayentImage(String payentImage) {
        this.payentImage = payentImage;
    }

    public String getPayentName() {
        return payentName;
    }

    public void setPayentName(String payentName) {
        this.payentName = payentName;
    }
}
