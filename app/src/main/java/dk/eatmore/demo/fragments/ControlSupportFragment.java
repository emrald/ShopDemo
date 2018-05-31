package dk.eatmore.demo.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlSupportFragment extends Fragment {

    private static final String TAG = ControlSupportFragment.class.getSimpleName();
    private View rootView;
    private ImageView backImageView;
    private LinearLayout controlSupportCell;
    private WebView controlSupportWebView;

    private SimpleArcDialog mSimpleArcDialog = null;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;

    private String controlSupportUrlString = "";

    public ControlSupportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_control_support, container, false);
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();
        bindViews();
        // title.setText(getResources().getString(R.string.terms_services));
        TextView title = (TextView) rootView.findViewById(R.id.tootlbar_title);
        title.setText(getResources().getString(R.string.control_support));

        backImageView = (ImageView) rootView.findViewById(R.id.imgback);
        backImageView.setOnClickListener(new View.OnClickListener() {
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
        controlSupportCell.setOnTouchListener(gestureListener);
        initDialog();
        setWebView();
        return rootView;
    }

    private void bindViews() {
        controlSupportCell = (LinearLayout) rootView.findViewById(R.id.control_support_cell);
        controlSupportWebView = (WebView) rootView.findViewById(R.id.wv_control_support);
    }

    private void setWebView() {
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mSimpleArcDialog.show();

        controlSupportUrlString = ApplicationClass.getInstance().getPrefManager().
                getStringPreferences(MyPreferenceManager.RESTAURANT_HEALTH_REPORT);

        controlSupportWebView.getSettings().setJavaScriptEnabled(true);
        controlSupportWebView.setWebChromeClient(new WebChromeClient());
//        controlSupportWebView.getSettings().setLoadWithOverviewMode(true);
//        controlSupportWebView.getSettings().setUseWideViewPort(true);
        controlSupportWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        controlSupportWebView.setScrollbarFadingEnabled(false);
        controlSupportWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                Log.e(TAG, "" + url + ", " + message);
                return super.onJsAlert(view, url, message, result);
            }
        });

        controlSupportWebView.loadUrl(controlSupportUrlString);

//        String postData = "submit=1&user_id=" + userIdString;
//        controlSupportWebView.postUrl(urlVIPPackage, EncodingUtils.getBytes(postData, "BASE64"));

        controlSupportWebView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        mSimpleArcDialog.dismiss();
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, final String url) {
                        try {
                            mSimpleArcDialog.dismiss();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

        );
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
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

    }

    private void initDialog() {
        mSimpleArcDialog = new SimpleArcDialog(getActivity());

        mSimpleArcDialog.setCancelable(false);

        ArcConfiguration configuration = new ArcConfiguration(getActivity());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(getString(R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mSimpleArcDialog.setConfiguration(configuration);
    }


}
