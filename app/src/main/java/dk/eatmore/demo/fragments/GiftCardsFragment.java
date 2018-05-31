package dk.eatmore.demo.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.adapter.NewGiftCardsAdapter;
import dk.eatmore.demo.interfaces.GetAllGiftCardsCallback;
import dk.eatmore.demo.interfaces.GiftCardsAdapterListener;
import dk.eatmore.demo.model.GiftCardPojo;
import dk.eatmore.demo.myutils.Utility;
import dk.eatmore.demo.network.NetworkUtils;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GiftCardsFragment extends Fragment implements View.OnClickListener, GiftCardsAdapterListener, GetAllGiftCardsCallback {

    private static String TAG = GiftCardsFragment.class.getSimpleName();
    private View rootView;
    private TextView titleTextView;
    private ImageView backImageView;
    private RecyclerView giftCardsRecyclerView;
    private RelativeLayout errorLayout;
    private SimpleArcDialog mSimpleArcDialog;

    private ArrayList<GiftCardPojo> giftCardArray = new ArrayList<>();
    private NewGiftCardsAdapter mAdapter = null;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;

    public GiftCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(dk.eatmore.demo.R.layout.fragment_gift_cards, container, false);
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();
        setToolBar();
        bindViews();
        setGestureDetect();

        initDialog();
        getAllGiftCards();
        return rootView;
    }

    private void setToolBar() {
        titleTextView = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.tootlbar_title);
        titleTextView.setText(getResources().getString(dk.eatmore.demo.R.string.giftcards));
        backImageView = (ImageView) rootView.findViewById(dk.eatmore.demo.R.id.imgback);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bindViews() {
        giftCardsRecyclerView = (RecyclerView) rootView.findViewById(dk.eatmore.demo.R.id.rv_gift_cards);
        mAdapter = new NewGiftCardsAdapter(getActivity(), GiftCardsFragment.this);
        mAdapter.setArrayList(giftCardArray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        giftCardsRecyclerView.setLayoutManager(mLayoutManager);
        giftCardsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        giftCardsRecyclerView.setAdapter(mAdapter);


        errorLayout = (RelativeLayout) rootView.findViewById(dk.eatmore.demo.R.id.error_layout);
        errorLayout.setVisibility(View.GONE);

        TextView txt_error_message = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.txt_error_message);
        txt_error_message.setText(dk.eatmore.demo.R.string.no_gift_cards);
    }

    private void setGestureDetect() {
        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };

        rootView.setOnClickListener(this);
        rootView.setOnTouchListener(gestureListener);
    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void buttonOnClick(View v, int position) {
        switch (v.getId()) {
            case dk.eatmore.demo.R.id.btn_copy_gift_code:
                String textToCopy = giftCardArray.get(position).getGiftCardCode();
                Utility.copyToClipboard(getActivity(), null, textToCopy);
                break;
        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // Toast.makeText(getActivity(), "Left Swipe", Toast.LENGTH_SHORT).show();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //    Toast.makeText(getActivity(), "Right Swipe", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    public void getAllGiftCards() {
        mSimpleArcDialog.show();

        String tag_string_req = "storePayment";

        NetworkUtils.callGetAllGiftCards(tag_string_req, GiftCardsFragment.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST, ApiCall.GET_GIFT_CARDS,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
//                            mSimpleArcDialog.dismiss();
//                        Log.e(TAG, "storePayment Response: " + response);
//                        try {
//                            JSONObject jObj = new JSONObject(response);
//                            boolean status = jObj.getBoolean("status");
//                            // Check for error node in json
//                            if (status) {
//                                JSONArray jsonArray = jObj.getJSONArray("data");
//                                int size = jsonArray.length();
//                                if (size > 0) {
//                                    errorLayout.setVisibility(View.GONE);
//                                    for (int i = 0; i < size; i++) {
//                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                        GiftCardPojo giftCardPojo = new GiftCardPojo();
//                                        giftCardPojo.setGiftCardCode(jsonObject.getString("code"));
//                                        giftCardPojo.setGiftExpDate(jsonObject.getString("expiry_date"));
//                                        giftCardPojo.setGiftvalue(jsonObject.getString("value"));
//                                        if (jsonObject.has("type"))
//                                            giftCardPojo.setGiftType(jsonObject.getString("type"));
//                                        giftCardPojo.setGiftSaldo("0");
//                                        giftCardPojo.setBalance(jsonObject.getString("balance"));
//                                        giftCardArray.add(giftCardPojo);
//                                    }
//                                } else {
//                                    errorLayout.setVisibility(View.VISIBLE);
//                                }
//                            } else {
//                                errorLayout.setVisibility(View.VISIBLE);
//                            }
//                            mAdapter.notifyDataSetChanged();
//                        } catch (JSONException e) {
//                            // hideDialog();
//                            Log.e("", "exp " + e);
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
//                            mSimpleArcDialog.dismiss();
//                        Log.e("storePayment", "storePayment Error: " + error.getMessage());
//                    }
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                params.put("user_id", ApplicationClass.getInstance().getPrefManager()
//                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void getAllGiftCardsCallbackSuccess(String success) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e(TAG, "storePayment Response: " + success);
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
                JSONArray jsonArray = jObj.getJSONArray("data");
                int size = jsonArray.length();
                if (size > 0) {
                    errorLayout.setVisibility(View.GONE);
                    for (int i = 0; i < size; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        GiftCardPojo giftCardPojo = new GiftCardPojo();
                        giftCardPojo.setGiftCardCode(jsonObject.getString("code"));
                        giftCardPojo.setGiftExpDate(jsonObject.getString("expiry_date"));
                        giftCardPojo.setGiftvalue(jsonObject.getString("value"));
                        if (jsonObject.has("type"))
                            giftCardPojo.setGiftType(jsonObject.getString("type"));
                        giftCardPojo.setGiftSaldo("0");
                        giftCardPojo.setBalance(jsonObject.getString("balance"));
                        giftCardArray.add(giftCardPojo);
                    }
                } else {
                    errorLayout.setVisibility(View.VISIBLE);
                }
            } else {
                errorLayout.setVisibility(View.VISIBLE);
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            // hideDialog();
            Log.e("", "exp " + e);
            e.printStackTrace();
        }

    }

    @Override
    public void getAllGiftCardsCallbackError(VolleyError volleyError) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e("storePayment", "storePayment Error: " + volleyError.getMessage());
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableGiftCards();
        }
    }

    private void showDialogInternetUnavailableGiftCards() {
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
                getAllGiftCards();
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

    private void initDialog() {
        mSimpleArcDialog = new SimpleArcDialog(getActivity());
        mSimpleArcDialog.setCancelable(false);

        ArcConfiguration configuration = new ArcConfiguration(getActivity());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(getString(dk.eatmore.demo.R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mSimpleArcDialog.setConfiguration(configuration);
    }

}
