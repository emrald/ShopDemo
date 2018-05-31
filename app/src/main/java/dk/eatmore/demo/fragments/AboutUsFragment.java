package dk.eatmore.demo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.interfaces.AboutUsCallback;
import dk.eatmore.demo.network.NetworkUtils;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONException;
import org.json.JSONObject;


public class AboutUsFragment extends Fragment implements AboutUsCallback {
    private static final String TAG = "AboutUsFragment";
    private ImageView imgback;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    LinearLayout aboutUsll;
    View view;
    ImageView restorentImage;
    TextView restroName, aboutDescription, phoneNO, address;
    SimpleArcDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.abount_us_new, container, false);
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();
        initToolBar();
        initGesture();
        initView();
        iitDialog();
        getAboutUs();
        return view;
    }

    private void iitDialog() {
        mDialog = new SimpleArcDialog(getActivity());

        mDialog.setCancelable(false);

        ArcConfiguration configuration = new ArcConfiguration(getActivity());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(getString(R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mDialog.setConfiguration(configuration);
    }

    private void getAboutUs() {
        String tag_string_req = "getAboutUs";
        mDialog.show();

        NetworkUtils.callGetAboutUs(tag_string_req, AboutUsFragment.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.ABOUT_US, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.e(TAG, "getAboutUs Response: " + response);
//                mDialog.dismiss();
//
//                try {
//                    JSONObject data = new JSONObject(response);
//                    if (data.getBoolean("status")) {
//                        JSONObject dataObj = data.getJSONObject("data");
//                        String image = data.getString("logo_path") + dataObj.getString("logo");
////                        Picasso.with(getActivity())
////                                .load(image)
//                        //  .placeholder(R.drawable.user_avatar_mainpicture)
//                        //.transform(new CircleTransformation())
////                                .into(restorentImage);
//
////                        if (image != null) {
////                            restorentImage.setVisibility(View.VISIBLE);
////                            restroName.setVisibility(View.GONE);
////                            Picasso.with(getActivity()).load(image)
////                                    .into(restorentImage);
////                        } else {
//                        restorentImage.setVisibility(View.GONE);
//                        restroName.setVisibility(View.VISIBLE);
////                        }
//
//                        restroName.setText(data.getString("restaurant_name"));
//                        phoneNO.setText(dataObj.getString("phone_no"));
//                        address.setText(dataObj.getString("address"));
////                        aboutDescription.setText(Html.fromHtml(dataObj.getString("cms")));
//                        aboutDescription.setText(dataObj.getString("cms"));
//
//                    } else
//                        Toast.makeText(getActivity(), data.getString("msg"), Toast.LENGTH_LONG).show();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mDialog.dismiss();
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
    public void getAboutUsCallbackSuccess(String success) {
        Log.e(TAG, "getAboutUs Response: " + success);
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        try {
            JSONObject data = new JSONObject(success);
            if (data.getBoolean("status")) {
                JSONObject dataObj = data.getJSONObject("data");
                String image = data.getString("logo_path") + dataObj.getString("logo");
//                        Picasso.with(getActivity())
//                                .load(image)
                //  .placeholder(R.drawable.user_avatar_mainpicture)
                //.transform(new CircleTransformation())
//                                .into(restorentImage);

//                        if (image != null) {
//                            restorentImage.setVisibility(View.VISIBLE);
//                            restroName.setVisibility(View.GONE);
//                            Picasso.with(getActivity()).load(image)
//                                    .into(restorentImage);
//                        } else {
                restorentImage.setVisibility(View.GONE);
                restroName.setVisibility(View.VISIBLE);
//                        }

                restroName.setText(data.getString("restaurant_name"));
                phoneNO.setText(dataObj.getString("phone_no"));
                address.setText(dataObj.getString("address"));
//                        aboutDescription.setText(Html.fromHtml(dataObj.getString("cms")));
                aboutDescription.setText(dataObj.getString("cms"));

            } else
                Toast.makeText(getActivity(), data.getString("msg"), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAboutUsCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableAboutUs();
        }
    }

    private void showDialogInternetUnavailableAboutUs() {
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
                getAboutUs();
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

    private void initView() {
        restorentImage = (ImageView) view.findViewById(R.id.restroImage);
        restroName = (TextView) view.findViewById(R.id.restroName);
        address = (TextView) view.findViewById(R.id.abtaddress);
        phoneNO = (TextView) view.findViewById(R.id.phoneNO);
        aboutDescription = (TextView) view.findViewById(R.id.aboutDescription);


    }

    private void initGesture() {
        aboutUsll = (LinearLayout) view.findViewById(R.id.aboutUsll);

        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        aboutUsll.setOnTouchListener(gestureListener);
    }

    private void initToolBar() {
        TextView title = (TextView) view.findViewById(R.id.tootlbar_title);
        title.setText(getResources().getString(R.string.about_us));
        imgback = (ImageView) view.findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
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
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

}
