package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/20/2017.
 */
public interface CommunicatingWithRestaurantCallback {
    void communicatingWithRestaurantCallbackSuccess(String success, String tag_string_req);

    void communicatingWithRestaurantCallbackError(VolleyError volleyError);
}
