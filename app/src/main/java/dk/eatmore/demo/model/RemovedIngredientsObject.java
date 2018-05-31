package dk.eatmore.demo.model;

/**
 * Created by sachi on 2/8/2017.
 */

public class RemovedIngredientsObject {
    /**
     * opt_id : 1025
     * op_id : 519
     * tsgd_id : 93
     * t_price : 10.00
     * opa_id : null
     * customer_id : 188
     * restaurant_id : 1
     * ingredient_name : Kebab
     */
    private String opi_id;
    private String op_id;
    private String i_id;
    private String customer_id;
    private String restaurant_id;
    private String ingredient_name;

    public String getOpi_id() {
        return opi_id;
    }

    public void setOpi_id(String opi_id) {
        this.opi_id = opi_id;
    }

    public String getOp_id() {
        return op_id;
    }

    public void setOp_id(String op_id) {
        this.op_id = op_id;
    }

    public String getI_id() {
        return i_id;
    }

    public void setI_id(String i_id) {
        this.i_id = i_id;
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
