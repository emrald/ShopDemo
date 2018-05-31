package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/21/2017.
 */
public interface VideoLinkHowItWorksCallback {
    void videoLinkCallbackSuccess(String success);

    void videoLinkCallbackError(VolleyError volleyError);
}
