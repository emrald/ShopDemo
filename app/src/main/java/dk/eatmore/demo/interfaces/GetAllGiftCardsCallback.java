package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/21/2017.
 */
public interface GetAllGiftCardsCallback {
    void getAllGiftCardsCallbackSuccess(String success);

    void getAllGiftCardsCallbackError(VolleyError volleyError);
}
