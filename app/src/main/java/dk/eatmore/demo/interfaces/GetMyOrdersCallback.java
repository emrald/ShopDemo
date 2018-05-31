package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/21/2017.
 */
public interface GetMyOrdersCallback {
    void getMyOrdersCallbackSuccess(String success);

    void getMyOrdersCallbackError(VolleyError volleyError);
}
