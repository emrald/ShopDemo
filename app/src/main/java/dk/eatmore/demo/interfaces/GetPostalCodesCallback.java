package dk.eatmore.demo.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/10/2017.
 */
public interface GetPostalCodesCallback {
    void getPostalCodesCallbackSuccess(String success);

    void getPostalCodesCallbackError(VolleyError volleyError);
}
