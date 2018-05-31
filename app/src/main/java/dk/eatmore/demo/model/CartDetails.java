package dk.eatmore.demo.model;

import java.util.List;

/**
 * Created by sachi on 2/14/2017.
 */

public class CartDetails {

    /**
     * op_id : 1050
     * order_no : null
     * customer_id : 188
     * c_id : 193
     * p_id : 262
     * quantity : 1
     * p_price : 150.00
     * discount : null
     * p_date : 2016-10-28 00:48:23
     * restaurant_id : 1
     * ip : 183.87.235.36
     * product_name : Pizza
     * product_no : P78
     * is_attributes : 1
     * product_image : http://itakeaway.dk/backend/web/images/product/201702020402055.png
     * product_image_thumbnail : http://itakeaway.dk/backend/web/images/product/thumbnail/201702020402055.png
     * removed_ingredients : [{"opi_id":"647","op_id":"1050","i_id":"34","customer_id":"188","restaurant_id":"1","ingredient_name":"Cheese"},{"opi_id":"648","op_id":"1050","i_id":"35","customer_id":"188","restaurant_id":"1","ingredient_name":"Onions"},{"opi_id":"649","op_id":"1050","i_id":"36","customer_id":"188","restaurant_id":"1","ingredient_name":"Garlic"},{"opi_id":"650","op_id":"1050","i_id":"37","customer_id":"188","restaurant_id":"1","ingredient_name":"Kebab"},{"opi_id":"651","op_id":"1050","i_id":"38","customer_id":"188","restaurant_id":"1","ingredient_name":"Corn"},{"opi_id":"652","op_id":"1050","i_id":"39","customer_id":"188","restaurant_id":"1","ingredient_name":"Olives"}]
     * ordered_product_attributes : [{"opa_id":"1073","op_id":"1050","pad_id":"217","a_price":"10.00","tm_id":"47","customer_id":"188","restaurant_id":"1","attribute_name":null,"attribute_value_name":"Weet","order_product_extra_topping_group":[{"opt_id":"1736","op_id":"1050","tsgd_id":"95","t_price":"5.00","opa_id":"1073","customer_id":"188","restaurant_id":"1","ingredient_name":"Garlic"},{"opt_id":"1737","op_id":"1050","tsgd_id":"96","t_price":"5.00","opa_id":"1073","customer_id":"188","restaurant_id":"1","ingredient_name":"Corn"},{"opt_id":"1738","op_id":"1050","tsgd_id":"97","t_price":"5.00","opa_id":"1073","customer_id":"188","restaurant_id":"1","ingredient_name":"Olives"}]},{"opa_id":"1074","op_id":"1050","pad_id":"219","a_price":"100.00","tm_id":"45","customer_id":"188","restaurant_id":"1","attribute_name":null,"attribute_value_name":"Large","order_product_extra_topping_group":[{"opt_id":"1739","op_id":"1050","tsgd_id":"89","t_price":"10.00","opa_id":"1074","customer_id":"188","restaurant_id":"1","ingredient_name":"Kebab"},{"opt_id":"1740","op_id":"1050","tsgd_id":"90","t_price":"15.00","opa_id":"1074","customer_id":"188","restaurant_id":"1","ingredient_name":"Onions"}]}]
     */

    private String op_id;
    private String order_no;
    private String customer_id;
    private String c_id;
    private String p_id;
    private String quantity;
    private String p_price;
    private String discount;
    private String p_date;
    private String restaurant_id;
    private String ip;
    private String product_name;
    private String product_no;
    private String is_attributes;
    private String product_image;
    private String product_image_thumbnail;
    /**
     * opi_id : 647
     * op_id : 1050
     * i_id : 34
     * customer_id : 188
     * restaurant_id : 1
     * ingredient_name : Cheese
     */

    private List<RemovedIngredientsObject> removed_ingredients;
    /**
     * opa_id : 1073
     * op_id : 1050
     * pad_id : 217
     * a_price : 10.00
     * tm_id : 47
     * customer_id : 188
     * restaurant_id : 1
     * attribute_name : null
     * attribute_value_name : Weet
     * order_product_extra_topping_group : [{"opt_id":"1736","op_id":"1050","tsgd_id":"95","t_price":"5.00","opa_id":"1073","customer_id":"188","restaurant_id":"1","ingredient_name":"Garlic"},{"opt_id":"1737","op_id":"1050","tsgd_id":"96","t_price":"5.00","opa_id":"1073","customer_id":"188","restaurant_id":"1","ingredient_name":"Corn"},{"opt_id":"1738","op_id":"1050","tsgd_id":"97","t_price":"5.00","opa_id":"1073","customer_id":"188","restaurant_id":"1","ingredient_name":"Olives"}]
     */

    private List<OrderProductExtraToppingGroupObject> order_product_extra_topping_group;

    private List<OrderedProductAttributes> ordered_product_attributes;

    public String getOp_id() {
        return op_id;
    }

    public void setOp_id(String op_id) {
        this.op_id = op_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getP_price() {
        return p_price;
    }

    public void setP_price(String p_price) {
        this.p_price = p_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getP_date() {
        return p_date;
    }

    public void setP_date(String p_date) {
        this.p_date = p_date;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_no() {
        return product_no;
    }

    public void setProduct_no(String product_no) {
        this.product_no = product_no;
    }

    public String getIs_attributes() {
        return is_attributes;
    }

    public void setIs_attributes(String is_attributes) {
        this.is_attributes = is_attributes;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_image_thumbnail() {
        return product_image_thumbnail;
    }

    public void setProduct_image_thumbnail(String product_image_thumbnail) {
        this.product_image_thumbnail = product_image_thumbnail;
    }

    public List<RemovedIngredientsObject> getRemoved_ingredients() {
        return removed_ingredients;
    }

    public void setRemoved_ingredients(List<RemovedIngredientsObject> removed_ingredients) {
        this.removed_ingredients = removed_ingredients;
    }

    public List<OrderProductExtraToppingGroupObject> getOrder_product_extra_topping_group() {
        return order_product_extra_topping_group;
    }

    public void setOrder_product_extra_topping_group(List<OrderProductExtraToppingGroupObject> order_product_extra_topping_group) {
        this.order_product_extra_topping_group = order_product_extra_topping_group;
    }

    public List<OrderedProductAttributes> getOrdered_product_attributes() {
        return ordered_product_attributes;
    }

    public void setOrdered_product_attributes(List<OrderedProductAttributes> ordered_product_attributes) {
        this.ordered_product_attributes = ordered_product_attributes;
    }

//    public static class RemovedIngredientsObject {
//        private String opi_id;
//        private String op_id;
//        private String i_id;
//        private String customer_id;
//        private String restaurant_id;
//        private String ingredient_name;
//
//        public String getOpi_id() {
//            return opi_id;
//        }
//
//        public void setOpi_id(String opi_id) {
//            this.opi_id = opi_id;
//        }
//
//        public String getOp_id() {
//            return op_id;
//        }
//
//        public void setOp_id(String op_id) {
//            this.op_id = op_id;
//        }
//
//        public String getI_id() {
//            return i_id;
//        }
//
//        public void setI_id(String i_id) {
//            this.i_id = i_id;
//        }
//
//        public String getCustomer_id() {
//            return customer_id;
//        }
//
//        public void setCustomer_id(String customer_id) {
//            this.customer_id = customer_id;
//        }
//
//        public String getRestaurant_id() {
//            return restaurant_id;
//        }
//
//        public void setRestaurant_id(String restaurant_id) {
//            this.restaurant_id = restaurant_id;
//        }
//
//        public String getIngredient_name() {
//            return ingredient_name;
//        }
//
//        public void setIngredient_name(String ingredient_name) {
//            this.ingredient_name = ingredient_name;
//        }
//    }

//    public static class OrderProductExtraToppingGroupObject {
//        private String opt_id;
//        private String op_id;
//        private String tsgd_id;
//        private String t_price;
//        private String opa_id;
//        private String customer_id;
//        private String restaurant_id;
//        private String ingredient_name;
//
//        public String getOpt_id() {
//            return opt_id;
//        }
//
//        public void setOpt_id(String opt_id) {
//            this.opt_id = opt_id;
//        }
//
//        public String getOp_id() {
//            return op_id;
//        }
//
//        public void setOp_id(String op_id) {
//            this.op_id = op_id;
//        }
//
//        public String getTsgd_id() {
//            return tsgd_id;
//        }
//
//        public void setTsgd_id(String tsgd_id) {
//            this.tsgd_id = tsgd_id;
//        }
//
//        public String getT_price() {
//            return t_price;
//        }
//
//        public void setT_price(String t_price) {
//            this.t_price = t_price;
//        }
//
//        public String getOpa_id() {
//            return opa_id;
//        }
//
//        public void setOpa_id(String opa_id) {
//            this.opa_id = opa_id;
//        }
//
//        public String getCustomer_id() {
//            return customer_id;
//        }
//
//        public void setCustomer_id(String customer_id) {
//            this.customer_id = customer_id;
//        }
//
//        public String getRestaurant_id() {
//            return restaurant_id;
//        }
//
//        public void setRestaurant_id(String restaurant_id) {
//            this.restaurant_id = restaurant_id;
//        }
//
//        public String getIngredient_name() {
//            return ingredient_name;
//        }
//
//        public void setIngredient_name(String ingredient_name) {
//            this.ingredient_name = ingredient_name;
//        }
//    }

//    public static class OrderedProductAttributes {
//        private String opa_id;
//        private String op_id;
//        private String pad_id;
//        private String a_price;
//        private String tm_id;
//        private String customer_id;
//        private String restaurant_id;
//        private String attribute_name;
//        private String attribute_value_name;
//        /**
//         * opt_id : 1736
//         * op_id : 1050
//         * tsgd_id : 95
//         * t_price : 5.00
//         * opa_id : 1073
//         * customer_id : 188
//         * restaurant_id : 1
//         * ingredient_name : Garlic
//         */
//
//        private List<OrderProductExtraToppingGroupObject> order_product_extra_topping_group;
//
//        public String getOpa_id() {
//            return opa_id;
//        }
//
//        public void setOpa_id(String opa_id) {
//            this.opa_id = opa_id;
//        }
//
//        public String getOp_id() {
//            return op_id;
//        }
//
//        public void setOp_id(String op_id) {
//            this.op_id = op_id;
//        }
//
//        public String getPad_id() {
//            return pad_id;
//        }
//
//        public void setPad_id(String pad_id) {
//            this.pad_id = pad_id;
//        }
//
//        public String getA_price() {
//            return a_price;
//        }
//
//        public void setA_price(String a_price) {
//            this.a_price = a_price;
//        }
//
//        public String getTm_id() {
//            return tm_id;
//        }
//
//        public void setTm_id(String tm_id) {
//            this.tm_id = tm_id;
//        }
//
//        public String getCustomer_id() {
//            return customer_id;
//        }
//
//        public void setCustomer_id(String customer_id) {
//            this.customer_id = customer_id;
//        }
//
//        public String getRestaurant_id() {
//            return restaurant_id;
//        }
//
//        public void setRestaurant_id(String restaurant_id) {
//            this.restaurant_id = restaurant_id;
//        }
//
//        public String getAttribute_name() {
//            return attribute_name;
//        }
//
//        public void setAttribute_name(String attribute_name) {
//            this.attribute_name = attribute_name;
//        }
//
//        public String getAttribute_value_name() {
//            return attribute_value_name;
//        }
//
//        public void setAttribute_value_name(String attribute_value_name) {
//            this.attribute_value_name = attribute_value_name;
//        }
//
//        public List<OrderProductExtraToppingGroupObject> getOrder_product_extra_topping_group() {
//            return order_product_extra_topping_group;
//        }
//
//        public void setOrder_product_extra_topping_group(List<OrderProductExtraToppingGroupObject> order_product_extra_topping_group) {
//            this.order_product_extra_topping_group = order_product_extra_topping_group;
//        }
//
//
//    }
}
