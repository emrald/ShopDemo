package dk.eatmore.demo.model;

/**
 * Created by sachi on 2/8/2017.
 */

public class OrderProductExtraToppingGroupObject {
    private String opt_id;
    private String op_id;
    private String tsgd_id;
    private String t_price;
    private String opa_id;
    private String customer_id;
    private String restaurant_id;
    private String ingredient_name;

    public String getOpt_id() {
        return opt_id;
    }

    public void setOpt_id(String opt_id) {
        this.opt_id = opt_id;
    }

    public String getOp_id() {
        return op_id;
    }

    public void setOp_id(String op_id) {
        this.op_id = op_id;
    }

    public String getTsgd_id() {
        return tsgd_id;
    }

    public void setTsgd_id(String tsgd_id) {
        this.tsgd_id = tsgd_id;
    }

    public String getT_price() {
        return t_price;
    }

    public void setT_price(String t_price) {
        this.t_price = t_price;
    }

    public String getOpa_id() {
        return opa_id;
    }

    public void setOpa_id(String opa_id) {
        this.opa_id = opa_id;
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

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }
}
