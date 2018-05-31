package dk.eatmore.demo.interfaces;

import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;

import dk.eatmore.demo.model.ProductAttributeValue;

/**
 * Created by sachi on 3/24/2017.
 */
public interface ProductExtraToppingsCallback {
    void getProductExtraToppingsCallbackSuccess(JSONObject success, ListView expndnd, TextView ectratopping_ind, int parentPos, ArrayList<ProductAttributeValue> arrayAttribute);

    void getProductExtraToppingsCallbackError(VolleyError volleyError);
}
