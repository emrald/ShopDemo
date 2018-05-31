package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/21/2017.
 */
public interface ClearCartCallback {
    void clearCartCallbackSuccess(String success);

    void clearCartCallbackError(VolleyError volleyError);
}
