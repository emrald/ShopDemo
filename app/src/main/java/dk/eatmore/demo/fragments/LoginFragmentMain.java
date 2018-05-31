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
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.activity.FacebookSignUpActivity;
import dk.eatmore.demo.activity.FadeAnimationActivity;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.activity.NeedHelpActivity;
import dk.eatmore.demo.activity.NewUserActivity;
import dk.eatmore.demo.interfaces.FacebookLoginCallback;
import dk.eatmore.demo.interfaces.ForgotPasswordCallback;
import dk.eatmore.demo.interfaces.GetRestaurantInfoCallback;
import dk.eatmore.demo.interfaces.LoginCallback;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.PasswordView;
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
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pallavi.b on 02-May-16.
 */
public class LoginFragmentMain extends Fragment implements View.OnClickListener,
        ForgotPasswordCallback, LoginCallback, FacebookLoginCallback, GetRestaurantInfoCallback {

    private View rootView;
    private LinearLayout loginLayout, mainlladdresss, mainllOpeningHours, mainllcall;
    private TextView restroNo;
    private TextView txtNeedHelp;
    //For custom actionbar
    private TextView mtxtNewUser;
    private Intent intent;
    long totalSize = 0;

    private AlertDialog forgotDialog;
    private Button btnLogin;
    private TextView txtForgotPassword;

    //for custom alert dialog
    private static final String TAG = "LoginActivity";

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;
    private ShimmerTextView shimmerText;
    private SimpleArcDialog mDialog;
    private EditText inputLoginMail, inputLoginPass;
    private String strLoginMail, strLoginPass;
    private Button facebookLoginButton;
    private LoginButton facebookLogin;
    private CallbackManager callbackManager;
    private String first_name, last_name, facebookemail, fbid, userfile;
    private ImageView splashMainresImage;
    private SharedPrefClass mSharedPrefClass = null;

//    private String forgotPasswordEmail = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
        rootView = inflater.inflate(dk.eatmore.demo.R.layout.fragment_login_main, parent, false);
        setupUI(rootView);
//        setupUI(rootView.findViewById(R.id.signupcoordinatorLayout));
        initView();//initialization
        initObjects();
        initGesture();
        onClickListener();
        //   iitDialog();
        initShimmerEffect();
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

    private void initView() {
        splashMainresImage = (ImageView) rootView.findViewById(dk.eatmore.demo.R.id.splashMainresImageWlogin);
        restroNo = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.loginrestoNo);
        mainllcall = (LinearLayout) rootView.findViewById(dk.eatmore.demo.R.id.mainllcallfragment);
        mainllOpeningHours = (LinearLayout) rootView.findViewById(dk.eatmore.demo.R.id.mainllOpeningHoursfragment);
        mainlladdresss = (LinearLayout) rootView.findViewById(dk.eatmore.demo.R.id.mainlladdresssfragment);
        mtxtNewUser = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.txtNewUser);
        txtForgotPassword = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.txtForgotPassword);
        txtNeedHelp = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.txtNeedHelp);
        btnLogin = (Button) rootView.findViewById(dk.eatmore.demo.R.id.btnLogin);

        inputLoginMail = (EditText) rootView.findViewById(dk.eatmore.demo.R.id.edttxtLoginMail);
        inputLoginPass = (PasswordView) rootView.findViewById(dk.eatmore.demo.R.id.edittextLoginPass);

        facebookLogin = (LoginButton) rootView.findViewById(dk.eatmore.demo.R.id.login_button);
        facebookLoginButton = (Button) rootView.findViewById(dk.eatmore.demo.R.id.facebookLogin);
        facebookLogin.setReadPermissions("email");
        facebookLogin.setFragment(this);
    }

    private void initObjects() {
        mSharedPrefClass = new SharedPrefClass(getActivity());
    }

    private void initShimmerEffect() {
        try {
            Shimmer shimmer = new Shimmer();
            shimmer.setDuration(5200).setStartDelay(2000).start(shimmerText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initGesture() {
        loginLayout = (LinearLayout) rootView.findViewById(dk.eatmore.demo.R.id.loginLayout);
        txtForgotPassword = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.txtForgotPassword);
        shimmerText = (ShimmerTextView) rootView.findViewById(dk.eatmore.demo.R.id.shimmer_tv);

        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        loginLayout.setOnTouchListener(gestureListener);
    }

    @Override
    public void onClick(View view) {

    }

    private void getRestroInfo() {
        String tag_string_req = "getRestroInfo";
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();

        NetworkUtils.callGetRestaurantInfo(tag_string_req, LoginFragmentMain.this);
    }

    @Override
    public void getRestaurantInfoCallbackSuccess(String success) {
        Log.e(TAG, "ABOUT_US ABOUT_US: " + success);
        mDialog.dismiss();

        try {
            JSONObject data = new JSONObject(success);
            if (data.getBoolean("status")) {
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.RESTAURANT_NAME, data.getString("restaurant_name"));

                String image = data.getString("logo_path") + data.getJSONObject("data").getString("logo");

                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE, image);


                // TODO: 1/30/2017 Handle splashMainresImage==null when user moves to next screen and activity is destroyed
                Picasso.with(getActivity())
                        .load(image)
                        //.transform(new CircleTransformation())
                        .into(splashMainresImage);

                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.restaurant_ADDRESS, data.getJSONObject("data").getString("address"));
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.restaurant_num, data.getJSONObject("data").getString("phone_no"));


                restroNo.setText(data.getJSONObject("data").getString("phone_no"));

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
                    Log.e(TAG, "Swipe Left");
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Intent i = new Intent(getActivity(), FadeAnimationActivity.class);
                    startActivity(i);
                    // getActivity().overridePendingTransition(R.anim.enter, R.anim.leave);
                }
            } catch (Exception e) {
                // nothing
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    private void onClickListener() {

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utility.isNetworkConnected(getActivity()))
                    facebookLogin.performClick();
                else
                    Utility.showInterNetMsg(getActivity());
            }
        });
        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Log.e("Response", "Response " + response.toString());

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
                Log.e("onCancel", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("Param" + exception, "exception" + exception);

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
                    Toast.makeText(getActivity(), dk.eatmore.demo.R.string.try_again, Toast.LENGTH_SHORT).show();
            }
        });

        mainllOpeningHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isNetworkConnected(getActivity())) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(dk.eatmore.demo.R.id.frgamentContainer, new OpeningHoursAlertClass());
                    fragmentTransaction.addToBackStack(LoginFragmentMain.class.getName());
                    fragmentTransaction.commit();
                } else
                    Utility.showInterNetMsg(getActivity());

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
                if (Utility.isNetworkConnected(getActivity())) {
                    Intent i = new Intent(getActivity(), NeedHelpActivity.class);
                    startActivity(i);
                } else
                    Utility.showInterNetMsg(getActivity());
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (Utility.isNetworkConnected(getActivity())) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(dk.eatmore.demo.R.layout.forgot_password_view, null);

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
                            inputForgetPass.setError(getString(dk.eatmore.demo.R.string.email_blank));
                        } else if (!isValidEmail(forgotPasswordEmail)) {
                            inputForgetPass.setError(getString(dk.eatmore.demo.R.string.invalid_email));
                        } else {
                            inputForgetPass.setError(null);
//                                if (Utility.isNetworkConnected(getActivity())) {
                            forgetPassword(forgotPasswordEmail);
                            forgotDialog.dismiss();
//                                } else
//                                    showDialogInternetUnavailableForgotPassword();
                        }
                    }
                });
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

//                } else
//                    Utility.showInterNetMsg(getActivity());

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isNetworkConnected(getActivity())) {
                    if (getValidation())
                        checkLogin(strLoginMail, strLoginPass, "0");
                } else
                    showDialogInternetUnavailableLogin();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean getValidation() {
        // Getting Values from EditTexts
        strLoginMail = inputLoginMail.getText().toString().trim();
        strLoginPass = inputLoginPass.getText().toString().trim();

        if (strLoginMail.isEmpty()) { // Check Email Address
            inputLoginMail.setError(getString(dk.eatmore.demo.R.string.email_blank));
            inputLoginMail.requestFocus();
            return false;
        } else if (!isValidEmail(strLoginMail)) {
            inputLoginMail.setError(getString(dk.eatmore.demo.R.string.invalid_email));
            inputLoginMail.requestFocus();
            return false;
        } else if (strLoginPass.isEmpty()) { // Check Password
            inputLoginPass.setError(getString(dk.eatmore.demo.R.string.password_empty));
            inputLoginPass.requestFocus();
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

    private void forgetPassword(final String emailId) {
        String tag_string_req = "forgetPassword";
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();

        NetworkUtils.callForgotPassword(emailId, tag_string_req, LoginFragmentMain.this);
    }

    @Override
    public void forgotPasswordCallbackSuccess(String success) {
        Log.e(TAG, "forgetPassword Response: " + success);
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        try {
            JSONObject data = new JSONObject(success);
            if (data.getBoolean("status")) {
                String msg = data.getString("msg");
                showAlert(msg, false);
//                showAlert("A new password has been sent to your e-mail address." +
//                        " Please check your mail for further instructions.", false);
            } else {
                String msg = data.getString("msg");
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }

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

    private void checkLogin(final String email, final String password, final String isFacebookLogin) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();

        NetworkUtils.callLogin(isFacebookLogin, email, password, tag_string_req, LoginFragmentMain.this);
    }

    @Override
    public void loginCallbackSuccess(String success) {
        mDialog.dismiss();
        Log.e(TAG, "Login Response: " + success);

        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
//                                Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                ApplicationClass.getInstance().getPrefManager().
                        setBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN, true);
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.KEY_USER_ID, jObj.getJSONObject("user_details").getString("id"));

                mSharedPrefClass.setFacebookLogin(false);

                Intent intent = new Intent(getActivity(),
                        MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
                LoginManager.getInstance().logOut();

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
//                showAlert(getResources().getString(dk.eatmore.demo.R.string.login_error_msg), false);
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            //Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void loginCallbackError(VolleyError volleyError) {
        mDialog.dismiss();
        // Log.e(TAG, "Login Error: " + error.getMessage());
//        Toast.makeText(getActivity(),
//                dk.eatmore.demo.R.string.try_again, Toast.LENGTH_LONG).show();
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

    private void checkFacebookLogin(final String email, final String password, final String isFacebookLogin,
                                    final String fbId, final String F_firstName, final String F_lastName, final String imagefile) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();

        NetworkUtils.callFacebookLogin(isFacebookLogin, fbId, email, F_firstName, F_lastName,
                imagefile, tag_string_req, LoginFragmentMain.this);
    }

    @Override
    public void facebookLoginCallbackSuccess(String success) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        Log.e(TAG, "Login Response: " + success);

        LoginManager.getInstance().logOut();
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {

                if (jObj.getString("fb_new_user").equals("0")) {
//                                    Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                    ApplicationClass.getInstance().getPrefManager().
                            setBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN, true);
                    ApplicationClass.getInstance().getPrefManager().
                            setStringPreferences(MyPreferenceManager.KEY_USER_ID, jObj.getJSONObject("user_details").getString("id"));

                    mSharedPrefClass.setFacebookLogin(true);
                    Intent intent = new Intent(getActivity(),
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();

                } else {
                    // New User FB
                    newFBUser();
//                            parseJsonFeed(jObj);
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
        Toast.makeText(getActivity(), dk.eatmore.demo.R.string.try_again, Toast.LENGTH_LONG).show();
    }

    private void newFBUser() {
        Intent intent = new Intent(getActivity(), FacebookSignUpActivity.class);
        intent.putExtra("FBId", fbid);
        intent.putExtra("firstName", first_name);
        intent.putExtra("lastName", last_name);
        intent.putExtra("email", facebookemail);
        intent.putExtra("userFaceBookImage", userfile);
        intent.putExtra(MyCartCheckOut.IS_FROM_CART, false);
        startActivity(intent);
        getActivity().finish();
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
            intent.putExtra(MyCartCheckOut.IS_FROM_CART, false);
            startActivity(intent);
            getActivity().finish();

        } catch (Exception ex) {
            Log.e("error facebook", "facebook " + ex.toString());
        }

    }

    private String checkNull(final JSONObject json, final String key) {
        return json.isNull(key) ? "" : json.optString(key);
    }

    public void showAlert(String message, final boolean newUserFlag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        View titleview = getActivity().getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
        title.setText(dk.eatmore.demo.R.string.alert);
        builder.setCustomTitle(titleview);

        TextView myMsg = new TextView(getActivity());
        myMsg.setText(message);
        myMsg.setGravity(Gravity.START);
        myMsg.setPadding(15, 15, 15, 15);
        myMsg.setTextColor(Color.BLACK);
        builder.setView(myMsg);


        //  builder.setMessage(message)
        builder.setCancelable(false)
                .setPositiveButton(getString(dk.eatmore.demo.R.string.dialog_positive), new DialogInterface.OnClickListener() {
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

}
