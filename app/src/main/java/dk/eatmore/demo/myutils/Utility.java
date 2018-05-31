package dk.eatmore.demo.myutils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dk.eatmore.demo.R;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.interfaces.InternetUnavailableCallback;

import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.WIFI_SERVICE;

/**
 * Created by ADMIN on 01-08-2016.
 */
public class Utility {
    private static final String TAG = Utility.class.getSimpleName();
    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void showInterNetMsg(final Context context) {
        final AlertDialog forgotDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(dk.eatmore.demo.R.layout.internet_info_dialog, null);

        Button buttonSetting = (Button) view.findViewById(dk.eatmore.demo.R.id.buttonSetting);
        Button buttoncancelI = (Button) view.findViewById(dk.eatmore.demo.R.id.buttoncancelI);
        builder.setView(view);
        forgotDialog = builder.create();
        forgotDialog.show();

        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotDialog.cancel();
                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(dialogIntent);

            }
        });
        buttoncancelI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotDialog.cancel();
            }
        });
    }

    public static void showDialogInternetUnavailable(final Context context, final InternetUnavailableCallback callback) {
        final AlertDialog internetAlertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(dk.eatmore.demo.R.layout.dialog_internet_unavailable, null);

        Button okButton = (Button) view.findViewById(R.id.btn_ok_internet);
        Button closeButton = (Button) view.findViewById(R.id.btn_close_internet);
        builder.setView(view);
        internetAlertDialog = builder.create();
        internetAlertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                internetAlertDialog.cancel();
//                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
//                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(dialogIntent);
                callback.onClickDialogButton(true);

            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                internetAlertDialog.cancel();
                callback.onClickDialogButton(false);
            }
        });
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String checkNull(final JSONObject json, final String key) {
        return json.isNull(key) ? "" : json.optString(key);
    }

    public static String convertCurrencyToDkk(Double amount) {
        NumberFormat mNumberFormat = NumberFormat.getCurrencyInstance
                (Locale.getDefault());
        mNumberFormat.setCurrency(Currency.getInstance("DKK"));

        return mNumberFormat.format(amount);
    }

    public static String formatValueToMoney(Double amount) {
//        DecimalFormat df = new DecimalFormat();
//        df.setMinimumFractionDigits(2);
//
//        return df.format(amount);
        NumberFormat mNumberFormat = NumberFormat.getCurrencyInstance(Locale.US);
        mNumberFormat.setMinimumFractionDigits(2);
        String convertedAmount = mNumberFormat.format(amount);
        System.out.println(convertedAmount);
        if (convertedAmount != null && convertedAmount.length() > 1) {
            convertedAmount = convertedAmount.substring(1, convertedAmount.length());
        }
        return convertedAmount;
    }

    public static String convertCurrencyToDanish(Double amount) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);
        if (deviceLocale.equalsIgnoreCase("en")) {
            return formatValueToMoney(amount);
        } else {
            NumberFormat mNumberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
            String convertedAmount = mNumberFormat.format(amount);
            System.out.println(convertedAmount);
            if (convertedAmount != null && convertedAmount.length() > 2) {
                convertedAmount = convertedAmount.substring(0, convertedAmount.length() - 2);
            }
            return convertedAmount;
        }
    }

    public static void copyToClipboard(Context mContext, TextView mTextView, String textToCopy) {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = null;
        if (mTextView != null) {
            clip = ClipData.newPlainText("Coupons/Gift",
                    mTextView.getText().toString().trim());
        } else {
            if (textToCopy != null) {
                clip = ClipData.newPlainText("Coupons/Gift",
                        textToCopy.trim());
            }
        }
        clipboard.setPrimaryClip(clip);

        Toast.makeText(mContext, "Coupons/Gift card copied", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static String NetwordDetect(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        String ipAdd = "";
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                ipAdd = GetDeviceipWiFiData(mContext);
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                ipAdd = GetDeviceipMobileData();
            }
        } else {
            // not connected to the internet
        }

        Log.e("IP", "IP " + ipAdd);
        return ipAdd;
    }


    private static String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = inetAddress.getHostAddress().toString();
                        return ipaddress;
                    }
                }
            }
        } catch (SocketException ex) {
            //Log.e(TAG, "Exception in Get IP Address: " + ex.toString());
        }
        return null;
    }

    private static String GetDeviceipWiFiData(Context mContext) {
        try {
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return String.format(Locale.getDefault(), "%d.%d.%d.%d",
                    (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                    (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        } catch (Exception ex) {
            //Log.e(TAG, ex.getMessage());
            return null;
        }
    }
    public static void logoutFromApp(Context mContext) {
        ApplicationClass.getInstance().getPrefManager().clearSharedPreference();
    }


}
