package dk.eatmore.demo.model;

import java.util.List;

/**
 * Created by sachi on 2/8/2017.
 */

public class OrderDetailsObject {

    /**
     * order_no : 62
     * customer_id : 188
     * first_name : Aniket
     * last_name : Patel
     * telephone_no : 8128886998
     * address : street 1130 København K
     * order_total : 225.00
     * order_date : 2017-02-06 16:54:00
     * order_status : Accepted
     * payment_status : Not Paid
     * system : Web
     * shipping : Pickup
     * expected_time : 2017-02-06 05:30:00
     * ip : 192.168.1.23
     * restaurant_id : 1
     * comments : null
     * paymethod : 40
     * accept_reject_time : null
     * reject_reason : null
     * pickup_delivery_time : null
     * pos_door_step_discount : null
     * discount_type : null
     * discount_id : null
     * discount_amount : null
     * shipping_costs : null
     * vat : 45.00
     * sub_total : 180.00
     * total_to_pay : 225.00
     * accept_tc : 0
     * sms_order_text : #62*62*Pickup;05:30 06-02-2017 ;As soon as possible;BrDr.Price;*1;GrilledCheeseSandwich;75.00;-Cheese,-Onions,-Garlic,-Olives,;+Kebab,+Onions,+Garlic,+Corn,;1;Pizza;150.00;-Cheese,-Onions,-Garlic,-Kebab,-Corn,-Olives,;Weet,+Garlic,+Corn,+Olives,Large,+Kebab,+Onions,;*225.00;0;0;225.00;*Not Paid;;*Aniket Patel;street 1130 København K;8128886998;;*0x0D0x0A
     * cardno : null
     * txnid : null
     * txnfee : null
     * order_year : 2017
     * order_month : February
     * order_month_date : 2017-02-06
     * is_deleted : 0
     * requirement : asap
     * order_products_details : [{"op_id":"519","order_no":"62","customer_id":"188","c_id":"192","p_id":"105","quantity":"1","p_price":"75.00","discount":null,"p_date":"2016-10-07 15:13:56","restaurant_id":"1","ip":"192.168.1.23","products":{"p_id":"105","product_no":"Sandwich01","c_id":"192","p_name":"GrilledCheeseSandwich","p_desc":"","p_image":"201702020402173.png","is_attributes":"0","p_price":"50.00","p_order":null,"extra_topping_group":"47","featured":"0","published":"1","system":"Web","action_by":"161","action_dt":"2016-10-07 03:43:56","is_activated":"1","is_deleted":"0","restaurant_id":"1"},"removed_ingredients":[{"opi_id":"310","op_id":"519","i_id":"34","customer_id":"188","restaurant_id":"1","ingredient_name":"Cheese"},{"opi_id":"311","op_id":"519","i_id":"35","customer_id":"188","restaurant_id":"1","ingredient_name":"Onions"},{"opi_id":"312","op_id":"519","i_id":"36","customer_id":"188","restaurant_id":"1","ingredient_name":"Garlic"},{"opi_id":"313","op_id":"519","i_id":"39","customer_id":"188","restaurant_id":"1","ingredient_name":"Olives"}],"order_product_extra_topping_group":[{"opt_id":"1025","op_id":"519","tsgd_id":"93","t_price":"10.00","opa_id":null,"customer_id":"188","restaurant_id":"1","ingredient_name":"Kebab"},{"opt_id":"1026","op_id":"519","tsgd_id":"94","t_price":"5.00","opa_id":null,"customer_id":"188","restaurant_id":"1","ingredient_name":"Onions"},{"opt_id":"1027","op_id":"519","tsgd_id":"95","t_price":"5.00","opa_id":null,"customer_id":"188","restaurant_id":"1","ingredient_name":"Garlic"},{"opt_id":"1028","op_id":"519","tsgd_id":"96","t_price":"5.00","opa_id":null,"customer_id":"188","restaurant_id":"1","ingredient_name":"Corn"}]},{"op_id":"520","order_no":"62","customer_id":"188","c_id":"193","p_id":"262","quantity":"1","p_price":"150.00","discount":null,"p_date":"2016-10-28 12:18:23","restaurant_id":"1","ip":"192.168.1.23","products":{"p_id":"262","product_no":"P78","c_id":"193","p_name":"Pizza","p_desc":"PizzaPizza Pizza Pizza Pizza Pizza","p_image":"201702020402055.png","is_attributes":"1","p_price":"150.00","p_order":null,"extra_topping_group":null,"featured":"0","published":"1","system":"Web","action_by":"161","action_dt":"2016-10-28 00:48:23","is_activated":"1","is_deleted":"0","restaurant_id":"1"},"removed_ingredients":[{"opi_id":"314","op_id":"520","i_id":"34","customer_id":"188","restaurant_id":"1","ingredient_name":"Cheese"},{"opi_id":"315","op_id":"520","i_id":"35","customer_id":"188","restaurant_id":"1","ingredient_name":"Onions"},{"opi_id":"316","op_id":"520","i_id":"36","customer_id":"188","restaurant_id":"1","ingredient_name":"Garlic"},{"opi_id":"317","op_id":"520","i_id":"37","customer_id":"188","restaurant_id":"1","ingredient_name":"Kebab"},{"opi_id":"318","op_id":"520","i_id":"38","customer_id":"188","restaurant_id":"1","ingredient_name":"Corn"},{"opi_id":"319","op_id":"520","i_id":"39","customer_id":"188","restaurant_id":"1","ingredient_name":"Olives"}],"ordered_product_attributes":[{"opa_id":"712","op_id":"520","pad_id":"217","a_price":"10.00","tm_id":"47","customer_id":"188","restaurant_id":"1","attribute_name":"Bread","attribute_value_name":"Weet","order_product_extra_topping_group":[{"opt_id":"1029","op_id":"520","tsgd_id":"95","t_price":"5.00","opa_id":"712","customer_id":"188","restaurant_id":"1","ingredient_name":"Garlic"},{"opt_id":"1030","op_id":"520","tsgd_id":"96","t_price":"5.00","opa_id":"712","customer_id":"188","restaurant_id":"1","ingredient_name":"Corn"},{"opt_id":"1031","op_id":"520","tsgd_id":"97","t_price":"5.00","opa_id":"712","customer_id":"188","restaurant_id":"1","ingredient_name":"Olives"}]},{"opa_id":"713","op_id":"520","pad_id":"219","a_price":"100.00","tm_id":"45","customer_id":"188","restaurant_id":"1","attribute_name":"Size","attribute_value_name":"Large","order_product_extra_topping_group":[{"opt_id":"1032","op_id":"520","tsgd_id":"89","t_price":"10.00","opa_id":"713","customer_id":"188","restaurant_id":"1","ingredient_name":"Kebab"},{"opt_id":"1033","op_id":"520","tsgd_id":"90","t_price":"15.00","opa_id":"713","customer_id":"188","restaurant_id":"1","ingredient_name":"Onions"}]}]}]
     */

    private String order_no;
    private String customer_id;
    private String first_name;
    private String last_name;
    private String telephone_no;
    private String address;
    private String order_total;
    private String order_date;
    private String order_status;
    private String payment_status;
    private String system;
    private String shipping;
    private String expected_time;
    private String ip;
    private String restaurant_id;
    private String comments;
    private String paymethod;
    private String accept_reject_time;
    private String reject_reason;
    private String pickup_delivery_time;
    private String pos_door_step_discount;
    private String discount_type;
    private String discount_id;
    private String discount_amount;
    private String shipping_costs;
    private String vat;
    private String sub_total;
    private String total_to_pay;
    private String accept_tc;
    private String sms_order_text;
    private String cardno;
    private String txnid;
    private String txnfee;
    private String order_year;
    private String order_month;
    private String order_month_date;
    private String is_deleted;
    private String requirement;
    /**
     * op_id : 519
     * order_no : 62
     * customer_id : 188
     * c_id : 192
     * p_id : 105
     * quantity : 1
     * p_price : 75.00
     * discount : null
     * p_date : 2016-10-07 15:13:56
     * restaurant_id : 1
     * ip : 192.168.1.23
     * products : {"p_id":"105","product_no":"Sandwich01","c_id":"192","p_name":"GrilledCheeseSandwich","p_desc":"","p_image":"201702020402173.png","is_attributes":"0","p_price":"50.00","p_order":null,"extra_topping_group":"47","featured":"0","published":"1","system":"Web","action_by":"161","action_dt":"2016-10-07 03:43:56","is_activated":"1","is_deleted":"0","restaurant_id":"1"}
     * removed_ingredients : [{"opi_id":"310","op_id":"519","i_id":"34","customer_id":"188","restaurant_id":"1","ingredient_name":"Cheese"},{"opi_id":"311","op_id":"519","i_id":"35","customer_id":"188","restaurant_id":"1","ingredient_name":"Onions"},{"opi_id":"312","op_id":"519","i_id":"36","customer_id":"188","restaurant_id":"1","ingredient_name":"Garlic"},{"opi_id":"313","op_id":"519","i_id":"39","customer_id":"188","restaurant_id":"1","ingredient_name":"Olives"}]
     * order_product_extra_topping_group : [{"opt_id":"1025","op_id":"519","tsgd_id":"93","t_price":"10.00","opa_id":null,"customer_id":"188","restaurant_id":"1","ingredient_name":"Kebab"},{"opt_id":"1026","op_id":"519","tsgd_id":"94","t_price":"5.00","opa_id":null,"customer_id":"188","restaurant_id":"1","ingredient_name":"Onions"},{"opt_id":"1027","op_id":"519","tsgd_id":"95","t_price":"5.00","opa_id":null,"customer_id":"188","restaurant_id":"1","ingredient_name":"Garlic"},{"opt_id":"1028","op_id":"519","tsgd_id":"96","t_price":"5.00","opa_id":null,"customer_id":"188","restaurant_id":"1","ingredient_name":"Corn"}]
     */

    private List<OrderProductsDetails> order_products_details;

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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getTelephone_no() {
        return telephone_no;
    }

    public void setTelephone_no(String telephone_no) {
        this.telephone_no = telephone_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getExpected_time() {
        return expected_time;
    }

    public void setExpected_time(String expected_time) {
        this.expected_time = expected_time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    public String getAccept_reject_time() {
        return accept_reject_time;
    }

    public void setAccept_reject_time(String accept_reject_time) {
        this.accept_reject_time = accept_reject_time;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public String getPickup_delivery_time() {
        return pickup_delivery_time;
    }

    public void setPickup_delivery_time(String pickup_delivery_time) {
        this.pickup_delivery_time = pickup_delivery_time;
    }

    public String getPos_door_step_discount() {
        return pos_door_step_discount;
    }

    public void setPos_door_step_discount(String pos_door_step_discount) {
        this.pos_door_step_discount = pos_door_step_discount;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(String discount_id) {
        this.discount_id = discount_id;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getShipping_costs() {
        return shipping_costs;
    }

    public void setShipping_costs(String shipping_costs) {
        this.shipping_costs = shipping_costs;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getTotal_to_pay() {
        return total_to_pay;
    }

    public void setTotal_to_pay(String total_to_pay) {
        this.total_to_pay = total_to_pay;
    }

    public String getAccept_tc() {
        return accept_tc;
    }

    public void setAccept_tc(String accept_tc) {
        this.accept_tc = accept_tc;
    }

    public String getSms_order_text() {
        return sms_order_text;
    }

    public void setSms_order_text(String sms_order_text) {
        this.sms_order_text = sms_order_text;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getTxnfee() {
        return txnfee;
    }

    public void setTxnfee(String txnfee) {
        this.txnfee = txnfee;
    }

    public String getOrder_year() {
        return order_year;
    }

    public void setOrder_year(String order_year) {
        this.order_year = order_year;
    }

    public String getOrder_month() {
        return order_month;
    }

    public void setOrder_month(String order_month) {
        this.order_month = order_month;
    }

    public String getOrder_month_date() {
        return order_month_date;
    }

    public void setOrder_month_date(String order_month_date) {
        this.order_month_date = order_month_date;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public List<OrderProductsDetails> getOrder_products_details() {
        return order_products_details;
    }

    public void setOrder_products_details(List<OrderProductsDetails> order_products_details) {
        this.order_products_details = order_products_details;
    }

}
