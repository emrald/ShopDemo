package dk.eatmore.demo.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import dk.eatmore.demo.R;
import dk.eatmore.demo.adapter.OrderDetailsAdapter;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.interfaces.RecyclerViewOnClickListener;
import dk.eatmore.demo.model.OrderDetailsObject;
import dk.eatmore.demo.model.OrderProductsDetails;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import com.google.gson.Gson;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static dk.eatmore.demo.myutils.Utility.convertCurrencyToDanish;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderDetailsFragment extends Fragment implements View.OnClickListener, RecyclerViewOnClickListener {

    private static final String TAG = MyOrderDetailsFragment.class.getSimpleName();
    private View rootView;
    private TextView titleTextView;
    private ImageView backImageView;
    private ScrollView orderDetailsScrollView;
    private TextView orderNumberTextView;
    private TextView subtotalTextView;
    private TextView discountTextView;
    private TextView shippingTextView;
    private TextView totalTextView;
    private RecyclerView myOrderDetailsRecyclerView;
    private PercentRelativeLayout discountPercentRelativeLayout;
    private PercentRelativeLayout shippingPercentRelativeLayout;
    private SimpleArcDialog mSimpleArcDialog;

    private String orderNo = "";
    private String subtotalString = "";
    private String discountString = "";
    private String shippingString = "";
    private String totalString = "";

    ArrayList<OrderProductsDetails> orderDetails;
    private OrderDetailsAdapter orderDetailsAdapter;

    public MyOrderDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_order_details, container, false);
        setToolBar();
        bindViews();
        getValuesFromBundle();
        setGestureDetect();

        initDialog();
        getMyOrderDetails();
        return rootView;
    }

    private void setToolBar() {
        titleTextView = (TextView) rootView.findViewById(R.id.tootlbar_title);
        titleTextView.setText(getResources().getString(R.string.my_order_details));
        backImageView = (ImageView) rootView.findViewById(R.id.imgback);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindViews() {
        orderDetailsScrollView = (ScrollView) rootView.findViewById(R.id.sv_order_details);
        orderNumberTextView = (TextView) rootView.findViewById(R.id.tv_order_number);
        subtotalTextView = (TextView) rootView.findViewById(R.id.tv_order_subtotal);
        discountTextView = (TextView) rootView.findViewById(R.id.tv_order_discount);
        shippingTextView = (TextView) rootView.findViewById(R.id.tv_order_shipping);
        totalTextView = (TextView) rootView.findViewById(R.id.tv_order_total);
        discountPercentRelativeLayout = (PercentRelativeLayout) rootView.findViewById(R.id.prl_discount);
        shippingPercentRelativeLayout = (PercentRelativeLayout) rootView.findViewById(R.id.prl_shipping);

        myOrderDetailsRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_ordered_product_list);
        orderDetails = new ArrayList<>();
        orderDetailsAdapter = new OrderDetailsAdapter(getActivity(), MyOrderDetailsFragment.this);
        orderDetailsAdapter.setArrayList(orderDetails);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        myOrderDetailsRecyclerView.setLayoutManager(mLayoutManager);
        myOrderDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myOrderDetailsRecyclerView.setAdapter(orderDetailsAdapter);
    }

    private void getValuesFromBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderNo = bundle.getString(NewMyOrdersFragment.ORDER_NO, "0");
        }
    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void buttonOnClick(View v, int position) {
//        switch (v.getId()) {
//            case R.id.btn_order_details:
////                fragment = new MyParticularOrderFragment();
//                fragment = new MyOrderDetailsFragment();
//
//                Bundle bundle = new Bundle();
//                bundle.putString(ORDER_NO, orderDetails.get(position).getOrder_no());
//
//                fragment.setArguments(bundle);
//                // title="Explore The MindSets";
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransactionParticularTrans = fragmentManager.beginTransaction();
//                fragmentTransactionParticularTrans.add(R.id.fragment_container, fragment);
//                fragmentTransactionParticularTrans.addToBackStack(MyOrdersFragment.class.getName());
//                fragmentTransactionParticularTrans.commit();
////                String textToCopy = orderDetails.get(position).getGiftCardCode();
////                Utility.copyToClipboard(getActivity(), null, textToCopy);
//                break;
//        }
    }

    private void setGestureDetect() {

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

    private void getMyOrderDetails() {
        orderDetails = new ArrayList<>();
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mSimpleArcDialog.show();
        String tag_string_req = "getParticularOrders";
        StringRequest strReq = new StringRequest(Request.Method.POST, ApiCall.VIEW_PARTICULAR_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
                            mSimpleArcDialog.dismiss();
                        orderDetailsScrollView.setVisibility(View.VISIBLE);
                        Log.e(TAG, "getParticularOrders Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean status = jObj.getBoolean("status");

                            if (status) {
                                JSONArray jsonArray = jObj.getJSONArray("data");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                Gson gson = new Gson();
                                OrderDetailsObject mOrderDetailsObject = gson.fromJson(jsonObject.toString(), OrderDetailsObject.class);
                                subtotalString = mOrderDetailsObject.getOrder_total();
                                discountString = mOrderDetailsObject.getDiscount_amount();
                                shippingString = mOrderDetailsObject.getShipping_costs();
                                totalString = mOrderDetailsObject.getTotal_to_pay();
                                orderDetails = (ArrayList<OrderProductsDetails>) mOrderDetailsObject.getOrder_products_details();

                                orderNumberTextView.setText(orderNo);
                                subtotalTextView.setText(convertCurrencyToDanish(Double.parseDouble(subtotalString)));
                                if (discountString != null && !discountString.isEmpty())
                                    discountTextView.setText(convertCurrencyToDanish(Double.parseDouble(discountString)));
                                if (shippingString != null && !shippingString.isEmpty())
                                    shippingTextView.setText(convertCurrencyToDanish(Double.parseDouble(shippingString)));
                                totalTextView.setText(convertCurrencyToDanish(Double.parseDouble(totalString)));
                                orderDetailsAdapter.setArrayList(orderDetails);

                                if (discountString != null) {
                                    if (discountString.equalsIgnoreCase("null")) {
                                        discountString = "";
                                        discountPercentRelativeLayout.setVisibility(View.GONE);
                                    } else if (Double.parseDouble(discountString) > 0) {
                                        discountPercentRelativeLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        discountString = "";
                                        discountPercentRelativeLayout.setVisibility(View.GONE);
                                    }
                                } else {
                                    discountString = "";
                                    discountPercentRelativeLayout.setVisibility(View.GONE);
                                }

                                if (shippingString != null) {
                                    if (shippingString.equalsIgnoreCase("null")) {
                                        shippingString = "";
                                        shippingPercentRelativeLayout.setVisibility(View.GONE);
                                    } else if (Double.parseDouble(shippingString) > 0) {
                                        shippingPercentRelativeLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        shippingString = "";
                                        shippingPercentRelativeLayout.setVisibility(View.GONE);
                                    }
                                } else {
                                    shippingString = "";
                                    shippingPercentRelativeLayout.setVisibility(View.GONE);
                                }

                            }

                        } catch (JSONException e) {
                            Log.e("VIEW_ORDERS ", "JSONException " + e.toString());
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mSimpleArcDialog.dismiss();
                        Log.e("VIEW_ORDERS", "VIEW_ORDERS Error: " + error.getMessage());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("order_no", orderNo);
                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
