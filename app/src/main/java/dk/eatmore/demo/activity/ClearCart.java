package dk.eatmore.demo.activity;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.myutils.MyPreferenceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ADMIN on 23-12-2016.
 */

public class ClearCart extends IntentService {
    public ClearCart(String name) {
        super(name);
    }
    public ClearCart() {
        super("hello");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        sendRegistrationToServer();


    }
    private void sendRegistrationToServer() {
        String tag_string_req = "fcmtoken";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ApiCall.CLEAR_ALL_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("respo ","clecr "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                if (ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.KEY_USER_ID) != null)
                    params.put("customer_id", ApplicationClass.getInstance().getPrefManager().
                            getStringPreferences(MyPreferenceManager.KEY_USER_ID));

                if (ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID) != null)
                    params.put("ip", ApplicationClass.getInstance().getPrefManager().
                            getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));


                return params;
            }
        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
