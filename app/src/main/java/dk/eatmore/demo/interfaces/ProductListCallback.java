package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/14/2017.
 */
public interface ProductListCallback {
    void getProductListCallbackSuccess(String success);

    void getProductListCallbackError(VolleyError volleyError);
}
