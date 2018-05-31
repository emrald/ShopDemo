package dk.eatmore.demo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.netutils.ProgressDialaogView;

import com.leo.simplearcloader.SimpleArcDialog;

import java.util.HashMap;
import java.util.Map;

import eu.epay.library.EpayWebView;
import eu.epay.library.PaymentResultListener;

public class EpayFragment extends Fragment implements PaymentResultListener {

    private static final String TAG = EpayFragment.class.getSimpleName();
    private View parentView;
    private Toolbar ePayToolbar;
    private ImageView imgback;
    private WebView webView;

    static String CARD_NO, TAXN_ID, TAX_FEE;
    private SimpleArcDialog pDialog;
    public static boolean acceptPayment = false;
    public static boolean ePayPageBackPress = false;
    private boolean isInResume = true;


    public EpayFragment() {
        // Required empty public constructor
    }

    private Map<String, String> data = new HashMap<String, String>();
    //    CartDialogFragmentClass cartDialogFragmentClass;
    String order_total;
    int final_amount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.epay_payment_view, container, false);
        webView = (WebView) parentView.findViewById(R.id.paymentWebView);
        ePayToolbar = (Toolbar) parentView.findViewById(R.id.toolbar);
        ePayToolbar.setVisibility(View.GONE);
        ePayPageBackPress = true;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //  orderNo = bundle.getString(CartFragment.ORDER_NO,"0");
            order_total = bundle.getString(CartFragment.AMOUNT_TOTAL, "0");

            final_amount = (int) (Double.parseDouble(order_total) * 100);
        }

        EpayWebView paymentView = new EpayWebView(this, webView, false);
        webView = paymentView.LoadPaymentWindow(getData());

        TextView title = (TextView) parentView.findViewById(R.id.tootlbar_title);
        title.setText("Payment view");
        imgback = (ImageView) parentView.findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
//                if (acceptPayment) {
//                    Intent intent = new Intent("OnlinePayment");
//                    // You can also include some extra data.
//                    intent.putExtra("onlinePayment", "1");
//                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//                }
            }
        });

        return parentView;
    }

    public Map<String, String> getData() {
        //http://tech.epay.dk/en/specification#258
//        data.put("merchantnumber", "8024206");
//        data.put("merchantnumber", "1027775");

        //  data.put("merchantnumber", "5209293");

        // byens
        data.put("merchantnumber", "6458157");

        //http://tech.epay.dk/en/specification#259
        data.put("currency", "DKK");

        //http://tech.epay.dk/en/specification#260
        data.put("amount", String.valueOf(final_amount));

//        Random r = new Random();
//        int tempOrderno = r.nextInt(80 - 65) + 61235;

        //http://tech.epay.dk/en/specification#261
        data.put("orderid", MainActivity.ORDER_NO);

        //http://tech.epay.dk/en/specification#262
        //data.put("windowid", "1");

        //http://tech.epay.dk/en/specification#263
        data.put("paymentcollection", "0");

        //http://tech.epay.dk/en/specification#264
        data.put("lockpaymentcollection", "0");

        //http://tech.epay.dk/en/specification#265
        //data.put("paymenttype", "1,2,3");

        //http://tech.epay.dk/en/specification#266
        data.put("language", "0");

        //http://tech.epay.dk/en/specification#267
        data.put("encoding", "UTF-8");

        //http://tech.epay.dk/en/specification#269
        //data.put("mobilecssurl", "");

        //http://tech.epay.dk/en/specification#270
        // Authorize
        data.put("instantcapture", "0");
        // Direct Capture
//        data.put("instantcapture", "1");

        //http://tech.epay.dk/en/specification#272
        //data.put("splitpayment", "0");

        //http://tech.epay.dk/en/specification#275
        //data.put("callbackurl", "");

        //http://tech.epay.dk/en/specification#276
        data.put("instantcallback", "1");

        //http://tech.epay.dk/en/specification#278
        data.put("ordertext", "This is the order text");

        //http://tech.epay.dk/en/specification#279
        //data.put("group", "group");

        //http://tech.epay.dk/en/specification#280
        data.put("description", "This is the description text");

        //http://tech.epay.dk/en/specification#281
        //data.put("hash", "");

        //http://tech.epay.dk/en/specification#282
        //data.put("subscription", "0");

        //http://tech.epay.dk/en/specification#283
        //data.put("subscriptionname", "0");

        //http://tech.epay.dk/en/specification#284
        //data.put("mailreceipt", "");

        //http://tech.epay.dk/en/specification#286
        //data.put("googletracker", "0");

        //http://tech.epay.dk/en/specification#287
        //data.put("backgroundcolor", "");

        //http://tech.epay.dk/en/specification#288
        //data.put("opacity", "");

        //http://tech.epay.dk/en/specification#289
        data.put("declinetext", "This is the decline text");

        Log.e("payment raw data", "payment" + data);

        return data;
    }


    @Override
    public void PaymentAccepted(Map<String, String> map) {
        Log.e(TAG, "PaymentAccepted");

//        if (cartDialogFragmentClass == null) {
        if (isInResume) {
            Log.e(TAG, "isInResume");
            if (!acceptPayment) {
                Log.e(TAG, "!acceptPayment");
                CARD_NO = map.get("cardno");
                TAXN_ID = map.get("txnid");
                String transactionFee = map.get("txnfee");
                TAX_FEE = String.valueOf(Double.parseDouble(transactionFee) / 100);

                acceptPayment = true;
                ePayPageBackPress = false;
//            cartDialogFragmentClass = new CartDialogFragmentClass();
//            Bundle bundle = new Bundle();
//            bundle.putString(CartFragment.ORDER_NO,MainActivity.ORDER_NO);
//            cartDialogFragmentClass.setArguments(bundle);
//
//            cartDialogFragmentClass.show(getActivity().getSupportFragmentManager(), "EpayFragment");
//        }

                ePayToolbar.setVisibility(View.GONE);

//        Intent intent = new Intent("OnlinePayment");
//        // You can also include some extra data.
//        intent.putExtra("onlinePayment", "1");
//        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//        Log.e("payment", "PaymentAccepted" + map);

                getActivity().getSupportFragmentManager().popBackStack();
            } else {
                Log.e(TAG, "acceptPayment");
                acceptPayment = false;
            }
        } else {
            Log.e(TAG, "!isInResume");
            if (!acceptPayment) {
                Log.e(TAG, "!acceptPayment");
                CARD_NO = map.get("cardno");
                TAXN_ID = map.get("txnid");
                String transactionFee = map.get("txnfee");
                TAX_FEE = String.valueOf(Double.parseDouble(transactionFee) / 100);

                acceptPayment = true;
                ePayPageBackPress = false;
//            cartDialogFragmentClass = new CartDialogFragmentClass();
//            Bundle bundle = new Bundle();
//            bundle.putString(CartFragment.ORDER_NO,MainActivity.ORDER_NO);
//            cartDialogFragmentClass.setArguments(bundle);
//
//            cartDialogFragmentClass.show(getActivity().getSupportFragmentManager(), "EpayFragment");
//        }

//                ePayToolbar.setVisibility(View.GONE);

//                getActivity().getSupportFragmentManager().popBackStack();
//            } else {
//                Log.e(TAG, "acceptPayment");
//                acceptPayment = false;
            }
        }
    }

    @Override
    public void PaymentWindowLoading() {
        pDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        pDialog.show();
    }

    @Override
    public void PaymentWindowLoaded() {
        pDialog.dismiss();
        Log.e(TAG, "PaymentWindowLoaded ");
    }

    @Override
    public void PaymentWindowCancelled() {
        Toast.makeText(getActivity(), "PaymentWindowCancelled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void ErrorOccurred(int i, String s, String s1) {
        Log.e(TAG, "ErrorOccurred " + i + " " + s + " " + s1);
    }

    @Override
    public void Debug(String s) {
        Log.e(TAG, "Debug " + s);
    }

    @Override
    public void PaymentLoadingAcceptPage() {
        Log.e("payment", "PaymentLoadingAcceptPage");
        //   Toast.makeText(getActivity(),"PaymentLoadingAcceptPage",1000).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume" + isInResume + acceptPayment + ePayPageBackPress);
        isInResume = true;
        if (acceptPayment && !ePayPageBackPress)
            getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        isInResume = false;
    }
}
