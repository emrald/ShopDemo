package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 1/31/2017.
 */
public interface ChangePasswordCallback {
    void changePasswordCallbackSuccess(String success);

    void changePasswordCallbackError(VolleyError volleyError);
}
