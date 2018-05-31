package dk.eatmore.demo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.fragments.EpayFragment;
import dk.eatmore.demo.interfaces.GetRestaurantInfoCallback;
import dk.eatmore.demo.interfaces.InternetUnavailableCallback;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.Utility;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import dk.eatmore.demo.network.NetworkUtils;

import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity implements GetRestaurantInfoCallback {

    private static final String TAG = SplashScreen.class.getSimpleName();
    private SimpleArcDialog mSimpleArcDialog;
    private ImageView splashScreenImageView;
    private TextView phoneNumberTextView;
    private TextView addressTextView;
    private LinearLayout contactAddressLinearLayout;
    private static int SPLASH_TIME_OUT = 2000;
//    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        // To get Hash Key for Facebook SDK
//        getKeyHashForFacebook();
        bindViews();
        EpayFragment.acceptPayment = false;
        String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        boolean resInfoAvailable = ApplicationClass.getInstance().getPrefManager().
                getBooleanPreferences(MyPreferenceManager.IS_RESTAURANT_INFO_AVAILABLE);

        if (resInfoAvailable) {
            String resImg = ApplicationClass.getInstance().getPrefManager().
                    getStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE);

            String resContactNumber = ApplicationClass.getInstance().getPrefManager().
                    getStringPreferences(MyPreferenceManager.restaurant_num);

            String resAddress = ApplicationClass.getInstance().getPrefManager().
                    getStringPreferences(MyPreferenceManager.restaurant_ADDRESS);

            Picasso.with(this)
                    .load(resImg)
                    //.transform(new CircleTransformation())
                    .into(splashScreenImageView);
            phoneNumberTextView.setText(resContactNumber);
            addressTextView.setText(resAddress);
            contactAddressLinearLayout.setVisibility(View.VISIBLE);

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    if (ApplicationClass.getInstance().getPrefManager()
//                            .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN)) {
//                        Intent i = new Intent(getBaseContext(), MainActivity.class);
//                        startActivity(i);
//                        finish();
//                    } else {
//                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//
//                }
//            }, SPLASH_TIME_OUT);
        }

//        if (Utility.isNetworkConnected(SplashScreen.this))
//            getRestaurantInfo();
//        else
//            Utility.showDialogInternetUnavailable(SplashScreen.this, SplashScreen.this);

//        if (Utility.isNetworkConnected(SplashScreen.this))
//            getRestaurantInfo();
//        else
//            Utility.showInterNetMsg(SplashScreen.this);

        getRestaurantInfo();

//        if (resInfoAvailable) {
//            String resImg = ApplicationClass.getInstance().getPrefManager().
//                    getStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE);
//
//            String resContactNumber = ApplicationClass.getInstance().getPrefManager().
//                    getStringPreferences(MyPreferenceManager.restaurant_num);
//
//            String resAddress = ApplicationClass.getInstance().getPrefManager().
//                    getStringPreferences(MyPreferenceManager.restaurant_ADDRESS);
//
//            Picasso.with(this)
//                    .load(resImg)
//                    //.transform(new CircleTransformation())
//                    .into(splashScreenImageView);
//            phoneNumberTextView.setText(resContactNumber);
//            addressTextView.setText(resAddress);
//            contactAddressLinearLayout.setVisibility(View.VISIBLE);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    if (ApplicationClass.getInstance().getPrefManager()
//                            .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN)) {
//                        Intent i = new Intent(getBaseContext(), MainActivity.class);
//                        startActivity(i);
//                        finish();
//                    } else {
//                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//
//                }
//            }, SPLASH_TIME_OUT);
//        } else {
//            getRestaurantInfo();
//        }

        Intent msgIntent = new Intent(this, ClearCart.class);
        startService(msgIntent);
    }

    private void bindViews() {
        splashScreenImageView = (ImageView) findViewById(dk.eatmore.demo.R.id.iv_splashScreen);
        phoneNumberTextView = (TextView) findViewById(dk.eatmore.demo.R.id.tv_phone_number);
        addressTextView = (TextView) findViewById(dk.eatmore.demo.R.id.tv_address);
        contactAddressLinearLayout = (LinearLayout) findViewById(dk.eatmore.demo.R.id.ll_contact_address);
    }

    private void getKeyHashForFacebook() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("dk.eatmore.demo", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e(TAG + " hash key", hashKey);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private void getRestaurantInfo() {
        String tag_string_req = "getRestroInfo";
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(SplashScreen.this);
        mSimpleArcDialog.show();

        NetworkUtils.callGetRestaurantInfo(tag_string_req, SplashScreen.this);
    }

    @Override
    public void getRestaurantInfoCallbackSuccess(String success) {
        //  Log.e(TAG, "forgetPassword Response: " + response.toString());
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();

        try {
            JSONObject data = new JSONObject(success);
            if (data.getBoolean("status")) {
                String android_version = "";
                if (data.has("android_version"))
                    android_version = data.getString("android_version");

                String android_app_link = "";
                if (data.has("android_app_link"))
                    android_app_link = data.getString("android_app_link");

                JSONObject dataJsonObject = data.getJSONObject("data");
                String restaurantName = data.getString("restaurant_name");
                String image = data.getString("logo_path") + dataJsonObject.getString("logo");
                String address1 = "";
                JSONObject address1JsonObject = dataJsonObject.getJSONObject("address1");
                int size = address1JsonObject.length();
                if (size > 0) {
                    for (int i = 1; i <= size; i++) {
                        String temp = address1JsonObject.getString("" + i);
                        if (temp != null && !temp.isEmpty())
                            if (i == 1)
                                address1 = temp;
                            else
                                address1 = address1 + ",\n" + temp;
                    }
                }
                String address = dataJsonObject.getString("address");
                String contactNum = dataJsonObject.getString("phone_no");

                String k_link = "";
                if (dataJsonObject.has("k_link"))
                    k_link = dataJsonObject.getString("k_link");

                ApplicationClass.getInstance().getPrefManager().
                        setBooleanPreferences(MyPreferenceManager.IS_RESTAURANT_INFO_AVAILABLE, true);
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.PREF_NAME, restaurantName);
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE, image);
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.RESTAURANT_HEALTH_REPORT, k_link);

                Picasso.with(SplashScreen.this)
                        .load(image)
                        //.transform(new CircleTransformation())
                        .into(splashScreenImageView);

                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.restaurant_ADDRESS, address);
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.restaurant_num, contactNum);

                phoneNumberTextView.setText(contactNum);
                addressTextView.setText(address);

                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String versionName = pInfo.versionName;
                    int versionCode = pInfo.versionCode;

                    Log.e(TAG, "versionName: " + versionName + " \nversionCode: " + versionCode);


                    if (android_version.equalsIgnoreCase(versionName)) {
//                    if (android_version.equalsIgnoreCase("1.0.1")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                if (ApplicationClass.getInstance().getPrefManager()
//                                        .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN)) {
//                                    Intent i = new Intent(getBaseContext(), MainActivity.class);
//                                    startActivity(i);
//                                    finish();
//                                } else {
                                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                                startActivity(i);
                                finish();
//                                }

                            }
                        }, SPLASH_TIME_OUT);
                    } else {
                        versionUpdate(android_app_link);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                String msg = data.getString("msg");
                Toast.makeText(SplashScreen.this, msg, Toast.LENGTH_LONG).show();
                showAlert(msg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getRestaurantInfoCallbackError(VolleyError volleyError) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e(TAG, "getOpeningHour Error: " + volleyError.getMessage());


        if (volleyError instanceof NoConnectionError) {
//            Utility.showDialogInternetUnavailable(SplashScreen.this, SplashScreen.this);
//            msg = getResources().getString(R.string.no_internet_connection);
            showDialogInternetUnavailable();
        }
    }

    public void showDialogInternetUnavailable() {
        final AlertDialog internetAlertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        LayoutInflater inflater = (LayoutInflater) SplashScreen.this.
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
                getRestaurantInfo();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetAlertDialog.dismiss();
                finish();
            }
        });
    }

    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        String msg = "", dalert = getString(dk.eatmore.demo.R.string.alert);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View titleview = getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
        title.setText(dalert);
        builder.setCustomTitle(titleview);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(dk.eatmore.demo.R.string.dialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void versionUpdate(final String android_app_link) {
//        message = "Please update the app";
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        LayoutInflater inflater = SplashScreen.this.getLayoutInflater();
        View view = inflater.inflate(dk.eatmore.demo.R.layout.dilaog_update_app, null);

        TextView textViewTitle = (TextView) view.findViewById(dk.eatmore.demo.R.id.tv_title_update_app);
        Button butonOk = (Button) view.findViewById(dk.eatmore.demo.R.id.btn_update_app);
        TextView dialogText = (TextView) view.findViewById(dk.eatmore.demo.R.id.tv_new_update);
//        textViewTitle.setText(R.string.alert);
        builder.setView(view);
//        dialogText.setTypeface(null, Typeface.BOLD);
//        dialogText.setText(message);

        final AlertDialog forgotDialog = builder.create();
        forgotDialog.setCancelable(false);
        forgotDialog.show();


        butonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotDialog.dismiss();
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    Log.e(TAG, "" + "market://details?id=" + appPackageName);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(android_app_link)));
                }
                finish();
            }
        });
    }

}
