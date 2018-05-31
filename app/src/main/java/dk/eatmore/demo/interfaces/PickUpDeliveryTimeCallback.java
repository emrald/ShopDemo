package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/20/2017.
 */
public interface PickUpDeliveryTimeCallback {
    void pickUpDeliveryTimeCallbackSuccess(String success, String pick_Type);

    void pickUpDeliveryTimeCallbackError(VolleyError volleyError);
}
