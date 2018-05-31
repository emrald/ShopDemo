package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/10/2017.
 */
public interface FacebookLoginCallback {
    void facebookLoginCallbackSuccess(String success);

    void facebookLoginCallbackError(VolleyError volleyError);
}
