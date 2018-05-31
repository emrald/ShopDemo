package dk.eatmore.demo.model;

import java.util.List;

/**
 * Created by sachi on 2/8/2017.
 */

public class OrderProductsDetails {
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
    /**
     * p_id : 105
     * product_no : Sandwich01
     * c_id : 192
     * p_name : GrilledCheeseSandwich
     * p_desc :
     * p_image : 201702020402173.png
     * is_attributes : 0
     * p_price : 50.00
     * p_order : null
     * extra_topping_group : 47
     * featured : 0
     * published : 1
     * system : Web
     * action_by : 161
     * action_dt : 2016-10-07 03:43:56
     * is_activated : 1
     * is_deleted : 0
     * restaurant_id : 1
     */

    private ProductsBean products;
    /**
     * opi_id : 310
     * op_id : 519
     * i_id : 34
     * customer_id : 188
     * restaurant_id : 1
     * ingredient_name : Cheese
     */

    private List<RemovedIngredientsObject> removed_ingredients;

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

    public ProductsBean getProducts() {
        return products;
    }

    public void setProducts(ProductsBean products) {
        this.products = products;
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

    public static class ProductsBean {
        private String p_id;
        private String product_no;
        private String c_id;
        private String p_name;
        private String p_desc;
        private String p_image;
        private String is_attributes;
        private String p_price;
        private String p_order;
        private String extra_topping_group;
        private String featured;
        private String published;
        private String system;
        private String action_by;
        private String action_dt;
        private String is_activated;
        private String is_deleted;
        private String restaurant_id;

        public String getP_id() {
            return p_id;
        }

        public void setP_id(String p_id) {
            this.p_id = p_id;
        }

        public String getProduct_no() {
            return product_no;
        }

        public void setProduct_no(String product_no) {
            this.product_no = product_no;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public String getP_desc() {
            return p_desc;
        }

        public void setP_desc(String p_desc) {
            this.p_desc = p_desc;
        }

        public String getP_image() {
            return p_image;
        }

        public void setP_image(String p_image) {
            this.p_image = p_image;
        }

        public String getIs_attributes() {
            return is_attributes;
        }

        public void setIs_attributes(String is_attributes) {
            this.is_attributes = is_attributes;
        }

        public String getP_price() {
            return p_price;
        }

        public void setP_price(String p_price) {
            this.p_price = p_price;
        }

        public String getP_order() {
            return p_order;
        }

        public void setP_order(String p_order) {
            this.p_order = p_order;
        }

        public String getExtra_topping_group() {
            return extra_topping_group;
        }

        public void setExtra_topping_group(String extra_topping_group) {
            this.extra_topping_group = extra_topping_group;
        }

        public String getFeatured() {
            return featured;
        }

        public void setFeatured(String featured) {
            this.featured = featured;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public String getAction_by() {
            return action_by;
        }

        public void setAction_by(String action_by) {
            this.action_by = action_by;
        }

        public String getAction_dt() {
            return action_dt;
        }

        public void setAction_dt(String action_dt) {
            this.action_dt = action_dt;
        }

        public String getIs_activated() {
            return is_activated;
        }

        public void setIs_activated(String is_activated) {
            this.is_activated = is_activated;
        }

        public String getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(String is_deleted) {
            this.is_deleted = is_deleted;
        }

        public String getRestaurant_id() {
            return restaurant_id;
        }

        public void setRestaurant_id(String restaurant_id) {
            this.restaurant_id = restaurant_id;
        }
    }

}
