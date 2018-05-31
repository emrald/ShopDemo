package dk.eatmore.demo.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import com.leo.simplearcloader.SimpleArcDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CartDialogFragmentClass extends DialogFragment {
    Button mMainMenuBtn;
    Context mContext;
    //  String orderNo;
    SimpleArcDialog pDialog;
    Dialog dialog;
    TextView dialogOrderId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.order_confirmation_activity, null);
        alert.setView(v);
        dialog = alert.create();

        dialogOrderId = (TextView) v.findViewById(R.id.dialogOrderId);

        dialogOrderId.setText(MainActivity.ORDER_NO);

        sendOrderTransaction();

        //  printOrder();

        mMainMenuBtn = (Button) v.findViewById(R.id.btnBackToMain);
        mMainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getActivity().getSupportFragmentManager().popBackStack();

                Intent intent = new Intent("CartCount");
                // You can also include some extra data.
                intent.putExtra("count", "007");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

            }
        });

        return alert.create();

    }

    public void sendOrderTransaction() {
        pDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        pDialog.show();

        String tag_string_req = "";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ApiCall.ORDER_TRANSACTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pDialog.dismiss();
                Log.e("ORDER_TRANSACTION", "ORDER_TRANSACTION Response: " + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    // Check for error node in json
                    if (status) {
                        Intent intent = new Intent("CartCount");
                        // You can also include some extra data.
                        intent.putExtra("count", "0");
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        MainActivity.ORDER_NO = "";
                    }

                } catch (JSONException e) {
                    // hideDialog();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("printOrder", "printOrder Error: " + error.getMessage());
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("order_no", MainActivity.ORDER_NO);
                params.put("cardno", EpayFragment.CARD_NO);
                params.put("txnid", EpayFragment.TAXN_ID);
                params.put("txnfee", EpayFragment.TAX_FEE);
                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


//    public void printOrder()
//    {
//
//        pDialog=new ProgressDialaogView().getProgressDialog(getActivity());
//        pDialog.show();
//
//        String tag_string_req = "";
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//               ApiCall.SMS_ORDER, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                pDialog.dismiss();
//                Log.e("printOrder", "printOrder Response: " + response.toString());
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//                    // Check for error node in json
//                    if (status) {
//
//                        Intent intent = new Intent("CartCount");
//                        // You can also include some extra data.
//                        intent.putExtra("count", "0");
//                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//                        MainActivity.ORDER_NO="";
//                    }
//
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
//                Log.e("printOrder", "printOrder Error: " + error.getMessage());
//                pDialog.dismiss();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                params.put("order_no", MainActivity.ORDER_NO);
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//    }
}
