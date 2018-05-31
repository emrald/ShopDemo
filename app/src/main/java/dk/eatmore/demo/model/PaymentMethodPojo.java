package dk.eatmore.demo.model;

/**
 * Created by ADMIN on 06-07-2016.
 */
public class PaymentMethodPojo  {
    String paymetTitle;
    String paymentImage,mainImagePath;

    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImagePath(String mainImagePath) {
        this.mainImagePath = mainImagePath;
    }

    public String getPaymetTitle() {
        return paymetTitle;
    }

    public void setPaymetTitle(String paymetTitle) {
        this.paymetTitle = paymetTitle;
    }

    public String getPaymentImage() {
        return paymentImage;
    }

    public void setPaymentImage(String paymentImage) {
        this.paymentImage = paymentImage;
    }
}
