package dk.eatmore.demo.network;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.adapter.ExtraToppingSubGroup;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.interfaces.AboutUsCallback;
import dk.eatmore.demo.interfaces.AddToCartCallback;
import dk.eatmore.demo.interfaces.ApplyCodeGiftCardCallback;
import dk.eatmore.demo.interfaces.CancelOrderTransactionCallback;
import dk.eatmore.demo.interfaces.ChangePasswordCallback;
import dk.eatmore.demo.interfaces.CheckoutCallback;
import dk.eatmore.demo.interfaces.ClearCartCallback;
import dk.eatmore.demo.interfaces.CommunicatingWithRestaurantCallback;
import dk.eatmore.demo.interfaces.ContactUsCallback;
import dk.eatmore.demo.interfaces.ContactUsSettingsCallback;
import dk.eatmore.demo.interfaces.DeleteItemFromCartCallback;
import dk.eatmore.demo.interfaces.DeleteUserAccountCallback;
import dk.eatmore.demo.interfaces.FacebookLoginCallback;
import dk.eatmore.demo.interfaces.ForgotPasswordCallback;
import dk.eatmore.demo.interfaces.GetAllGiftCardsCallback;
import dk.eatmore.demo.interfaces.GetAllPaymentMethodsCallback;
import dk.eatmore.demo.interfaces.GetCityListCallback;
import dk.eatmore.demo.interfaces.GetMyOrdersCallback;
import dk.eatmore.demo.interfaces.GetOpeningHoursCallback;
import dk.eatmore.demo.interfaces.GetPostalCodesCallback;
import dk.eatmore.demo.interfaces.GetRestaurantInfoCallback;
import dk.eatmore.demo.interfaces.GetUserProfileDataCallback;
import dk.eatmore.demo.interfaces.LoginCallback;
import dk.eatmore.demo.interfaces.OrderDetailsForCheckoutCallback;
import dk.eatmore.demo.interfaces.OrderTransactionCallback;
import dk.eatmore.demo.interfaces.PickUpDeliveryTimeCallback;
import dk.eatmore.demo.interfaces.ProductDescriptionCallback;
import dk.eatmore.demo.interfaces.ProductExtraToppingsCallback;
import dk.eatmore.demo.interfaces.ProductListCallback;
import dk.eatmore.demo.interfaces.ProductSearchListCallback;
import dk.eatmore.demo.interfaces.RestaurantIsClosedCallback;
import dk.eatmore.demo.interfaces.SendRegistrationToServerCallback;
import dk.eatmore.demo.interfaces.ShippingCostCallback;
import dk.eatmore.demo.interfaces.UpdateProductCartQuantityCallback;
import dk.eatmore.demo.interfaces.VideoLinkHowItWorksCallback;
import dk.eatmore.demo.interfaces.ViewCartCallback;
import dk.eatmore.demo.model.ExtraToppeningCategory;
import dk.eatmore.demo.model.ProductAttributeList;
import dk.eatmore.demo.model.ProductAttributeValue;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.myutils.ExpandableListUtility;
import dk.eatmore.demo.myutils.MyPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by sachi on 1/31/2017.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private Context context;

    public NetworkUtils(Context context) {
        this.context = context;
    }

    // Call Get Restaurant Info
    public static void callGetRestaurantInfo(String requestTag, final GetRestaurantInfoCallback callback) {

        String url = ApiCall.ABOUT_US;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getRestaurantInfoCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getRestaurantInfoCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                return params;
            }

        };

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Call Login
    public static void callLogin(final String isFacebookLogin, final String email, final String password, String requestTag,
                                 final LoginCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.USER_LOGIN;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.loginCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.loginCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("is_facebook", isFacebookLogin);
                params.put("username", email);
                params.put("password_hash", password);
                params.put("language", deviceLocale);

                return params;
            }
        };

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Call Facebook Login
    public static void callFacebookLogin(final String isFacebookLogin, final String fbId, final String email,
                                         final String F_firstName, final String F_lastName,
                                         final String imagefile, String requestTag,
                                         final FacebookLoginCallback callback) {

        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.USER_LOGIN;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.facebookLoginCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.facebookLoginCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("is_facebook", isFacebookLogin);
                params.put("fb_id", fbId);
                params.put("fb_email", email);
                params.put("fb_first_name", F_firstName);
                params.put("fb_last_name", F_lastName);
                params.put("fb_image_path", imagefile);
                params.put("language", deviceLocale);
                return params;
            }
        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Call Forgot Password
    public static void callForgotPassword(final String emailString, String requestTag,
                                          final ForgotPasswordCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.FORGOT_PASSWORD;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.forgotPasswordCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.forgotPasswordCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("email", emailString);
                params.put("language", deviceLocale);
                return params;
            }

        };

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Call Postal Codes
    public static void callGetPostalCodes(String requestTag, final GetPostalCodesCallback callback) {

        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.POSTAL_CODE_LIST;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getPostalCodesCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getPostalCodesCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                return params;
            }

        };
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Call City List
    public static void callGetCityList(final String postalId, String requestTag, final GetCityListCallback callback) {

        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.CITY_LIST;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getCityListCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getCityListCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("postal_code", postalId);
                return params;
            }

        };
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // User Profile Data
    public static void callGetUserProfileData(String requestTag, final GetUserProfileDataCallback callback) {

        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.GET_USER_PROFILE;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getUserProfileDataCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getUserProfileDataCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("id", ApplicationClass.getInstance().getPrefManager()
                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                return params;
            }
        };

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Delete User Account
    public static void callDeleteUserAccount(String requestTag, final DeleteUserAccountCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.DELETE_ACCOUNT;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.deleteUserAccountCallbackSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR " + error.toString());
                callback.deleteUserAccountCallbackError(error);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("id", ApplicationClass.getInstance().getPrefManager()
                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // My Orders
    public static void callGetMyOrders(String requestTag, final GetMyOrdersCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.VIEW_ORDERS;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getMyOrdersCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getMyOrdersCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                return params;
            }
        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Gift Cards
    public static void callGetAllGiftCards(String requestTag, final GetAllGiftCardsCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        StringRequest strReq = new StringRequest(Request.Method.POST, ApiCall.GET_GIFT_CARDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getAllGiftCardsCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getAllGiftCardsCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("user_id", ApplicationClass.getInstance().getPrefManager()
                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Contact Us
    public static void callContactUs(final Map<String, String> params, String requestTag, final ContactUsCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);
        params.put("language", deviceLocale);

        String url = ApiCall.CONTACT_US;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.contactUsCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.contactUsCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("email", email);
////                params.put("password", password);
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                params.put("first_name", firstName);
//                params.put("last_name", lastName);
//                params.put("email", email);
//                params.put("address", address);
//                params.put("telephone", phonoNO);
//                params.put("postal_code", postalCode);
//                params.put("message", message);
//                params.put("send_copy", sendMailCopy);

                Log.e("params", "params : " + params);
                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Contact Us Settings
    public static void callContactUsSettings(final String userId, String requestTag, final ContactUsSettingsCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.GET_CONTACT_SETTING;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.contactUsSettingsCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.contactUsSettingsCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);

                if (userId != null)
                    params.put("user_id", userId);
                else
                    params.put("user_id", "");

                return params;
            }

        };
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // About Us
    public static void callGetAboutUs(String requestTag, final AboutUsCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.ABOUT_US;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getAboutUsCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getAboutUsCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                return params;
            }
        };

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Video Link How It Works
    public static void callGetVideoLinkHowItWorks(String requestTag, final VideoLinkHowItWorksCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.HOW_IT_WORKS_VIDEO;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.videoLinkCallbackSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR " + error.toString());
                callback.videoLinkCallbackError(error);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                return params;
            }

        };

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // All Payment Methods
    public static void callGetAllPaymentMethods(String requestTag, final GetAllPaymentMethodsCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.GET_PAYMENT_METHD;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getAllPaymentMethodsCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getAllPaymentMethodsCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
//                params.put("user_id", ApplicationClass.getInstance().getPrefManager()
//                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Call Change Password
    public static void callChangePassword(String oldPasswordString, String newPasswordString,
                                          String requestTag, final ChangePasswordCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);
        String url = ApiCall.CHANGE_PASSWORD;

        final Map<String, String> params = new HashMap<>();
        params.put("r_token", ApiCall.R_TOKEN);
        params.put("r_key", ApiCall.R_KEY);
        params.put("old_password", oldPasswordString);
        params.put("newpassword", newPasswordString);
        params.put("id", ApplicationClass.getInstance().getPrefManager()
                .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
        params.put("language", deviceLocale);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.changePasswordCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.changePasswordCallbackError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(request, requestTag);
    }

    // Call Get Opening Hours
    public static void callGetOpeningHours(String requestTag, final GetOpeningHoursCallback callback) {

        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.GET_OPENIG_HOUR;

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getOpeningHoursCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getOpeningHoursCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("tm_id", "");
                params.put("language", deviceLocale);
                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // sendRegistrationToServer
    public static void sendRegistrationToServer(final String token, String requestTag,
                                                final SendRegistrationToServerCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ApiCall.SET_FCM_TOKEN_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Success " + response);
                callback.sendRegistrationToServerCallbackSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR " + error.toString());
                callback.sendRegistrationToServerCallbackError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("token", token);
                params.put("device_type", "Android");
                if (ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.KEY_USER_ID) != null)
                    params.put("user_id", ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                return params;
            }
        };

//        int socketTimeout = 30000;//30 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Clear Cart
    public static void callClearCart(String requestTag, final ClearCartCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.CLEAR_ALL_CART;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.clearCartCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.clearCartCallbackError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                if (ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.KEY_USER_ID) != null)
                    params.put("customer_id", ApplicationClass.getInstance().getPrefManager().
                            getStringPreferences(MyPreferenceManager.KEY_USER_ID));

                if (ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID) != null)
                    params.put("ip", ApplicationClass.getInstance().getPrefManager().
                            getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));

                return params;
            }
        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Check Restaurant Is Closed
    public static void checkRestaurantIsClosed(String requestTag, final RestaurantIsClosedCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.RESTAURANT_CLOSED;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.restaurantIsClosedCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.restaurantIsClosedCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("language", deviceLocale);
                return params;
            }

        };

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Product List
    public static void getProductList(String requestTag, final ProductListCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.MENU_LIST;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getProductListCallbackSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR " + error.toString());
                callback.getProductListCallbackError(error);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                if (ApplicationClass.getInstance().getPrefManager()
                        .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN))
                    params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
                            .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Product Search List
    public static void getProductSearchList(final String searchText, String requestTag, final ProductSearchListCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.SEARCH_PRODUCT;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getProductSearchListCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getProductSearchListCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("searchTerm", searchText);
                return params;
            }
        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Get Product Description
    public static void getProductDescription(final String product_id, String requestTag, final ProductDescriptionCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.PRODUCT_DETAIL_BY_ID;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.getProductDescriptionCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getProductDescriptionCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("p_id", product_id);
                return params;
            }
        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Get Product Description
    public static void getProductExtraToppings(JSONObject jsonObject, final ListView expndnd,
                                               final TextView ectratopping_ind, final int parentPos,
                                               final ArrayList<ProductAttributeValue> arrayAttribute,
                                               String requestTag, final ProductExtraToppingsCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.EXTRATOPPING_DETAILS;
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Success " + response);
                        callback.getProductExtraToppingsCallbackSuccess(response, expndnd, ectratopping_ind, parentPos,
                                arrayAttribute);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.getProductExtraToppingsCallbackError(error);
                    }
                }
        );
        // Adding request to volley request queue
        ApplicationClass.getInstance().addToRequestQueue(jsonReq, requestTag);
    }

    // Add to Cart
    public static void callAddToCart(JSONObject jsonData, String requestTag, final AddToCartCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        try {
            jsonData.accumulate("language", deviceLocale);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ApiCall.ADD_TO_CART;
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, url, jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Success " + response);
                        callback.addToCartCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.addToCartCallbackError(error);
                    }
                }
        );
        // Adding request to volley request queue
        ApplicationClass.getInstance().addToRequestQueue(jsonReq, requestTag);
    }

    // View Cart
    public static void callViewCart(JSONObject jsonParams, String requestTag, final ViewCartCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);
        String urlViewCart = ApiCall.VIEW_ALL_CART_ITEM;

        try {
            jsonParams.accumulate("language", deviceLocale);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest viewCartJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlViewCart, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Success " + response);
                        callback.viewCartCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.viewCartCallbackError(error);
                    }
                });
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(viewCartJsonObjectRequest, requestTag);
    }

    // Delete Item From Cart
    public static void callDeleteItemFromCart(final String opId, String requestTag, final DeleteItemFromCartCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String urlDelete = ApiCall.DELETE_CART_ITEM;
        StringRequest strReq = new StringRequest(Request.Method.POST, urlDelete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.deleteItemFromCartCallbackSuccess(response, opId);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR " + error.toString());
                callback.deleteItemFromCartCallbackError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);

                if (ApplicationClass.getInstance().getPrefManager()
                        .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN)) {
                    params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
                            .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                    params.put("is_login", "1");
                } else {
                    params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
                            .getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));
                    params.put("is_login", "0");
                }

//                    params.put("order_no", MainActivity.ORDER_NO);
                params.put("op_id", opId);
                params.put("ip", ApplicationClass.getInstance().getPrefManager()
                        .getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));
                Log.e("param", "param  " + params);
                return params;
            }
        };

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Update Product Cart Quantity
    public static void callUpdateProductCartQuantity(final String op_id, final String qtyflag,
                                                     final int itemPos, final TextSwitcher itemCountText,
                                                     final String requestTag, final UpdateProductCartQuantityCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        StringRequest strReq = new StringRequest(Request.Method.POST, ApiCall.PRODUCT_QTY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.updateProductCartQuantityCallbackSuccess(response, itemPos, itemCountText);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.updateProductCartQuantityCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("op_id", op_id);
                params.put("qtyflag", qtyflag);
//                params.put("order_no", MainActivity.ORDER_NO);
                params.put("ip", ApplicationClass.getInstance().getPrefManager()
                        .getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));

                if (ApplicationClass.getInstance().getPrefManager()
                        .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN)) {
                    params.put("is_login", "1");
                    params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
                            .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                } else {
                    params.put("is_login", "0");
                    params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
                            .getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));
                }
                return params;
            }
        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Checkout
    public static void callCheckout(JSONObject jsonData, String requestTag, final CheckoutCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        try {
            jsonData.accumulate("language", deviceLocale);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ApiCall.CART_CHECKOUT;
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, url, jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Success " + response);
                        callback.checkoutCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.checkoutCallbackError(error);
                    }
                });
//        int socketTimeout = 60000;//30 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonReq.setRetryPolicy(policy);
        ApplicationClass.getInstance().addToRequestQueue(jsonReq, requestTag);
    }

    // Order Details For Checkout
    public static void callOrderDetailsForCheckout(String requestTag, final OrderDetailsForCheckoutCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.MY_ORDER_DETAILS;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.orderDetailsForCheckoutCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.orderDetailsForCheckoutCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
//                params.put("order_no", MainActivity.ORDER_NO);
                params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                params.put("language", deviceLocale);

                Log.e("params", "params  " + params);

                return params;
            }
        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Pick Up Delivery Time
    public static void callPickUpDeliveryTime(final String pick_Type, String requestTag, final PickUpDeliveryTimeCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.RESTAURANT_PD_TIME;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.pickUpDeliveryTimeCallbackSuccess(response, pick_Type);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.pickUpDeliveryTimeCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("shipping", pick_Type);
                params.put("language", deviceLocale);
                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Shipping Cost
    public static void callShippingCost(JSONObject jsonParams, String requestTag, final ShippingCostCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        try {
            jsonParams.accumulate("language", deviceLocale);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ApiCall.GET_NEW_SHIPPING_COST;
        JsonObjectRequest getShippingCostRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Success " + response);
                        callback.shippingCostCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.shippingCostCallbackError(error);
                    }
                });

        ApplicationClass.getInstance().addToRequestQueue(getShippingCostRequest, requestTag);
    }

    // Apply Code of Gift Card
    public static void callApplyCodeGiftCard(final Map<String, String> params, String requestTag, final ApplyCodeGiftCardCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        params.put("language", deviceLocale);

        String url = ApiCall.APPLY_CODE;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.applyCodeGiftCardCallbackSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR " + error.toString());
                callback.applyCodeGiftCardCallbackError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                params.put("order_total", "" + subTotalDouble);
//                params.put("code", promoId);
//                if (isDeliveryFlag)
//                    params.put("shipping", "Delivery");
//                else
//                    params.put("shipping", "Pickup");
////                if (floorDoorString != null && !floorDoorString.isEmpty())
////                    params.put("address", street + " " + floorDoorString + ", " + postal_code + " " + city);
////                else
//                if (shippingTypeFlag.equalsIgnoreCase("1")) {
//                    params.put("address", street + ", " + postal_code + " " + city);
//                } else if (shippingTypeFlag.equalsIgnoreCase("0")) {
//                    params.put("address", streetNumberOriginal + ", " + postalCodeOriginal + " " + cityOriginal);
//                }
//                params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
//                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));

                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Order Transaction
    public static void callOrderTransaction(JSONObject jsonParams, String requestTag, final OrderTransactionCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        try {
            jsonParams.accumulate("language", deviceLocale);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ApiCall.ORDER_TRANSACTION;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Success " + response);
                        callback.orderTransactionCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.orderTransactionCallbackError(error);
                    }
                });

        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(jsonObjectRequest, requestTag);
    }

    // Order Transaction
    public static void callCancelOrderTransaction(String requestTag, final CancelOrderTransactionCallback callback) {

        String url = ApiCall.CANCEL_ORDER_TRANSACTION;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.cancelOrderTransactionCallbackSuccess(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.cancelOrderTransactionCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("order_no", MainActivity.ORDER_NO);

                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }

    // Communicating With Restaurant
    public static void callCommunicatingWithRestaurant(final String orderNo, final String requestTag, final CommunicatingWithRestaurantCallback callback) {
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String url = ApiCall.COMMUNICATING_WITH_RESTRO;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Success " + response);
                        callback.communicatingWithRestaurantCallbackSuccess(response, requestTag);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR " + error.toString());
                        callback.communicatingWithRestaurantCallbackError(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("order_no", orderNo);
                params.put("language", deviceLocale);

                return params;
            }

        };
        // Adding request to request queue
        ApplicationClass.getInstance().addToRequestQueue(strReq, requestTag);
    }
}
