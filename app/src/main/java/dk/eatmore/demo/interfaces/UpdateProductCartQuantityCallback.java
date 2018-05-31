package dk.eatmore.demo.interfaces;

import android.widget.TextSwitcher;

import com.android.volley.VolleyError;

/**
 * Created by sachi on 3/21/2017.
 */
public interface UpdateProductCartQuantityCallback {
    void updateProductCartQuantityCallbackSuccess(String success, int itemPos, TextSwitcher itemCountText);

    void updateProductCartQuantityCallbackError(VolleyError volleyError);
}
