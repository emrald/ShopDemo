package dk.eatmore.demo.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.interfaces.VideoLinkHowItWorksCallback;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import dk.eatmore.demo.network.NetworkUtils;
import com.leo.simplearcloader.SimpleArcDialog;

import org.json.JSONException;
import org.json.JSONObject;

import dk.eatmore.demo.myutils.ApiCall;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by pallavi.b on 01-Jun-16.
 */
public class HowItWorksFragment extends Fragment implements VideoLinkHowItWorksCallback {

    private static final String TAG = HowItWorksFragment.class.getSimpleName();
    private View rootView;
    private VideoView howItWorksVideoView;
    private JCVideoPlayerStandard jcVideoPlayerStandard;
    private ImageView imgback;
    LinearLayout howitworksll;
    private SimpleArcDialog mSimpleArcDialog;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    private MediaController mediaControls;
    private int position = 0;

    private WebView howItWorksView;
    private String howItWorksUrlString = "http://e-point.dk/en/web-view/how-it-works";
    private ProgressDialog progressDialog = null;
    boolean isVideoLinkFetched = false;
    boolean isVideoFetched = false;
    boolean isWebViewContentFetched = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(dk.eatmore.demo.R.layout.how_it_works, container, false);
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();
        bindViews();
//        initMediaPlayer();

        TextView title = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.tootlbar_title);
        title.setText(getResources().getString(dk.eatmore.demo.R.string.how_it_works));
        imgback = (ImageView) rootView.findViewById(dk.eatmore.demo.R.id.imgback);
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
        howitworksll.setOnTouchListener(gestureListener);
        getVideoLink();
        setWebView();
        return rootView;
    }

    private void bindViews() {
        howitworksll = (LinearLayout) rootView.findViewById(dk.eatmore.demo.R.id.howitworksll);
        howItWorksView = (WebView) rootView.findViewById(dk.eatmore.demo.R.id.wv_how_it_works);
        howItWorksVideoView = (VideoView) rootView.findViewById(dk.eatmore.demo.R.id.vv_how_it_works);

//        jcVideoPlayerStandard = (JCVideoPlayerStandard) rootView.findViewById(R.id.vv_how_it_works);
//        jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
//                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
//        jcVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
    }

    private void initMediaPlayer(String videoLink) {
        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(getActivity());
        }

        //initialize the VideoView

//        // create a progress bar while the video file is loading
//        progressDialog = new ProgressDialog(getActivity());
//        // set a title for the progress bar
//        progressDialog.setTitle("JavaCodeGeeks Android Video View Example");
//        // set a message for the progress bar
//        progressDialog.setMessage("Loading...");
//        //set the progress bar not cancelable on users' touch
//        progressDialog.setCancelable(false);
//        // show the progress bar
//        progressDialog.show();

        try {
            //set the uri of the video to be played

//            howItWorksVideoView.setVideoURI(Uri.parse("http://itakeaway.dk//admin//web////docs//how_it_works//denmark.mp4"));
            howItWorksVideoView.setVideoURI(Uri.parse(videoLink));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        howItWorksVideoView.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        howItWorksVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                //set the media controller in the VideoView
                howItWorksVideoView.setMediaController(mediaControls);
                mediaControls.setAnchorView(howItWorksVideoView);
                // close the progress bar and play the video
//                progressDialog.dismiss();
                isVideoLinkFetched = true;
                hideProgressDialog();
                //if we have a position on savedInstanceState, the video playback should start from here
                howItWorksVideoView.seekTo(position);
                if (position == 10) {
                    howItWorksVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    howItWorksVideoView.pause();
                }
            }
        });
    }

    private void getVideoLink() {
        String tag_string_req = "HOW_IT_WORKS_VIDEO";
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mSimpleArcDialog.show();

        NetworkUtils.callGetVideoLinkHowItWorks(tag_string_req, HowItWorksFragment.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.HOW_IT_WORKS_VIDEO, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.e(TAG, "ABOUT_US ABOUT_US: " + response);
//                isVideoLinkFetched = true;
//                hideProgressDialog();
//
//                try {
//                    JSONObject data = new JSONObject(response);
//                    if (data.getBoolean("status")) {
//                        howItWorksVideoView.setVisibility(View.VISIBLE);
//                        String videoLink = data.getString("video_link");
//                        initMediaPlayer(videoLink);
////                        jcVideoPlayerStandard.setUp(videoLink,
////                                JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
//                    } else {
////                        Toast.makeText(getActivity(), data.getString("msg"), Toast.LENGTH_LONG).show();
//                        howItWorksVideoView.setVisibility(View.GONE);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                isVideoLinkFetched = true;
//                hideProgressDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                return params;
//            }
//
//        };
//
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void videoLinkCallbackSuccess(String success) {
        Log.e(TAG, "ABOUT_US ABOUT_US: " + success);
        isVideoLinkFetched = true;
        hideProgressDialog();

        try {
            JSONObject data = new JSONObject(success);
            if (data.getBoolean("status")) {
                howItWorksVideoView.setVisibility(View.VISIBLE);
                String videoLink = data.getString("video_link");
                initMediaPlayer(videoLink);
//                        jcVideoPlayerStandard.setUp(videoLink,
//                                JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
            } else {
//                        Toast.makeText(getActivity(), data.getString("msg"), Toast.LENGTH_LONG).show();
                howItWorksVideoView.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void videoLinkCallbackError(VolleyError volleyError) {
        isVideoLinkFetched = true;
        hideProgressDialog();
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableVideoLink();
        }
    }

    private void showDialogInternetUnavailableVideoLink() {
        final AlertDialog internetAlertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(dk.eatmore.demo.R.layout.dialog_internet_unavailable, null);

        Button okButton = (Button) view.findViewById(R.id.btn_ok_internet);
        Button closeButton = (Button) view.findViewById(R.id.btn_close_internet);
        builder.setView(view);
        internetAlertDialog = builder.create();
        internetAlertDialog.setCancelable(false);
        internetAlertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetAlertDialog.dismiss();
                getVideoLink();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetAlertDialog.dismiss();
                getActivity().finish();
            }
        });
    }

    private void hideProgressDialog() {
        if (isVideoLinkFetched && isWebViewContentFetched)
            if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
                mSimpleArcDialog.dismiss();
    }

    private void setWebView() {
//        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
//        mSimpleArcDialog.show();

        howItWorksView.getSettings().setJavaScriptEnabled(true);
        howItWorksView.setWebChromeClient(new WebChromeClient());
//        howItWorksView.getSettings().setLoadWithOverviewMode(true);
//        howItWorksView.getSettings().setUseWideViewPort(true);
        howItWorksView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        howItWorksView.setScrollbarFadingEnabled(false);
        howItWorksView.loadUrl(ApiCall.HOW_IT_WORKS);
        howItWorksView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                Log.e(TAG, "" + url + ", " + message);
                return super.onJsAlert(view, url, message, result);
            }
        });


//        String postData = "submit=1&user_id=" + userIdString;
//        howItWorksView.postUrl(urlVIPPackage, EncodingUtils.getBytes(postData, "BASE64"));

        howItWorksView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                isWebViewContentFetched = true;
                hideProgressDialog();
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                isWebViewContentFetched = true;
                hideProgressDialog();
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
//                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
//        savedInstanceState.putInt("Position", howItWorksVideoView.getCurrentPosition());
//        howItWorksVideoView.pause();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //we use onRestoreInstanceState in order to play the video playback from the stored position
//        if (savedInstanceState != null) {
//            position = savedInstanceState.getInt("Position");
//            howItWorksVideoView.seekTo(position);
//        }
    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        //we use onRestoreInstanceState in order to play the video playback from the stored position
//        position = savedInstanceState.getInt("Position");
//        howItWorksVideoView.seekTo(position);
//    }
}
