package dk.eatmore.demo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pallavi.b on 02-Jun-16.
 */
public class TermsServicesFragment extends Fragment {

    private static final String TAG = TermsServicesFragment.class.getSimpleName();
    private View view;
    private ImageView imgback;
    private LinearLayout termsservicell;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    TextView txtTermsAdService;
    private SimpleArcDialog mDialog;

    private WebView termsWebView;
    private String termsUrlString = ApiCall.BASE_WEBSITE_URL + "web-view/t-o-s";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(dk.eatmore.demo.R.layout.fragment_terms_service, container, false);
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();
        bindViews();
        // title.setText(getResources().getString(R.string.terms_services));
        TextView title = (TextView) view.findViewById(dk.eatmore.demo.R.id.tootlbar_title);
        title.setText(getResources().getString(dk.eatmore.demo.R.string.terms_of_service));

        imgback = (ImageView) view.findViewById(dk.eatmore.demo.R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
            }
        });

        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        termsservicell.setOnTouchListener(gestureListener);

        setWebView();
//        initDialog();
//        getTermsAndCond();
        return view;
    }

    private void bindViews() {
        termsservicell = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.termsservicell);
        txtTermsAdService = (TextView) view.findViewById(dk.eatmore.demo.R.id.text);
        termsWebView = (WebView) view.findViewById(dk.eatmore.demo.R.id.wv_terms_service);
    }

    private void setWebView() {
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();

        termsWebView.getSettings().setJavaScriptEnabled(true);
        termsWebView.setWebChromeClient(new WebChromeClient());
//        termsWebView.getSettings().setLoadWithOverviewMode(true);
//        termsWebView.getSettings().setUseWideViewPort(true);
        termsWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        termsWebView.setScrollbarFadingEnabled(false);
        termsWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                Log.e(TAG, "" + url + ", " + message);
                return super.onJsAlert(view, url, message, result);
            }
        });

        termsWebView.loadUrl(termsUrlString);

//        String postData = "submit=1&user_id=" + userIdString;
//        termsWebView.postUrl(urlVIPPackage, EncodingUtils.getBytes(postData, "BASE64"));

        termsWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (mDialog != null && mDialog.isShowing())
                    mDialog.dismiss();
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                if (mDialog != null && mDialog.isShowing())
                    mDialog.dismiss();
            }
        });
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    // Toast.makeText(getActivity(), "Left Swipe", Toast.LENGTH_SHORT).show();
                }
                //else
//
//                if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    Toast.makeText(getActivity(), "Right Swipe", Toast.LENGTH_SHORT).show();
//                    getActivity().getSupportFragmentManager().popBackStack();
//                }
            } catch (Exception e) {
                // nothing
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    private void initDialog() {
        mDialog = new SimpleArcDialog(getActivity());

        mDialog.setCancelable(false);

        ArcConfiguration configuration = new ArcConfiguration(getActivity());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(getString(dk.eatmore.demo.R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mDialog.setConfiguration(configuration);
    }

    public void getTermsAndCond() {
        mDialog.show();

        String tag_string_req = "storePayment";
        StringRequest strReq = new StringRequest(Request.Method.POST, ApiCall.GET_TERMS_AND_CONDITION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        Log.e("storePayment", "storePayment Response: " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean status = jObj.getBoolean("status");
                            // Check for error node in json
                            if (status) {
                                txtTermsAdService.setText(Html.fromHtml(jObj.getString("data")));
                            }

                        } catch (JSONException e) {
                            // hideDialog();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        Log.e("storePayment", "storePayment Error: " + error.getMessage());
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
