package dk.eatmore.demo.myutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/***
 * class to save and retrieve data in the preferences
 *
 * @author Aniket
 */
public class SharedPrefClass {

    /* private members */
    @SuppressWarnings("unused")
    private Context context;

    private static final String PREF_NAME = "WebShopPref";

    private static final String FACEBOOK_LOGIN = "FacebookLogin";

    public static final String LOGIN_STATUS = "LoginStatus";
    public static final String USER_ID = "UserId";
    private static final String USER_DETAILS = "UserDetails";

    private SharedPreferences preferences = null;
    public static SharedPrefClass sharedPreferenceDataManager = null;


    //private String key;
//	private String value;

    /***
     * this is constructor for saving and retrieving the values from shared_preferences.
     *
     * @param context
     */
    public SharedPrefClass(Context context) {
        this.context = context;
        if (preferences == null) {
//            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences = context.getSharedPreferences(PREF_NAME, 0);
        }
    }

    /***
     * this is static function to create singleton object.
     *
     * @param context
     * @return
     */
    public static SharedPrefClass getInstance(Context context) {
        if (sharedPreferenceDataManager == null) {
            sharedPreferenceDataManager = new SharedPrefClass(context);
        }
        return sharedPreferenceDataManager;
    }

    /***
     * this is function to clear preferences.
     *
     * @return
     */
    public void clearSharedPrefs() {
        Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    /***
     * this will return saved preference having value as string
     *
     * @param key
     * @return
     */
    public String getSavedStringPreference(String key) {
        String value = preferences.getString(key, null);
        return value;
    }

    /***
     * this will return saved preference having value as boolean
     *
     * @param key
     * @return
     */
    public boolean getSavedBooleanPreference(String key) {
        boolean value = preferences.getBoolean(key, false);
        return value;
    }

    /***
     * this will return saved preference having value as boolean
     *
     * @param key
     * @return
     */
    public long getSavedLongPreference(String key) {
        long value = preferences.getLong(key, 0);
        return value;
    }

    public float getSavedFloatPreference(String key) {
        float value = preferences.getFloat(key, 0f);
        return value;
    }

    /***
     * @param key
     * @param value
     */
    public void savePreference(String key, String value) {
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    /***
     * @param key
     * @param value
     */
    public void savePreference(String key, long value) {
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /***
     * @param key
     * @param value
     */
    public void savePreference(String key, float value) {
        Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /***
     * @param key
     * @param value
     */
    public void savePreference(String key, boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /***
     * this will return saved preference having value as boolean
     *
     * @param key
     * @return
     */
    public boolean getSavedBooleanPreferenceWithDefaultValue(String key, boolean retuenDefaultValue) {
        boolean value = preferences.getBoolean(key, retuenDefaultValue);
        return value;
    }

    /**
     * @param key
     * @param array
     * @return
     */
    public void savePreference(String key, boolean[] array) {
        Editor editor = preferences.edit();
        editor.putInt(key + "_size", array.length);
        for (int i = 0; i < array.length; i++)
            editor.putBoolean(key + "_" + i, array[i]);
        editor.apply();
    }

    public boolean[] getSavedBooleanArray(String key) {
        int size = preferences.getInt(key + "_size", 0);
        boolean array[] = new boolean[size];
        for (int i = 0; i < size; i++)
            array[i] = preferences.getBoolean(key + "_" + i, false);
        return array;
    }

    /* check login or not */
    public boolean isUserLogin() {
        return preferences.getBoolean(LOGIN_STATUS, false);
    }

    public void setUserLogin(boolean loginStatus) {
        Editor editor = preferences.edit();
        editor.putBoolean(LOGIN_STATUS, loginStatus);
        editor.apply();
    }

        /* save and get User id */

    public String getUserId() {
        return preferences.getString(USER_ID, "0");
    }

    public void setUserId(String strUserId) {
        Editor editor = preferences.edit();
        editor.putString(USER_ID, strUserId);
        editor.apply();
    }

    public String getUserDetails() {
        return preferences.getString(USER_DETAILS, "");
    }

    public void setUserDetails(String strUserDetails) {
        Editor editor = preferences.edit();
        editor.putString(USER_DETAILS, strUserDetails);
        editor.apply();
    }

    // Religion
    public boolean isFacebookLogin() {
        return preferences.getBoolean(FACEBOOK_LOGIN, false);
    }

    public void setFacebookLogin(boolean facebookLogin) {
        Editor editor = preferences.edit();
        editor.putBoolean(FACEBOOK_LOGIN, facebookLogin);
        editor.apply();
    }

}
