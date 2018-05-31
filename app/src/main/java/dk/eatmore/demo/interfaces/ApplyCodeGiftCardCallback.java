package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/20/2017.
 */
public interface ApplyCodeGiftCardCallback {
    void applyCodeGiftCardCallbackSuccess(String success);

    void applyCodeGiftCardCallbackError(VolleyError volleyError);
}
