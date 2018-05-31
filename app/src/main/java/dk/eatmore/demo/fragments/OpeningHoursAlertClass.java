package dk.eatmore.demo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import dk.eatmore.demo.adapter.OpeningHourAdaptor;
import dk.eatmore.demo.interfaces.GetOpeningHoursCallback;
import dk.eatmore.demo.model.OpeningHourPojo;
import dk.eatmore.demo.network.NetworkUtils;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by pallavi.b on 02-Jun-16.
 */
public class OpeningHoursAlertClass extends Fragment implements View.OnClickListener, GetOpeningHoursCallback {

    private static final String TAG = OpeningHoursAlertClass.class.getSimpleName();
    private View rootView;
    private ImageView imgback;
    private Button btnOk;
    private RecyclerView mRecyclerView;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    LinearLayout openningHourll;
    List<OpeningHourPojo> data;
    OpeningHourAdaptor adapter;
    SimpleArcDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.opening_hours_alert, container, false);
        if (getActivity() instanceof MainActivity){
            // Hiding Bottom Bar as it is Login screen
            ((MainActivity) getActivity()).hideBottomNavigation();
        }else {
            // If parent Activity is not MainActivity then there is no Bottom Navigation View to hide
        }

        bindViews();

        TextView title = (TextView) rootView.findViewById(R.id.tootlbar_title);
        title.setText(getResources().getString(R.string.opening_hour));

        imgback = (ImageView) rootView.findViewById(R.id.imgback);
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

        mRecyclerView.setOnTouchListener(gestureListener);
        rootView.setOnClickListener(this);
        rootView.setOnTouchListener(gestureListener);
        iitDialog();
        return rootView;
    }

    private void bindViews() {
        openningHourll = (LinearLayout) rootView.findViewById(R.id.openningHourll);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.openinghourlist);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = new ArrayList<>();

        //  init();
        //   setCustomActionBar();//for actionbar

        adapter = new OpeningHourAdaptor(data, getActivity());
        mRecyclerView.setAdapter(adapter);
        setRetainInstance(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getOpeningHour();
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
                    Toast.makeText(getActivity(), "Left Swipe", Toast.LENGTH_SHORT).show();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(getActivity(), "Right Swipe", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
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
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void getOpeningHour() {
        mDialog.show();
        String tag_string_req = "getOpeningHour";
        NetworkUtils.callGetOpeningHours(tag_string_req, OpeningHoursAlertClass.this);
    }

    @Override
    public void getOpeningHoursCallbackSuccess(String success) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        int day;
        OpeningHourPojo openingHourPojo = null;
        //   Log.e("getOpeningHour", "getOpeningHour Response: " + response.toString());
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
                Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_WEEK);

                JSONArray jsonArray = jObj.getJSONArray("Openinghours");
                int sizeOfArray = jsonArray.length();
                for (int i = 0; i < sizeOfArray; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (i == day - 2)
                        openingHourPojo = new OpeningHourPojo(jsonObject.getString("day"),
                                jsonObject.getString("opens") + "   -   " + jsonObject.getString("closes"), true);
                    else
                        openingHourPojo = new OpeningHourPojo(jsonObject.getString("day"),
                                jsonObject.getString("opens") + "   -   " + jsonObject.getString("closes"), false);

                    data.add(openingHourPojo);
                    //    Log.e("Calendar","Calendar"+day);
                }
                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            // hideDialog();
            e.printStackTrace();
        }

    }

    @Override
    public void getOpeningHoursCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        Log.e(TAG, "getOpeningHour Error: " + volleyError.getMessage());
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableOpeningHours();
        }
    }

    private void showDialogInternetUnavailableOpeningHours() {
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
                getOpeningHour();
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
}
