package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by sachi on 3/20/2017.
 */
public interface CheckoutCallback {
    void checkoutCallbackSuccess(JSONObject success);

    void checkoutCallbackError(VolleyError volleyError);
}
