package dk.eatmore.demo.netutils;

import org.json.JSONObject;

/**
 * Created by pallavi.b on 9/10/2015.
 */
public interface ApiCallBack {
    public void onEventCompleted(JSONObject success);
    public void onEventFailure();
}
