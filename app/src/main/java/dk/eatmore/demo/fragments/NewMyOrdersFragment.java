package dk.eatmore.demo.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import dk.eatmore.demo.adapter.NewMyOrdersAdapter;
import dk.eatmore.demo.interfaces.GetMyOrdersCallback;
import dk.eatmore.demo.interfaces.RecyclerViewOnClickListener;
import dk.eatmore.demo.model.MyOrders;
import dk.eatmore.demo.netutils.ProgressDialaogView;
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
public class NewMyOrdersFragment extends Fragment implements View.OnClickListener, RecyclerViewOnClickListener, GetMyOrdersCallback {

    private static final String TAG = NewMyOrdersFragment.class.getSimpleName();
    private View rootView;
    private TextView titleTextView;
    private ImageView backImageView;
    private RecyclerView myOrdersRecyclerView;
    private RelativeLayout errorLayout;
    private SimpleArcDialog mSimpleArcDialog;

    private ArrayList<MyOrders> myOrdersArray;
    //    private MyOrdersAdapter myOrderAdapter;
    private NewMyOrdersAdapter myOrderAdapter;

    Context context;
    Button btnBackToMain;
    Fragment fragment = null;
    //    private ImageView imgback;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    public static String ORDER_NO = "order_no";

    public NewMyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(dk.eatmore.demo.R.layout.fragment_new_my_orders, container, false);
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();
        setToolBar();
        bindViews();
        setGestureDetect();

        initDialog();
        getMyOrders();
        return rootView;
    }

    private void setToolBar() {
        titleTextView = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.tootlbar_title);
        titleTextView.setText(getResources().getString(dk.eatmore.demo.R.string.my_orders));
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
        myOrdersRecyclerView = (RecyclerView) rootView.findViewById(dk.eatmore.demo.R.id.rv_my_orders);
        myOrdersArray = new ArrayList<>();
        myOrderAdapter = new NewMyOrdersAdapter(getActivity(), NewMyOrdersFragment.this);
        myOrderAdapter.setArrayList(myOrdersArray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        myOrdersRecyclerView.setLayoutManager(mLayoutManager);
        myOrdersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myOrdersRecyclerView.setAdapter(myOrderAdapter);

        errorLayout = (RelativeLayout) rootView.findViewById(dk.eatmore.demo.R.id.error_layout);
        errorLayout.setVisibility(View.GONE);

        TextView txt_error_message = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.txt_error_message);
        txt_error_message.setText(dk.eatmore.demo.R.string.no_orders);
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
            case dk.eatmore.demo.R.id.btn_order_details:
//                fragment = new MyParticularOrderFragment();
                fragment = new MyOrderDetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putString(ORDER_NO, myOrdersArray.get(position).getOrder_no());

                fragment.setArguments(bundle);
                // title="Explore The MindSets";
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransactionParticularTrans = fragmentManager.beginTransaction();
                fragmentTransactionParticularTrans.add(dk.eatmore.demo.R.id.fragment_container, fragment);
                fragmentTransactionParticularTrans.addToBackStack(NewMyOrdersFragment.class.getName());
                fragmentTransactionParticularTrans.commit();
//                String textToCopy = myOrdersArray.get(position).getGiftCardCode();
//                Utility.copyToClipboard(getActivity(), null, textToCopy);
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

    private void getMyOrders() {
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mSimpleArcDialog.show();
        String tag_string_req = "getAllOrders";

        NetworkUtils.callGetMyOrders(tag_string_req, NewMyOrdersFragment.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST, ApiCall.VIEW_ORDERS,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
//                            mSimpleArcDialog.dismiss();
//
//                        Log.e("getAllOrders", "getAllOrders Response: " + response);
//                        try {
//                            JSONObject jObj = new JSONObject(response);
//                            boolean status = jObj.getBoolean("status");
//                            // Check for error node in json
//                            if (status) {
//
//                                JSONArray jsonArray = jObj.getJSONArray("orderresult");
//
//                                int size = jsonArray.length();
//                                if (size > 0) {
//                                    errorLayout.setVisibility(View.GONE);
//                                    for (int i = 0; i < size; i++) {
//                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                                        MyOrders myOrders = new MyOrders();
//                                        myOrders.setOrder_no(jsonObject.getString("order_no"));
//                                        myOrders.setOrder_date(jsonObject.getString("order_date"));
//                                        myOrders.setTotal_to_pay(jsonObject.getString("total_to_pay"));
//                                        myOrders.setDiscount(jsonObject.getString("discount_amount"));
//                                        myOrders.setExpectedTime(jsonObject.getString("expected_time"));
//
//                                        myOrdersArray.add(myOrders);
//                                    }
//                                } else {
//                                    errorLayout.setVisibility(View.VISIBLE);
//                                }
//                            } else {
//                                errorLayout.setVisibility(View.VISIBLE);
//                            }
//                            myOrderAdapter.notifyDataSetChanged();
//                        } catch (JSONException e) {
//                            // hideDialog();
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        mSimpleArcDialog.dismiss();
//                        Log.e("VIEW_ORDERS", "VIEW_ORDERS Error: " + error.getMessage());
//                    }
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
//                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void getMyOrdersCallbackSuccess(String success) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();

        Log.e(TAG, "getAllOrders Response: " + success);
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {

                JSONArray jsonArray = jObj.getJSONArray("orderresult");

                int size = jsonArray.length();
                if (size > 0) {
                    errorLayout.setVisibility(View.GONE);
                    for (int i = 0; i < size; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        MyOrders myOrders = new MyOrders();
                        myOrders.setOrder_no(jsonObject.getString("order_no"));
                        myOrders.setOrder_date(jsonObject.getString("order_date"));
                        myOrders.setTotal_to_pay(jsonObject.getString("total_to_pay"));
                        myOrders.setDiscount(jsonObject.getString("discount_amount"));
                        myOrders.setExpectedTime(jsonObject.getString("expected_time"));

                        myOrdersArray.add(myOrders);
                    }
                } else {
                    errorLayout.setVisibility(View.VISIBLE);
                }
            } else {
                errorLayout.setVisibility(View.VISIBLE);
            }
            myOrderAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            // hideDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void getMyOrdersCallbackError(VolleyError volleyError) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e(TAG, "VIEW_ORDERS Error: " + volleyError.getMessage());
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableMyOrders();
        }
    }

    private void showDialogInternetUnavailableMyOrders() {
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
                getMyOrders();
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
