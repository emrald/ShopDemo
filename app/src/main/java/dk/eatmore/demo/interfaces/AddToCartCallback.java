package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by sachi on 3/14/2017.
 */
public interface AddToCartCallback {
    void addToCartCallbackSuccess(JSONObject success);

    void addToCartCallbackError(VolleyError volleyError);
}
