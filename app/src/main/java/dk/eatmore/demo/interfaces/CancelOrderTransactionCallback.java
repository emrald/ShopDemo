package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/23/2017.
 */
public interface CancelOrderTransactionCallback {
    void cancelOrderTransactionCallbackSuccess(String success);

    void cancelOrderTransactionCallbackError(VolleyError volleyError);
}
