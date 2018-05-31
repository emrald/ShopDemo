package dk.eatmore.demo.firebase;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by param on 10/19/2016.
 */


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";


    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String token) {
        String tag_string_req = "fcmtoken";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                ApiCall.SET_FCM_TOKEN_ID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "sendRegistrationToServer Response: " + response.toString());



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
                params.put("r_token",ApiCall.R_TOKEN);
                params.put("r_key",ApiCall.R_KEY);
                params.put("token",token);
                params.put("device_type","Android");


                if(ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.KEY_USER_ID)!=null)
                params.put("user_id ",ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                //  Log.e("Forkjfb","njf "+params);
                return params;
            }

        };

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


}