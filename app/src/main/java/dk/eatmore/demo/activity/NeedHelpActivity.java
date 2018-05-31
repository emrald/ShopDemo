package dk.eatmore.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import com.leo.simplearcloader.SimpleArcDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NeedHelpActivity extends AppCompatActivity {
    private static final String TAG = NeedHelpActivity.class.getSimpleName();
    private ImageView backImageView;
    private TextView titleTextView;
    private WebView webView;
    private SimpleArcDialog mSimpleArcDialog;

    private String googleDocs = "http://docs.google.com/gview?embedded=true&url=";
    private String helpUrlString = ApiCall.BASE_WEBSITE_URL + "web-view/need-help";
    // google docs support url.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(dk.eatmore.demo.R.layout.need_help_activity);
        setToolBar();

        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(this);
        mSimpleArcDialog.show();

        webView = (WebView) findViewById(dk.eatmore.demo.R.id.text);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                Log.e(TAG, "" + url + ", " + message);
                return super.onJsAlert(view, url, message, result);
            }
        });
//        webView.loadUrl(googleDocs + helpUrlString);
        webView.loadUrl(helpUrlString);
        //following lines are to show the loader untile downloading the pdf file for view.
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
                    mSimpleArcDialog.dismiss();
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                try {
                    if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
                        mSimpleArcDialog.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void setToolBar() {
        titleTextView = (TextView) findViewById(dk.eatmore.demo.R.id.tootlbar_title);
        titleTextView.setText(getResources().getString(dk.eatmore.demo.R.string.helps));
        backImageView = (ImageView) findViewById(dk.eatmore.demo.R.id.imgback);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(NeedHelpActivity.this,LoginActivity.class);
//                startActivity(i);
                finish();
            }
        });
//        Api_Call_Post();
    }

    private void Api_Call_Post() {
        String tag_string_req = "forgetPassword";
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(this);
        mSimpleArcDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, ApiCall.NEED_HELP,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);
                            if (data.getBoolean("status"))
                                webView.loadUrl(googleDocs + data.getString("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mSimpleArcDialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                return params;
            }
        };

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
