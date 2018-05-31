package dk.eatmore.demo.model;

/**
 * Created by ADMIN on 28-12-2016.
 */

public class MyOrders {

    private String order_no;
    private String order_date;
    private String total_to_pay;
    private String discount;
    private String expectedTime;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getTotal_to_pay() {
        return total_to_pay;
    }

    public void setTotal_to_pay(String total_to_pay) {
        this.total_to_pay = total_to_pay;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }
}
