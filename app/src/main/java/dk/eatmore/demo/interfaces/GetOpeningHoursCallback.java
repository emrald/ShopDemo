package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/8/2017.
 */

public interface GetOpeningHoursCallback {
    void getOpeningHoursCallbackSuccess(String success);

    void getOpeningHoursCallbackError(VolleyError volleyError);
}
