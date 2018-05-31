package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/21/2017.
 */
public interface ContactUsSettingsCallback {
    void contactUsSettingsCallbackSuccess(String success);

    void contactUsSettingsCallbackError(VolleyError volleyError);
}
