package dk.eatmore.demo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.activity.FacebookSignUpActivity;
import dk.eatmore.demo.activity.NeedHelpActivity;
import dk.eatmore.demo.activity.NewUserActivity;
import dk.eatmore.demo.interfaces.FacebookLoginCallback;
import dk.eatmore.demo.interfaces.ForgotPasswordCallback;
import dk.eatmore.demo.interfaces.GetRestaurantInfoCallback;
import dk.eatmore.demo.interfaces.LoginCallback;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.SharedPrefClass;
import dk.eatmore.demo.myutils.Utility;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import dk.eatmore.demo.network.NetworkUtils;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pallavi.b on 02-May-16.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, ForgotPasswordCallback, LoginCallback, FacebookLoginCallback, GetRestaurantInfoCallback {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private TextView txtNeedHelp, txtTitle;
    //For custom actionbar
    private TextView mtxtNewUser;
    private Button mLoginBtn;
    private Intent intent;

    View rootView;
    private Fragment fragment;
    private Context context;
    private ImageView imgback;
    // private RippleView rippleView;
    private TextView txtForgotPassword, restroNowithoutLogin;

    private SimpleArcDialog mDialog;
    private AlertDialog forgotDialog;
    //for custom alert dialog

    private static final int DLG_EXAMPLE1 = 0;
    private static final int TEXT_ID = 0;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    LinearLayout mainlladdresss, mainllOpeningHours, mainllcall;
    String strLoginMail, strLoginPass;
    EditText inputUserEmail, inputUserPassword;
    CallbackManager callbackManager;

    Button facebookUserLogin;
    LoginButton login_buttonUser;
    String first_name, last_name, facebookemail, fbid, userfile;
    boolean is_from_cart = false;
    ImageView splashMainresImage;

    private SharedPrefClass mSharedPrefClass = null;
//    private String forgotPasswordEmail = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();

        rootView = inflater.inflate(R.layout.fragment_login, parent, false);
        setupUI(rootView.findViewById(R.id.signupcoordinatorLayout));
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();

        Bundle bundle = this.getArguments();
        if (bundle != null)
            is_from_cart = bundle.getBoolean(MyCartCheckOut.IS_FROM_CART, false);

        mSharedPrefClass = new SharedPrefClass(getActivity());

        //    loginLayout= (LinearLayout) rootView.findViewById(R.id.loginLayout);
        //initialization
        //  imgback = (ImageView) rootView.findViewById(R.id.imgback);
        //   rippleView = (RippleView) rootView.findViewById(R.id.rippleView);
        txtForgotPassword = (TextView) rootView.findViewById(R.id.txtForgotPassword);

        init();//initialization
        onClickListener();//OnClickListener

        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        rootView.setOnClickListener(this);
        rootView.setOnTouchListener(gestureListener);

        onClickListener();
        getRestroInfo();

        return rootView;

    }

    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Utility.hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void init() {
        splashMainresImage = (ImageView) rootView.findViewById(R.id.splashMainresImage);
        inputUserEmail = (EditText) rootView.findViewById(R.id.inputUserEmail);
        inputUserPassword = (EditText) rootView.findViewById(R.id.inputUserPassword);

        mainllcall = (LinearLayout) rootView.findViewById(R.id.mainllcall);
        mainllOpeningHours = (LinearLayout) rootView.findViewById(R.id.mainllOpeningHours);
        mainlladdresss = (LinearLayout) rootView.findViewById(R.id.mainlladdresss);
        //  txtSwipe = (TextView) rootView.findViewById(R.id.txtSwipe);
        mtxtNewUser = (TextView) rootView.findViewById(R.id.txtNewUser);
        mLoginBtn = (Button) rootView.findViewById(R.id.btnUserLogin);
        txtForgotPassword = (TextView) rootView.findViewById(R.id.txtForgotPassword);
        txtNeedHelp = (TextView) rootView.findViewById(R.id.txtNeedHelp);

        restroNowithoutLogin = (TextView) rootView.findViewById(R.id.restroNowithoutLogin);

        login_buttonUser = (LoginButton) rootView.findViewById(R.id.login_buttonUser);
        facebookUserLogin = (Button) rootView.findViewById(R.id.facebookUserLogin);
        login_buttonUser.setReadPermissions("email");
        login_buttonUser.setFragment(this);


        TextView title = (TextView) rootView.findViewById(R.id.tootlbar_title);
        title.setText("Login");
        imgback = (ImageView) rootView.findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
            }
        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isNetworkConnected(getActivity()))
                    if (getValidation())
                        checkLogin(strLoginMail, strLoginPass, "0");
                    else
                        showDialogInternetUnavailableLogin();
            }
        });
    }

    private boolean getValidation() {
        // Getting Values from EditTexts
        strLoginMail = inputUserEmail.getText().toString().trim();
        strLoginPass = inputUserPassword.getText().toString().trim();

        if (strLoginMail.isEmpty()) { // Check Email Address
            inputUserEmail.setError(getString(R.string.email_blank));
            inputUserEmail.requestFocus();
            return false;
        } else if (!isValidEmail(strLoginMail)) {
            inputUserEmail.setError(getString(R.string.invalid_email));
            inputUserEmail.requestFocus();
            return false;
        } else if (strLoginPass.isEmpty()) { // Check Password
            inputUserPassword.setError(getString(R.string.password_empty));
            inputUserPassword.requestFocus();
            return false;
        }

        return true;
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void checkLogin(final String email, final String password, final String isFacebookLogin) {
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        mDialog.show();

        NetworkUtils.callLogin(isFacebookLogin, email, password, tag_string_req, LoginFragment.this);
    }

    @Override
    public void loginCallbackSuccess(String success) {
        Log.e(TAG, "Login Response: " + success);
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {

//                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                ApplicationClass.getInstance().getPrefManager().
                        setBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN, true);
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.KEY_USER_ID, jObj.getJSONObject("user_details").getString("id"));

                LoginManager.getInstance().logOut();

                mSharedPrefClass.setFacebookLogin(false);
                reDirectAfterLogin();


            } else {
//                        if(jObj.getString("error_id").equals("1"))
//                        {
//                            showAlert(jObj.getString("msg"),false);
//                        }
//                        else
//                        {
//                            String errorKey=jObj.getString("errorkey");
//                            showAlert(jObj.getJSONObject("errors").getString(errorKey),false);
//                        }

                String msg = jObj.getString("msg");
                showAlert(msg, false);
//                showAlert(getResources().getString(R.string.login_error_msg), false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void loginCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        // Log.e(TAG, "Login Error: " + error.getMessage());
//        Toast.makeText(getActivity(),
//                R.string.try_again, Toast.LENGTH_LONG).show();
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableLogin();
        }
    }

    public void showDialogInternetUnavailableLogin() {
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
                if (getValidation())
                    checkLogin(strLoginMail, strLoginPass, "0");
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

    private void reDirectAfterLogin() {
        // Showing Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).showBottomNavigation();

        if (is_from_cart) {
            try {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new CartFragment());
//                fragmentTransaction.addToBackStack(LoginFragmentMain.class.getName());
                fragmentTransaction.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("Exception ", "Exception " + ex);
            }

            Log.e(TAG, "Cart called");
//            Toast.makeText(getActivity(), "Cart called", Toast.LENGTH_SHORT).show();
        } else {
//            getActivity().getSupportFragmentManager().popBackStack();
            // TODO: 1/30/2017 Commented Code
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new MenuFragment());
//            fragmentTransaction.addToBackStack(LoginFragment.class.getName());
            fragmentTransaction.commit();

            ((MainActivity) getActivity()).setCurrentItemOfBottomNav(1);
        }
    }

    private void getRestroInfo() {
        String tag_string_req = "getRestroInfo";
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();

        NetworkUtils.callGetRestaurantInfo(tag_string_req, LoginFragment.this);
    }

    @Override
    public void getRestaurantInfoCallbackSuccess(String success) {
        //  Log.e(TAG, "forgetPassword Response: " + response.toString());
        mDialog.dismiss();

        try {
            JSONObject data = new JSONObject(success);
            if (data.getBoolean("status")) {
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.PREF_NAME, data.getString("restaurant_name"));
                String image = data.getString("logo_path") + data.getJSONObject("data").getString("logo");

                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE, image);

                Picasso.with(getActivity())
                        .load(image)
                        //.transform(new CircleTransformation())
                        .into(splashMainresImage);

                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.restaurant_ADDRESS, data.getJSONObject("data").getString("address"));
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.restaurant_num, data.getJSONObject("data").getString("phone_no"));


                restroNowithoutLogin.setText(data.getJSONObject("data").getString("phone_no"));

            } else
                Toast.makeText(getActivity(), data.getString("msg"), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getRestaurantInfoCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    Toast.makeText(getActivity(), "Left Swipe", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Left Swipe");
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    Toast.makeText(getActivity(), "Right Swipe", Toast.LENGTH_SHORT).show();
                    // Showing Bottom Bar as it is Login screen
                    ((MainActivity) getActivity()).showBottomNavigation();
                    getActivity().getSupportFragmentManager().popBackStack();

                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    private void onClickListener() {

        facebookUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_buttonUser.performClick();
            }
        });
        login_buttonUser.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.e("loginResult", "loginResult " + loginResult.toString());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Log.e("facebook", "Response " + response.toString());

                                    JSONObject fbJsonObject = response.getJSONObject();

                                    if (fbJsonObject.has("email"))
                                        facebookemail = fbJsonObject.getString("email");
                                    else
                                        facebookemail = "";

                                    if (fbJsonObject.has("first_name"))
                                        first_name = fbJsonObject.getString("first_name");
                                    else
                                        first_name = "";

                                    if (fbJsonObject.has("last_name"))
                                        last_name = fbJsonObject.getString("last_name");
                                    else
                                        last_name = "";

                                    if (fbJsonObject.has("id"))
                                        fbid = fbJsonObject.getString("id");
                                    else
                                        fbid = "";


                                    if (Profile.getCurrentProfile() != null)
                                        userfile = "" + Profile.getCurrentProfile().getProfilePictureUri(200, 200);
                                        //Log.e("Response","userfile "+userfile);
                                    else
                                        userfile = "";

                                    checkFacebookLogin(facebookemail, facebookemail, "1", fbid, first_name, last_name, userfile);
                                } catch (Exception e) {
                                    Log.e("e", "e " + e);
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.e("onCancel", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("exception", "exception" + exception);
            }
        });

        mainllcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = ApplicationClass.getInstance().getPrefManager()
                        .getStringPreferences(MyPreferenceManager.restaurant_num);
                if (phone != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();


            }
        });

        mainllOpeningHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, new OpeningHoursAlertClass());
                fragmentTransaction.addToBackStack(LoginFragmentMain.class.getName());
                fragmentTransaction.commit();
            }
        });


        mainlladdresss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String map = "https://maps.google.dk/maps?q=" + ApplicationClass.getInstance().getPrefManager()
                        .getStringPreferences(MyPreferenceManager.restaurant_ADDRESS);

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(i);


//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.add(R.id.frgamentContainer, new Direction());
//                fragmentTransaction.addToBackStack(LoginFragmentMain.class.getName());
//                fragmentTransaction.commit();
            }
        });


        mtxtNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewUserActivity.class);
                startActivity(i);

            }
        });
        txtNeedHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NeedHelpActivity.class);
                startActivity(i);

            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotPasswordDialog();
//                new LovelyTextInputDialog(getActivity(), R.style.TintTheme)
//                        .setTopColorRes(R.color.green)
//                        .setTitle(R.string.forgot_password)
//                        .setMessage(R.string.enter_your_email_address)
//                        //  .setIcon(R.drawable.ic_add)
//                        .setInputFilter(R.string.enter_your_email_address, new LovelyTextInputDialog.TextFilter() {
//                            @Override
//                            public boolean check(String text) {
//                                // return text.matches("\\w+");
//
//                                Pattern pattern = Patterns.EMAIL_ADDRESS;
//                                return pattern.matcher(text).matches();
//                            }
//                        })
//                        .setConfirmButton(R.string.login_ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
//                            @Override
//                            public void onTextInputConfirmed(String text) {
//
//                                forgetPassword(text);
//                            }
//                        })
//                        .show();
            }
        });

//        facebookUserLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(getValidation())
//                    checkLogin(strLoginMail,strLoginPass,"0");
//            }
//        });


    }

    private void forgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.forgot_password_view, null);

        Button buttonForgetPass = (Button) view.findViewById(R.id.buttonForgetPass);
        final EditText inputForgetPass = (EditText) view.findViewById(R.id.inputForgetPass);
        builder.setView(view);
        forgotDialog = builder.create();
        forgotDialog.show();

        buttonForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forgotPasswordEmail = inputForgetPass.getText().toString().trim();
                if (forgotPasswordEmail.isEmpty()) {
                    inputForgetPass.setError(getString(R.string.email_blank));
                } else if (!isValidEmail(forgotPasswordEmail)) {
                    inputForgetPass.setError(getString(R.string.invalid_email));
                } else {
                    inputForgetPass.setError(null);
//                    if (Utility.isNetworkConnected(getActivity())) {
                    forgetPassword(forgotPasswordEmail);
                    forgotDialog.dismiss();
//                    } else
//                        Utility.showInterNetMsg(getActivity());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void checkFacebookLogin(final String email, final String password, final String isFacebookLogin,
                                    final String fbId, final String F_firstName, final String F_lastName, final String imagefile) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();

        NetworkUtils.callFacebookLogin(isFacebookLogin, fbId, email, F_firstName, F_lastName,
                imagefile, tag_string_req, LoginFragment.this);
    }

    @Override
    public void facebookLoginCallbackSuccess(String success) {
        Log.e(TAG, "Login Response: " + success);
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        LoginManager.getInstance().logOut();
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {

                if (jObj.getString("fb_new_user").equals("0")) {

                    // Already Registered User
//                                    Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                    ApplicationClass.getInstance().getPrefManager().
                            setBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN, true);
                    ApplicationClass.getInstance().getPrefManager().
                            setStringPreferences(MyPreferenceManager.KEY_USER_ID, jObj.getJSONObject("user_details").getString("id"));

                    // TODO: 1/30/2017 I think Below Broacast is to clear the cart on Login with fb check and remove
                    Intent intent = new Intent("CartCount");
                    // You can also include some extra data.
                    intent.putExtra("count", "007");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

                    mSharedPrefClass.setFacebookLogin(true);

                    reDirectAfterLogin();
                } else {
                    // New User FB
                    newFBUser();
//                                    parseJsonFeed(jObj);
                }

            } else {
                if (jObj.getString("error_id").equals("1"))
                    showAlert(jObj.getString("msg"), false);

                else {
                    String errorKey = jObj.getString("errorkey");
                    showAlert(jObj.getJSONObject("errors").getString(errorKey), false);
                }
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            //Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void facebookLoginCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        LoginManager.getInstance().logOut();
        // Log.e(TAG, "Login Error: " + error.getMessage());
        Toast.makeText(getActivity(),
                R.string.try_again, Toast.LENGTH_LONG).show();
    }

    private void newFBUser() {
        Intent intent = new Intent(getActivity(), FacebookSignUpActivity.class);
        intent.putExtra("FBId", fbid);
        intent.putExtra("firstName", first_name);
        intent.putExtra("lastName", last_name);
        intent.putExtra("email", facebookemail);
        intent.putExtra("userFaceBookImage", userfile);

        if (is_from_cart)
            intent.putExtra(MyCartCheckOut.IS_FROM_CART, true);
        else
            intent.putExtra(MyCartCheckOut.IS_FROM_CART, false);

        startActivity(intent);
        //  getActivity().finish();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void parseJsonFeed(JSONObject jsonresponse) {

        try {

            Log.e("edit p", "edit  " + jsonresponse);

            String userId = jsonresponse.getJSONObject("data").getString("id");

            ApplicationClass.getInstance().getPrefManager().
                    setStringPreferences(MyPreferenceManager.KEY_USER_ID, userId);

            Intent intent = new Intent(getActivity(), FacebookSignUpActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("userFaceBookImage", userfile);

            if (is_from_cart)
                intent.putExtra(MyCartCheckOut.IS_FROM_CART, true);
            else
                intent.putExtra(MyCartCheckOut.IS_FROM_CART, false);

            startActivity(intent);
            //  getActivity().finish();
            getActivity().getSupportFragmentManager().popBackStack();

        } catch (Exception ex) {
            Log.e("error facebook", "facebook " + ex.toString());
        }

    }

    private void forgetPassword(final String emailId) {
        String tag_string_req = "forgetPassword";
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();

        NetworkUtils.callForgotPassword(emailId, tag_string_req, LoginFragment.this);
    }

    @Override
    public void forgotPasswordCallbackSuccess(String success) {
        Log.e(TAG, "forgetPassword Response: " + success);
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        try {
            JSONObject data = new JSONObject(success);
            if (data.getBoolean("status"))
                showAlert("A new password has been sent to your e-mail address." +
                        " Please check your mail for further instructions.", false);

            else
                Toast.makeText(getActivity(), data.getString("msg"), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void forgotPasswordCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableForgotPassword();
        }
    }

    public void showDialogInternetUnavailableForgotPassword() {
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
                forgotDialog.dismiss();
//                forgetPassword(forgotPasswordEmail);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetAlertDialog.dismiss();
                forgotDialog.dismiss();
                getActivity().finish();
            }
        });
    }

    public void showAlert(String message, final boolean newUserFlag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        View titleview = getActivity().getLayoutInflater().inflate(R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(R.id.dialogtitle);
        title.setText(R.string.alert);
        builder.setCustomTitle(titleview);

        TextView myMsg = new TextView(getActivity());
        myMsg.setText(message);
        myMsg.setGravity(Gravity.START);
        myMsg.setPadding(15, 15, 15, 15);
        myMsg.setTextColor(Color.BLACK);
        builder.setView(myMsg);


        //  builder.setMessage(message)

        builder.setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (newUserFlag) {
                            Intent i = new Intent(getActivity(), NewUserActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        } else
                            dialog.dismiss();
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
