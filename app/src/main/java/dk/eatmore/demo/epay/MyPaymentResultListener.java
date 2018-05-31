//package com.edz.webshop.epay;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import java.util.Map;
//
//import eu.epay.library.PaymentResultListener;
//
//public class MyPaymentResultListener implements PaymentResultListener {
//
//	private ProgressDialog dialog;
//
//	private static final String TAG = "MyPaymentResultListener";
//
//	private Context c;
//
//	public MyPaymentResultListener(Context c)
//	{
//		this.c = c;
//	}
//
//	@Override
//	public void PaymentAccepted(Map<String, String> arg0) {
//
//		Toast.makeText(c, "PaymentAccepted", Toast.LENGTH_LONG).show();
//		Log.d(TAG, "PaymentAccepted" + arg0);
//	}
//
//	@Override
//	public void PaymentWindowLoaded(){
//
//		Toast.makeText(c, "PaymentWindowLoaded", Toast.LENGTH_LONG).show();
//		Log.d(TAG, "PaymentWindowLoaded");
//		dialog.dismiss();
//	}
//
//	@Override
//	public void PaymentWindowCancelled() {
//		Toast.makeText(c, "PaymentWindowCancelled", Toast.LENGTH_LONG).show();
//		Log.d(TAG, "PaymentWindowCancelled");
//
//	}
//
//	@Override
//	public void PaymentWindowLoading() {
//		dialog = new ProgressDialog(c);
//		dialog.setMessage("Preparing paymentwindow...");
//	    dialog.show();
//
//		Toast.makeText(c, "PaymentWindowLoading", Toast.LENGTH_LONG).show();
//		Log.d(TAG, "PaymentWindowLoading");
//	}
//
//	@Override
//	public void Debug(String arg0) {
//		Toast.makeText(c, "Debug" + arg0, Toast.LENGTH_LONG).show();
//		Log.d(TAG, arg0);
//	}
//
//	@Override
//	public void ErrorOccurred(int arg0, String arg1, String arg2) {
//		Toast.makeText(c, "ErrorOccurred" + arg1 + "," + arg2, Toast.LENGTH_LONG).show();
//	}
//
//	@Override
//	public void PaymentLoadingAcceptPage() {
//		// TODO Auto-generated method stub
//
//	}
//}
