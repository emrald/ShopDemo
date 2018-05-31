package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/10/2017.
 */
public interface GetCityListCallback {
    void getCityListCallbackSuccess(String success);

    void getCityListCallbackError(VolleyError volleyError);
}
