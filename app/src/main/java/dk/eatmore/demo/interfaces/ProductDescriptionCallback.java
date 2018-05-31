package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/14/2017.
 */
public interface ProductDescriptionCallback {
    void getProductDescriptionCallbackSuccess(String success);

    void getProductDescriptionCallbackError(VolleyError volleyError);
}
