package dk.eatmore.demo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import dk.eatmore.demo.R;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.fragments.InfoBarFragment;
import dk.eatmore.demo.fragments.MenuFragment;
import dk.eatmore.demo.fragments.MyCartCheckOut;
import dk.eatmore.demo.interfaces.RestaurantIsClosedCallback;
import dk.eatmore.demo.interfaces.SendRegistrationToServerCallback;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.Utility;
import dk.eatmore.demo.network.NetworkUtils;

import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements RestaurantIsClosedCallback, SendRegistrationToServerCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private InfoBarFragment infobarFragment;
    private MenuFragment menuFragment;
    private MyCartCheckOut cartfragment;
    private boolean isLight = true;
    private AHBottomNavigation bottomNavigation;
    private String deviceId = "0";
    public static String ORDER_NO = "";

    public static boolean isRestaurantClosed = false;
    public static String restaurantClosedMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(isLight ? Color.WHITE : Color.BLACK));
        setContentView(R.layout.activity_mainn);
//        setContentView(R.layout.activity_main);

        checkRestaurantIsClosed();

        //sending fcm device id to server
        deviceId = FirebaseInstanceId.getInstance().getToken();

        ApplicationClass.getInstance().getPrefManager().
                setStringPreferences(MyPreferenceManager.KEY_DEVICE_ID, deviceId);

        sendRegistrationToServer(deviceId);

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.menu_bottom, R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.knifefork, R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.shoppingcart, R.color.white);
//        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        bottomNavigation.setAccentColor(Color.parseColor("#019E0F"));
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setCurrentItem(1);
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
//        bottomNavigation.setNotification(0, 2);

//        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                switch (position) {
//                    case 0:
//                        infobarFragment = new InfoBarFragment();
//                        openFramnebt(infobarFragment);
//                        break;
//                    case 1:
//                        menuFragment = new MenuFragment();
//                        openFramnebt(menuFragment);
//                        break;
//                    case 2:
//                        cartfragment = new MyCartCheckOut();
//                        openFramnebt(cartfragment);
//                        break;
//                }
//
//                return wasSelected;
//            }
//        });

        bottomNavigation.setOnTabSelectedListener(
                new AHBottomNavigation.OnTabSelectedListener() {
                    @Override
                    public boolean onTabSelected(int position, boolean wasSelected) {
                        switch (position) {
                            case 0:
//                                restaurantCloseDialog();
                                infobarFragment = new InfoBarFragment();
                                openFramnebt(infobarFragment);
                                break;
                            case 1:
                                menuFragment = new MenuFragment();
                                openFramnebt(menuFragment);
                                break;
                            case 2:
//                                restaurantCloseDialog();
                                cartfragment = new MyCartCheckOut();
                                openFramnebt(cartfragment);
                                break;
                        }
                        return true;
                    }
                });
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("CartCount"));

        menuFragment = new MenuFragment();
        openFramnebt(menuFragment);

    }

    private void checkRestaurantIsClosed() {
        String tag_string_req = "RESTO_CLOSED";

        NetworkUtils.checkRestaurantIsClosed(tag_string_req, MainActivity.this);
    }

    @Override
    public void restaurantIsClosedCallbackSuccess(String success) {
        //  Log.e(TAG, "forgetPassword Response: " + response.toString());
//                if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
//                    mSimpleArcDialog.dismiss();

        try {
            JSONObject responseJsonObject = new JSONObject(success);
            boolean status = responseJsonObject.getBoolean("status");
            isRestaurantClosed = responseJsonObject.getBoolean("is_restaurant_closed");

            if (status)
                if (isRestaurantClosed) {
                    restaurantClosedMsg = responseJsonObject.getString("msg");
//                                    restaurantCloseDialog(msg);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void restaurantIsClosedCallbackError(VolleyError volleyError) {

    }

    private void restaurantCloseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dilaog_info_layout, null);

        TextView textViewTitle = (TextView) view.findViewById(R.id.textviewinfoTitle);
        Button butonOk = (Button) view.findViewById(R.id.buttonAaddtocartOk);
        TextView dialogText = (TextView) view.findViewById(R.id.addedtocartText);
        textViewTitle.setText("Restaurant is closed");
        builder.setView(view);
        dialogText.setTypeface(null, Typeface.BOLD);
        dialogText.setText(restaurantClosedMsg);

        final AlertDialog forgotDialog = builder.create();
        forgotDialog.show();


        butonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotDialog.dismiss();
            }
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String count = intent.getStringExtra("count");
            //Order Completed redirecting
            String order = intent.getStringExtra("orderRejected");

            if (count != null && count.equals("007")) {
                if (order != null && order.equals("1")) {
                    menuFragment = new MenuFragment();
                    openFramnebt(menuFragment);
                    bottomNavigation.setCurrentItem(1);
                }
                bottomNavigation.setNotification(0, 2);
            } else if (bottomNavigation != null)
                try {
                    bottomNavigation.setNotification(Integer.parseInt(count), 2);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }

        }
    };

    public void setCurrentItemOfBottomNav(int position) {
        bottomNavigation.setCurrentItem(position);
    }

    @Override
    protected void onDestroy() {
        //    Toast.makeText(this, "on distroy", Toast.LENGTH_SHORT).show();

        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        //  ApplicationClass.getInstance().getPrefManager().delateSharedPreferenceByKey(MyPreferenceManager.ORDER_NUMBER);

        // TODO: 3/22/2017 Logout
        Utility.logoutFromApp(MainActivity.this);

        super.onDestroy();
    }

    public void openFramnebt(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    private void sendRegistrationToServer(final String token) {
        String tag_string_req = "fcmtoken";

        NetworkUtils.sendRegistrationToServer(token, tag_string_req, MainActivity.this);
    }

    @Override
    public void sendRegistrationToServerCallbackSuccess(String success) {
        try {
            JSONObject jsonObjetc = new JSONObject(success);

            if (jsonObjetc.getBoolean("status"))
                bottomNavigation.setNotification(Integer.parseInt(jsonObjetc.getString("cartcnt")), 2);
            else
                bottomNavigation.setNotification(0, 2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.e(TAG, "sendRegistrationToServer Response: " + success);
    }

    @Override
    public void sendRegistrationToServerCallbackError(VolleyError volleyError) {

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count < 1) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.close_app)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, null)
                    .show();
        } else {
            fragmentManager.popBackStack();
//            showBottomNavigation();
        }
    }

    public void showBottomNavigation() {
        if (bottomNavigation != null)
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigation() {
        if (bottomNavigation != null)
            bottomNavigation.setVisibility(View.GONE);
    }
}
