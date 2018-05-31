package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/14/2017.
 */
public interface SendRegistrationToServerCallback {
    void sendRegistrationToServerCallbackSuccess(String success);

    void sendRegistrationToServerCallbackError(VolleyError volleyError);
}
