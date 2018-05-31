package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/21/2017.
 */
public interface GetAllPaymentMethodsCallback {
    void getAllPaymentMethodsCallbackSuccess(String success);

    void getAllPaymentMethodsCallbackError(VolleyError volleyError);
}
