package dk.eatmore.demo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.adapter.PaymentMethodAdaptor;
import dk.eatmore.demo.interfaces.GetAllPaymentMethodsCallback;
import dk.eatmore.demo.model.PaymentMethodPojo;
import dk.eatmore.demo.network.NetworkUtils;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 06-07-2016.
 */
public class PaymentMethodFragment extends Fragment implements GetAllPaymentMethodsCallback {
    private static final String TAG = PaymentMethodFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    ImageView imgback;
    List<PaymentMethodPojo> data;
    PaymentMethodAdaptor adapter;
    SimpleArcDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(dk.eatmore.demo.R.layout.payment_method, container, false);
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();
        mRecyclerView = (RecyclerView) view.findViewById(dk.eatmore.demo.R.id.Paymentlist);

        TextView title = (TextView) view.findViewById(dk.eatmore.demo.R.id.tootlbar_title);
        title.setText(getResources().getString(dk.eatmore.demo.R.string.payment_method));

        imgback = (ImageView) view.findViewById(dk.eatmore.demo.R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        data = new ArrayList<>();
        adapter = new PaymentMethodAdaptor(data, getActivity());
        mRecyclerView.setAdapter(adapter);
        // setRetainInstance(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initDialog();
        getAllPaymentMethod();
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

    public void getAllPaymentMethod() {
        mDialog.show();

        String tag_string_req = "storePayment";

        NetworkUtils.callGetAllPaymentMethods(tag_string_req, PaymentMethodFragment.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.GET_PAYMENT_METHD, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                mDialog.dismiss();
//                Log.e("GET_PAYMENT_METHD", "GET_PAYMENT_METHD Response: " + response);
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//                    // Check for error node in json
//                    if (status) {
//                        JSONArray jsonArray = jObj.getJSONArray("data");
//                        int size = jsonArray.length();
//                        for (int i = 0; i < size; i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                            PaymentMethodPojo paymentMethodPojo = new PaymentMethodPojo();
//                            paymentMethodPojo.setPaymetTitle(jsonObject.getString("pm_name"));
//                            paymentMethodPojo.setPaymentImage(jsonObject.getString("logo"));
//
//                            paymentMethodPojo.setMainImagePath(jObj.getString("payment_method_image_path"));
//
//                            data.add(paymentMethodPojo);
//                        }
//                    }
//                    adapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    // hideDialog();
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mDialog.dismiss();
//                Log.e(TAG, "GET_PAYMENT_METHD Error: " + error.getMessage());
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
////                params.put("user_id", ApplicationClass.getInstance().getPrefManager()
////                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void getAllPaymentMethodsCallbackSuccess(String success) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        Log.e(TAG, "GET_PAYMENT_METHD Response: " + success);
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
                JSONArray jsonArray = jObj.getJSONArray("data");
                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    PaymentMethodPojo paymentMethodPojo = new PaymentMethodPojo();
                    paymentMethodPojo.setPaymetTitle(jsonObject.getString("pm_name"));
                    paymentMethodPojo.setPaymentImage(jsonObject.getString("logo"));

                    paymentMethodPojo.setMainImagePath(jObj.getString("payment_method_image_path"));

                    data.add(paymentMethodPojo);
                }
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            // hideDialog();
            e.printStackTrace();
        }

    }

    @Override
    public void getAllPaymentMethodsCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        Log.e(TAG, "GET_PAYMENT_METHD Error: " + volleyError.getMessage());
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailablePaymentMethod();
        }
    }

    private void showDialogInternetUnavailablePaymentMethod() {
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
                getAllPaymentMethod();
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
