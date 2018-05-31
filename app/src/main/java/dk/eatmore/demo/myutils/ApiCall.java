package dk.eatmore.demo.myutils;

/**
 * Created by ADMIN on 22-08-2016.
 */
public class ApiCall {

//    public static String R_TOKEN_TEMP = "aFOFmxiPyk6CIcYOfK3-rzRGoVFgooSl25102016011117";
//    public static String R_KEY_TEMP = "NhIO-cJbldiAXIAKKgiHpek1gwVcqHmd25102016011117";


    //rns
    public static String R_TOKEN = "w5oRqFiAXTBB3hwpixAORbg_BwUj0EMQ07042017114812";
    public static String R_KEY = "fcARlrbZFXYee1W6eYEIA0VRlw7MgV4o07042017114812";


    //    private static String BASE_URL = "http://e-point.dk/api/web/";\
//    private static String BASE_URL = "http://itakeaway.dk/api/web/";
    private static String BASE_URL = "http://eatmore.dk/api/web/";
//    private static String BASE_URL = "http://itakeaway.dk/api/web/";

    //    public static String BASE_WEBSITE_URL = "http://itakeaway.dk/";
    public static String BASE_WEBSITE_URL = "http://eatmore.dk/";

    public static String GET_All_TOPPING = BASE_URL + "Extratoppings/toppinggroup/view_record";

    public static String USER_SIGNUP = BASE_URL + "Enduser/enduser/create_record";
    public static String POSTAL_CODE_LIST = BASE_URL + "Enduser/enduser/postal-code-list";
    //  public static String  KOMMUNE_LIST=BASE_URL+"Enduser/enduser/kommune-list";
    public static String CITY_LIST = BASE_URL + "Enduser/enduser/city-list";
    public static String USER_LOGIN = BASE_URL + "Enduser/enduser/login";
    public static String FORGOT_PASSWORD = BASE_URL + "Enduser/enduser/forgot-password";
    public static String GET_USER_PROFILE = BASE_URL + "Enduser/enduser/view_record";
    public static String EDIT_PROFILE = BASE_URL + "Enduser/enduser/update_record";
    public static String GET_CONTACT_SETTING = BASE_URL + "Enduser/enduser/contact-us-details";
    public static String CONTACT_US = BASE_URL + "Enduser/enduser/contact-us";
    public static String ABOUT_US = BASE_URL + "Enduser/enduser/about-us";
    public static String CHANGE_PASSWORD = BASE_URL + "Enduser/enduser/change-password";
    public static String DELETE_ACCOUNT = BASE_URL + "Enduser/enduser/delete_record";
    public static String MENU_LIST = BASE_URL + "Category/category/menu";
    public static String SEARCH_PRODUCT = BASE_URL + "Product/productmaster/product-search";
    public static String PRODUCT_DETAIL_BY_ID = BASE_URL + "Product/productmaster/product-details";

    //  public static String EXTRA_TOPPING_DETAILS=BASE_URL+"Extratoppings/toppinggroup/product_extratopping_view";
    public static String GET_OPENIG_HOUR = BASE_URL + "Openinghours/openinghours/all_record";
    public static String SET_FCM_TOKEN_ID = BASE_URL + "Enduser/enduser/device-token";
    public static String GET_TERMS_AND_CONDITION = BASE_URL + "Enduser/enduser/t-o-s";
    public static String GET_GIFT_CARDS = BASE_URL + "Enduser/enduser/display-gift-cards";
    public static String GET_PAYMENT_METHD = BASE_URL + "RestaurantPaymentMethod/restaurant-payment-method/view_record";
    public static String ADD_TO_CART = BASE_URL + "Cart/cart/addtocart";


    // public static String EXTRATOPPING_DETAILS_NEW= BASE_URL+"Extratoppings/toppinggroup/multiple_product_extratopping_view";

    public static String EXTRATOPPING_DETAILS = BASE_URL + "Extratoppings/toppinggroup/product_extratopping_views";

    public static String VIEW_ALL_CART_ITEM = BASE_URL + "Cart/cart/viewcart";

    public static String DELETE_CART_ITEM = BASE_URL + "Cart/cart/deletefromcart";

    public static String MY_ORDER_DETAILS = BASE_URL + "Cart/cart/get_userprofile_pdtimes_restpaymethods";

    public static String RESTAURANT_PD_TIME = BASE_URL + "Cart/cart/restpickupdeltime";

    public static String CART_CHECKOUT = BASE_URL + "Cart/cart/checkout";

    public static String PRODUCT_QTY = BASE_URL + "Cart/cart/productqty";

    public static String CLEAR_ALL_CART = BASE_URL + "Cart/cart/clearcart";

    public static String VIEW_ORDERS = BASE_URL + "Cart/cart/vieworders";

    public static String VIEW_PARTICULAR_ORDER = BASE_URL + "Cart/cart/vieworder";

//    public static String SMS_ORDER = BASE_URL + "Cart/cart/smsorder";

    public static String ORDER_TRANSACTION = BASE_URL + "Cart/cart/ordertransaction";

    public static String CANCEL_ORDER_TRANSACTION = BASE_URL + "Cart/cart/cancelordertransaction";

//    public static String GET_SHIPPING_COST = BASE_URL + "Cart/cart/shipping_method";

    public static String GET_NEW_SHIPPING_COST = BASE_URL + "PosOrder/order/calculate-shipping-method";

//    public static String COUPONS_REVEAL = BASE_URL + "Cart/cart/cartreveal";
    public static String APPLY_CODE = BASE_URL + "Cart/cart/apply-code";
    public static String NEED_HELP = BASE_URL + "Enduser/enduser/need-help";

    public static String COMMUNICATING_WITH_RESTRO = BASE_URL + "Cart/cart/order-accept-reject-response";
//    public static String NEW_COMMUNICATING_WITH_RESTRO = BASE_URL + "Cart/cart/accept-reject-response";

    public static String RESTAURANT_CLOSED = BASE_URL + "Enduser/enduser/restaurant-closed";

    public static String FACEBOOK_SIGN_UP = BASE_URL + "Enduser/enduser/sign-up-with-facebook";

    public static String HOW_IT_WORKS = BASE_WEBSITE_URL + "web-view/how-it-works-content";

    public static String HOW_IT_WORKS_VIDEO = BASE_URL + "Enduser/enduser/how-it-works";
}