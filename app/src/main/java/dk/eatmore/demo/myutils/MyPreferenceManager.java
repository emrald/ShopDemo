package dk.eatmore.demo.myutils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Lincoln on 07/01/16.
 */
public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = "dk.eatmore.demo";

//    // All Shared Preferences Keys

    //    private static final String KEY_USER_NAME = "user_name";
//    private static final String KEY_USER_EMAIL = "user_email";
//    private static final String KEY_NOTIFICATIONS = "notifications";
    public static String KEY_IS_LOGGEDIN = "is_login";
    public static final String KEY_USER_ID = "user_id";

    public static final String KEY_DEVICE_ID = "device_id";

    public static final String IS_RESTAURANT_INFO_AVAILABLE = "r_present";
    public static final String RESTAURANT_NAME = "r_name";
    public static final String RESTAURANT_IMAGE = "r_image";
    public static final String restaurant_num = "r_number";
    public static final String restaurant_ADDRESS = "r_add";
    public static final String RESTAURANT_HEALTH_REPORT = "r_health_report";

    public static final String FACEBOOK_IMAGE = "FacebookImage";

    //  public static final String ORDER_NUMBER="orde_no";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void delateSharedPreferenceByKey(String key) {
        editor.remove(key);
        editor.commit();
    }


    public void clearSharedPreference() {
        editor.clear();
        editor.commit();
    }

    //boolean
    public void setBooleanPreferences(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();

    }

    public boolean getBooleanPreferences(String value) {

        return pref.getBoolean(value, false);
    }

    //String
    public String getStringPreferences(String value) {

        return pref.getString(value, null);
    }

    public void setStringPreferences(String key, String value) {
        editor.putString(key, value);
        editor.commit();

    }


//    public void storeUser(User user) {
//        editor.putString(KEY_USER_ID, user.getId());
//        editor.putString(KEY_USER_NAME, user.getName());
//        editor.putString(KEY_USER_EMAIL, user.getEmail());
//        editor.commit();
//
//        Log.e(TAG, "User is stored in shared preferences. " + user.getName() + ", " + user.getEmail());
//    }
//
//    public User getUser() {
//        if (pref.getString(KEY_USER_ID, null) != null) {
//            String id, name, email;
//            id = pref.getString(KEY_USER_ID, null);
//            name = pref.getString(KEY_USER_NAME, null);
//            email = pref.getString(KEY_USER_EMAIL, null);
//
//            User user = new User(id, name, email);
//            return user;
//        }
//        return null;
//    }

//    public void addNotification(String notification) {
//
//        // get old notifications
//        String oldNotifications = getNotifications();
//
//        if (oldNotifications != null) {
//            oldNotifications += "|" + notification;
//        } else {
//            oldNotifications = notification;
//        }
//
//        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
//        editor.commit();
//    }
//
//    public String getNotifications() {
//        return pref.getString(KEY_NOTIFICATIONS, null);
//    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
