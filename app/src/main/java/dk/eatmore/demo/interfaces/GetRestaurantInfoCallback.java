package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/8/2017.
 */

public interface GetRestaurantInfoCallback {
    void getRestaurantInfoCallbackSuccess(String success);

    void getRestaurantInfoCallbackError(VolleyError volleyError);
}
