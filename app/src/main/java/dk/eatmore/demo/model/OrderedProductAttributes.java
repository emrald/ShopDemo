package dk.eatmore.demo.model;

import java.util.List;

/**
 * Created by sachi on 2/8/2017.
 */

public class OrderedProductAttributes {

    /**
     * opa_id : 712
     * op_id : 520
     * pad_id : 217
     * a_price : 10.00
     * tm_id : 47
     * customer_id : 188
     * restaurant_id : 1
     * attribute_name : Bread
     * attribute_value_name : Weet
     * order_product_extra_topping_group : [{"opt_id":"1029","op_id":"520","tsgd_id":"95","t_price":"5.00","opa_id":"712","customer_id":"188","restaurant_id":"1","ingredient_name":"Garlic"},{"opt_id":"1030","op_id":"520","tsgd_id":"96","t_price":"5.00","opa_id":"712","customer_id":"188","restaurant_id":"1","ingredient_name":"Corn"},{"opt_id":"1031","op_id":"520","tsgd_id":"97","t_price":"5.00","opa_id":"712","customer_id":"188","restaurant_id":"1","ingredient_name":"Olives"}]
     */

    private String opa_id;
    private String op_id;
    private String pad_id;
    private String a_price;
    private String tm_id;
    private String customer_id;
    private String restaurant_id;
    private String attribute_name;
    private String attribute_value_name;

    private List<OrderProductExtraToppingGroupObject> order_product_extra_topping_group;

    public String getOpa_id() {
        return opa_id;
    }

    public void setOpa_id(String opa_id) {
        this.opa_id = opa_id;
    }

    public String getOp_id() {
        return op_id;
    }

    public void setOp_id(String op_id) {
        this.op_id = op_id;
    }

    public String getPad_id() {
        return pad_id;
    }

    public void setPad_id(String pad_id) {
        this.pad_id = pad_id;
    }

    public String getA_price() {
        return a_price;
    }

    public void setA_price(String a_price) {
        this.a_price = a_price;
    }

    public String getTm_id() {
        return tm_id;
    }

    public void setTm_id(String tm_id) {
        this.tm_id = tm_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public String getAttribute_value_name() {
        return attribute_value_name;
    }

    public void setAttribute_value_name(String attribute_value_name) {
        this.attribute_value_name = attribute_value_name;
    }

    public List<OrderProductExtraToppingGroupObject> getOrder_product_extra_topping_group() {
        return order_product_extra_topping_group;
    }

    public void setOrder_product_extra_topping_group(List<OrderProductExtraToppingGroupObject> order_product_extra_topping_group) {
        this.order_product_extra_topping_group = order_product_extra_topping_group;
    }

}
