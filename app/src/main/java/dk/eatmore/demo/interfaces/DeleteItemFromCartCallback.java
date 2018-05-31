package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/20/2017.
 */
public interface DeleteItemFromCartCallback {
    void deleteItemFromCartCallbackSuccess(String success, String opId);

    void deleteItemFromCartCallbackError(VolleyError volleyError);
}
