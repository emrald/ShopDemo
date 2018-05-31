package dk.eatmore.demo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.eatmore.demo.R;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.fragments.CartFragment;
import dk.eatmore.demo.fragments.MyCartCheckOut;
import dk.eatmore.demo.interfaces.GetCityListCallback;
import dk.eatmore.demo.interfaces.GetPostalCodesCallback;
import dk.eatmore.demo.myutils.AndroidMultiPartEntity;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.myutils.CircleTransform;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.PasswordView;
import dk.eatmore.demo.myutils.SharedPrefClass;
import dk.eatmore.demo.myutils.Utility;
import dk.eatmore.demo.network.NetworkUtils;
import dk.eatmore.demo.searchbardialog.CustomAlertAdapter;


/**
 * Created by pallavi.b on 04-May-16.
 */
public class FacebookSignUpActivity extends AppCompatActivity implements GetPostalCodesCallback, GetCityListCallback {

    private static final String TAG = FacebookSignUpActivity.class.getSimpleName();
    CustomAlertAdapter arrayAdapter;
    private ArrayList<String> array_sort;
    int textlength = 0;
    private android.app.AlertDialog myalertDialog = null;

    long totalSize = 0;
    SimpleArcDialog mDialog;
    private ImageView imgback;

    private Button btnSignUp;
    private Button postalCodeButton;
    private Button cityNameButton;
    private Button facebookbtnCancel;

    private EditText inputEmail;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputStreetName;
    private EditText floorDoorEditText;
    private EditText inputPhoneNo;
    private EditText facebookedttxtPassword;
    private EditText facebookedttxtConfirmPassword;

    private String strEmail;
    private String strFirstName;
    private String strLastName;
    private String strStreetName;
    private String strFloorDoor = "";
    private String strPostalCode;
    private String strPhoneNo;
    private String strCityName;
    private String strPassword;
    private String strConfirmPassowrd;

    ImageView inputprofileimage;
    String imagePath;
    String[] allPostalName_array;
    //    private String userIdIntent;
    private String userIdApi;
    boolean is_from_cart = false;
    private String fbIdString = "";

    private SharedPrefClass mSharedPrefClass = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(dk.eatmore.demo.R.layout.facebook_signup_view);
        setupUI(findViewById(dk.eatmore.demo.R.id.signupcoordinatorLayout));

        fbIdString = getIntent().getStringExtra("FBId");
        strFirstName = getIntent().getStringExtra("firstName");
        strLastName = getIntent().getStringExtra("lastName");
        strEmail = getIntent().getStringExtra("email");
//        userIdIntent = getIntent().getStringExtra("userId");
        imagePath = getIntent().getStringExtra("userFaceBookImage");
        ApplicationClass.getInstance().getPrefManager().
                setStringPreferences(MyPreferenceManager.FACEBOOK_IMAGE, imagePath);

        mSharedPrefClass = new SharedPrefClass(FacebookSignUpActivity.this);
        is_from_cart = getIntent().getBooleanExtra(MyCartCheckOut.IS_FROM_CART, false);
        initToolBar();
        initView();
        getAllPostal();
        setValues();
        initDialog();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getValidation())
                    signUpWithFacebook();
//                new SignUpCall().execute();
            }
        });

        facebookbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FacebookSignUpActivity.this
                );
                View titleview = LayoutInflater.from(FacebookSignUpActivity.this).inflate(dk.eatmore.demo.R.layout.delete_account_title, null);
                TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.deletedialogtitle);
                title.setText("Are you sure want to cancel?");
                alertDialog.setCustomTitle(titleview);
                //   .setMessage("Are you sure you want to delete this entry?")
                alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LoginManager.getInstance().logOut();
                        ApplicationClass.getInstance().getPrefManager().clearSharedPreference();
                        Intent i = new Intent(FacebookSignUpActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
//                        deleteUserAccount();

                    }
                })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })

                        .show();
            }
        });


        postalCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder myDialog = new android.app.AlertDialog.Builder(FacebookSignUpActivity.this);

                View titleview = getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
                TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
                title.setText("Select postal code");
                myDialog.setCustomTitle(titleview);

                final EditText editText = new EditText(FacebookSignUpActivity.this);
                editText.setTextColor(Color.BLACK);
                editText.setHint(dk.eatmore.demo.R.string.enter_postal_code);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                int maxLength = 4;
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

                final ListView listview = new ListView(FacebookSignUpActivity.this);

                editText.setCompoundDrawablesWithIntrinsicBounds(dk.eatmore.demo.R.drawable.ic_search_grey_500_24dp, 0, 0, 0);
                array_sort = new ArrayList<String>(Arrays.asList(allPostalName_array));

                LinearLayout layout = new LinearLayout(FacebookSignUpActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(editText);
                layout.addView(listview);
                myDialog.setView(layout);
                arrayAdapter = new CustomAlertAdapter(FacebookSignUpActivity.this, array_sort);
                listview.setAdapter(arrayAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
                        myalertDialog.dismiss();
                        postalCodeButton.setText(array_sort.get(position));
                        strPostalCode = array_sort.get(position);

                        // Log.e("onclick","onclick "+array_sort.get(position));

                        getCityList(strPostalCode);


                    }
                });
                editText.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {

                    }

                    public void beforeTextChanged(CharSequence s,
                                                  int start, int count, int after) {

                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        editText.setCompoundDrawablesWithIntrinsicBounds(dk.eatmore.demo.R.drawable.ic_search_grey_500_24dp, 0, 0, 0);
                        textlength = editText.getText().length();
                        array_sort.clear();
                        for (int i = 0; i < allPostalName_array.length; i++) {
                            if (textlength <= allPostalName_array[i].length()) {

                                if (allPostalName_array[i].toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                                    array_sort.add(allPostalName_array[i]);
                                }
                            }
                        }
                        listview.setAdapter(new CustomAlertAdapter(FacebookSignUpActivity.this, array_sort));
                    }
                });
                myDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                myalertDialog = myDialog.show();
            }
        });

//        getUserProfileData(userIdIntent);
    }

    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Utility.hideSoftKeyboard(FacebookSignUpActivity.this);
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

    private void signUpWithFacebook() {
        mDialog.show();
        final String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);

        String tag_string_req = "SignUpWithFacebook";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ApiCall.FACEBOOK_SIGN_UP,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        //    Log.e(TAG, "FB Login " + response);
                        showAlert(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialog.dismiss();
                        //   Log.e(TAG, "getAllCharity Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("r_token", ApiCall.R_TOKEN);
                params.put("r_key", ApiCall.R_KEY);
                params.put("fb_id", fbIdString);
                params.put("email", strEmail);
                params.put("password_hash", strPassword);
                params.put("first_name", strFirstName);
                params.put("last_name", strLastName);
                params.put("telephone_no", strPhoneNo);
                params.put("street", strStreetName);
                params.put("address", strFloorDoor);
                params.put("postal_code", strPostalCode);
                params.put("city", strCityName);
                params.put("language", deviceLocale);

                return params;
            }

        };
        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

//    private void deleteUserAccount() {
//
//        mDialog.show();
//        String tag_string_req = "deleteUserAccount";
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.DELETE_ACCOUNT, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                mDialog.dismiss();
//                Log.e("storePayment", "storePayment Response: " + response);
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//                    // Check for error node in json
//                    if (status) {
//                        ApplicationClass.getInstance().getPrefManager().clearSharedPreference();
//                        Intent i = new Intent(FacebookSignUpActivity.this, LoginActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
//                        finish();
//
//                    } else {
//                        showDeleteAccountDialog("Unable to cancel your request. Please try again.", false);
//                    }
//                } catch (JSONException e) {
//                    // hideDialog();
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("deleteUserAccount", "deleteUserAccount Error: " + error.getMessage());
//                mDialog.dismiss();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                params.put("id", ApplicationClass.getInstance().getPrefManager()
//                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
//
//                Log.e("efe", "facebook " + params);
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }

    private void showDeleteAccountDialog(String message, final boolean actionFlag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FacebookSignUpActivity.this);
        builder.setCancelable(false);
        View titleview = getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
        title.setText(dk.eatmore.demo.R.string.alert);
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

    private void initView() {
        inputprofileimage = (ImageView) findViewById(dk.eatmore.demo.R.id.facebookprofileimage);
        inputEmail = (EditText) findViewById(dk.eatmore.demo.R.id.facebookedttxtEmail);

        facebookedttxtPassword = (PasswordView) findViewById(dk.eatmore.demo.R.id.facebookedttxtPassword);
        facebookedttxtConfirmPassword = (PasswordView) findViewById(dk.eatmore.demo.R.id.facebookedttxtConfirmPassword);

        inputFirstName = (EditText) findViewById(dk.eatmore.demo.R.id.facebookedttxtFirstName);
        inputLastName = (EditText) findViewById(dk.eatmore.demo.R.id.facebookedttxtLastName);
        inputStreetName = (EditText) findViewById(dk.eatmore.demo.R.id.facebookedttxtStreetName);
        floorDoorEditText = (EditText) findViewById(dk.eatmore.demo.R.id.facebook_et_floor_door);
        inputStreetName.setSingleLine(true);
        inputStreetName.setLines(10);
        inputStreetName.setHorizontallyScrolling(false);
        postalCodeButton = (Button) findViewById(dk.eatmore.demo.R.id.facebookedttxtPostelCode);
        cityNameButton = (Button) findViewById(dk.eatmore.demo.R.id.facebookbtnCityName);
        inputPhoneNo = (EditText) findViewById(dk.eatmore.demo.R.id.facebookedttxtTelePhoneSignUp);
        btnSignUp = (Button) findViewById(dk.eatmore.demo.R.id.facebookbtnSignUp);
        facebookbtnCancel = (Button) findViewById(dk.eatmore.demo.R.id.facebookbtnCancel);
        if (!imagePath.isEmpty())
            Picasso.with(this)
                    .load(imagePath)
                    .transform(new CircleTransform()).placeholder(dk.eatmore.demo.R.drawable.user_avatar_mainpicture)
                    //.transform(new CircleTransformation())
                    .into(inputprofileimage);

        inputFirstName.setKeyListener(null);
        inputLastName.setKeyListener(null);
//        inputEmail.setKeyListener(null);
    }

    //getall getAllPostal
    private void getAllPostal() {
        String tag_string_req = "getAllCharity";

        NetworkUtils.callGetPostalCodes(tag_string_req, FacebookSignUpActivity.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.POSTAL_CODE_LIST, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                //    Log.e(TAG, "cHARITY Response: " + response);
//                ArrayList<String> stringArrayListID = new ArrayList<String>();
//                ArrayList<String> stringArrayListNAME = new ArrayList<String>();
//                JSONObject jObj;
//                try {
//                    jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//
//                    if (status) {
//                        JSONArray jsonArray = jObj.getJSONArray("postalcode_list");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                            String postCode = jsonObject.getString("postal_code");
//                            stringArrayListID.add(postCode);
//                            stringArrayListNAME.add(postCode);
//                        }
//
//                        allPostalName_array = stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
//                    } else {
//                        String errorMsg = jObj.getString("status");
////                        Toast.makeText(getApplicationContext(),
////                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //   Log.e(TAG, "getAllCharity Error: " + error.getMessage());
//                        Toast.makeText(getApplicationContext(),
//                                error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting params to register url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                return params;
//            }
//
//        };
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void getPostalCodesCallbackSuccess(String success) {
//        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
//            mSimpleArcDialog.dismiss();
        //    Log.e(TAG, "cHARITY Response: " + response);
        ArrayList<String> stringArrayListID = new ArrayList<String>();
        ArrayList<String> stringArrayListNAME = new ArrayList<String>();
        JSONObject jObj;
        try {
            jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");

            if (status) {
                JSONArray jsonArray = jObj.getJSONArray("postalcode_list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String postCode = jsonObject.getString("postal_code");
                    stringArrayListID.add(postCode);
                    stringArrayListNAME.add(postCode);
                }

                allPostalName_array = stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
            } else {
                String errorMsg = jObj.getString("status");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getPostalCodesCallbackError(VolleyError volleyError) {
        //   Log.e(TAG, "getAllCharity Error: " + error.getMessage());
//        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
//            mSimpleArcDialog.dismiss();
        Toast.makeText(getApplicationContext(),
                volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void setValues() {
        inputFirstName.setText(strFirstName);
        inputLastName.setText(strLastName);
        inputEmail.setText(strEmail);
    }

    private void initDialog() {
        mDialog = new SimpleArcDialog(this);

        mDialog.setCancelable(false);

        ArcConfiguration configuration = new ArcConfiguration(this);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(getString(dk.eatmore.demo.R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mDialog.setConfiguration(configuration);
    }

    private boolean getValidation() {
        strFirstName = inputFirstName.getText().toString().trim();
        strLastName = inputLastName.getText().toString().trim();
        strEmail = inputEmail.getText().toString().trim();
        strPhoneNo = inputPhoneNo.getText().toString().trim();
        strPassword = facebookedttxtPassword.getText().toString().trim();
        strConfirmPassowrd = facebookedttxtConfirmPassword.getText().toString().trim();
        strStreetName = inputStreetName.getText().toString().trim();
        strFloorDoor = floorDoorEditText.getText().toString().trim();

        if (strFirstName.isEmpty()) { // Check First Name
            inputFirstName.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
            inputFirstName.requestFocus();
            return false;
        } else if (strFirstName.length() <= 2 || strFirstName.length() > 100) {
            inputFirstName.setError(getString(dk.eatmore.demo.R.string.first_name_limit));
            inputFirstName.requestFocus();
            return false;
        } else if (!strFirstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
            inputFirstName.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
            inputFirstName.requestFocus();
            return false;
        } else if (strLastName.isEmpty()) { // Check Last Name
            inputLastName.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
            inputLastName.requestFocus();
            return false;
        } else if (strLastName.length() <= 2 || strLastName.length() > 100) {
            inputLastName.setError(getString(dk.eatmore.demo.R.string.last_name_limit));
            inputLastName.requestFocus();
            return false;
        } else if (!strLastName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
            inputLastName.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
            inputLastName.requestFocus();
            return false;
        } else if (strEmail.isEmpty()) { // Check Email
            inputEmail.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
            inputEmail.requestFocus();
            return false;
        } else if (!isValidEmail(strEmail)) {
            inputEmail.setError(getString(dk.eatmore.demo.R.string.invalid_email));
            inputEmail.requestFocus();
            return false;
        } else if (strPhoneNo.isEmpty()) { // Check Phone
            inputPhoneNo.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
            inputPhoneNo.requestFocus();
            return false;
        } else if (strPhoneNo.length() <= 7) {
            inputPhoneNo.setError(getString(dk.eatmore.demo.R.string.invalid_phone_number));
            inputPhoneNo.requestFocus();
            return false;
        } else if (strPassword.length() <= 5) { // Check Password
            facebookedttxtPassword.setError(getString(dk.eatmore.demo.R.string.password_minimum_length));
            facebookedttxtPassword.requestFocus();
            return false;
        } else if (!strPassword.equals(strConfirmPassowrd)) {
            facebookedttxtConfirmPassword.setError(getString(dk.eatmore.demo.R.string.confirm_password_not_match));
            facebookedttxtConfirmPassword.requestFocus();
            return false;
        } else if (strStreetName.isEmpty()) { // Check Street Name
            inputStreetName.setError(getString(dk.eatmore.demo.R.string.empty_street_number));
            inputStreetName.requestFocus();
            return false;
        } else if (strPostalCode == null) {
            postalCodeButton.setError(getString(dk.eatmore.demo.R.string.invalid_postal_code));
            postalCodeButton.requestFocus();
            return false;
        } else if (strCityName == null) {
            cityNameButton.setError(getString(dk.eatmore.demo.R.string.invalid_city_name));
            cityNameButton.requestFocus();
            return false;
        }

//        strFirstName = inputFirstName.getText().toString().trim();
//        if (!strFirstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
//            inputFirstName.setError("Empty first name");
//            //  inputFirstName.requestFocus();
//            return false;
//        }
//
//        if (!strFirstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
//            inputFirstName.setError("First name charaters limit should be come between 3-100");
//            //  inputFirstName.requestFocus();
//        }
//
//        if (strFirstName.length() <= 2) {
//            inputFirstName.setError("First name charaters limit should be come between 3-100");
//            // inputLastName.requestFocus();
//        } else
//            inputFirstName.setError(null);
//
//        strLastName = inputLastName.getText().toString().trim();
//        if (!strLastName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
//            inputLastName.setError("Empty last name");
//            //  inputFirstName.requestFocus();
//            return false;
//        }
//
//        if (!strLastName.matches("^[A-Za-z_ÆØÅæøå ]+$") && strLastName.length() >= 3) {
//            inputLastName.setError("Last name charaters limit should be come between 3-100");
//            //  inputLastName.requestFocus();
//        }
//
//        if (strLastName.length() <= 2) {
//            inputLastName.setError("Last name charaters limit should be come between 3-100");
//            //  inputLastName.requestFocus();
//        }
//
//        strEmail = inputEmail.getText().toString().trim();
//        if (!isValidEmail(strEmail)) {
//            inputEmail.setError("Invalid email");
//            //   inputEmail.requestFocus();
//        }
//
//        strPhoneNo = inputPhoneNo.getText().toString().trim();
//        if (strPhoneNo.isEmpty() && strPhoneNo.length() >= 8) {
//            inputPhoneNo.setError("Empty phone number");
//            // inputPhoneNo.requestFocus();
//        }
//
//        if (strPhoneNo.length() <= 7) {
//            inputPhoneNo.setError("Telephone No should contain at least 8 characters.");
//            // inputPhoneNo.requestFocus();
//        }
//
//        strPassword = facebookedttxtPassword.getText().toString().trim();
//        if (strPassword.length() <= 5) {
//            facebookedttxtPassword.setError("Password should contain at least 6 characters");
//            ///   inputPassword.requestFocus();
//        } else
//            facebookedttxtPassword.setError(null);
//
//        strConfirmPassowrd = facebookedttxtConfirmPassword.getText().toString().trim();
//        if (!strPassword.equals(strConfirmPassowrd)) {
//            // inputPassword.setError("Confirm password didn't match.");
//            facebookedttxtConfirmPassword.setError("Confirm password didn't match.");
//        }
//
//        strStreetName = inputStreetName.getText().toString().trim();
//        if (strStreetName.isEmpty()) {
//            inputStreetName.setError("Address is not valid.");
//            //  inputStreetName.requestFocus();
//        }
//
//        if (!(strPostalCode != null && strPostalCode.length() > 0)) {
//            postalCodeButton.setError("Empty postal code");
////            postalCodeButton.requestFocus();
//            return false;
//        }
//        if(!(strCityName != null && strCityName.length() > 0))
//        {
//            cityNameButton.setError("Empty city name");
//           // cityNameButton.requestFocus();
//        }

        //////
//        if (!(userIdIntent != null && userIdIntent.length() > 0)) {
//            Toast.makeText(FacebookSignUpActivity.this, R.string.try_again, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        strStreetName = inputStreetName.getText().toString().trim();
//        if (strStreetName.isEmpty()) {
//            inputStreetName.setError("Empty street name");
//            // inputStreetName.requestFocus();
//
//            return false;
//        }
//        if (!(strPostalCode != null && strPostalCode.length() > 0)) {
//            postalCodeButton.setError("Empty postal code");
////            postalCodeButton.requestFocus();
//            return false;
//        }
//

//        if(!(strCityName != null && strCityName.length() > 0))
//        {
//            cityNameButton.setError("Empty city name");
//            cityNameButton.requestFocus();
//            return false;
//        }
//        strPhoneNo = inputPhoneNo.getText().toString().trim();
//        if (strPhoneNo.isEmpty() && strPhoneNo.length() >= 8) {
//            inputPhoneNo.setError("Empty phone number");
//            //  inputPhoneNo.requestFocus();
//            return false;
//        }
//
//        if (strPhoneNo.length() <= 7) {
//            inputPhoneNo.setError("Telephone No should contain at least 8 characters.");
//            //  inputPhoneNo.requestFocus();
//            return false;
//        }

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

//    private void getUserProfileData(final String userId) {
//        String tag_string_req = "suggested_user";
//
//        mDialog.show();
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.GET_USER_PROFILE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //   Log.e("GET_INDIVIDUAL_MEMBER", "GET_INDIVIDUAL_MEMBER Response: " + response.toString());
//                mDialog.dismiss();
//
//                if (response != null) {
//                    JSONObject jsonresponse = null;
//                    try {
//                        jsonresponse = new JSONObject(response.toString());
//                        parseJsonFeed(jsonresponse);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Log.e("param", "Login Error: " + error.getMessage());
//                if (mDialog != null && mDialog.isShowing())
//                    mDialog.dismiss();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                params.put("id", userId);
//                return params;
//            }
//
//        };
//
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }

    private void parseJsonFeed(JSONObject jsonresponse) {
        try {
            Log.e("edit p", "edit  " + jsonresponse);
            JSONObject jObj = jsonresponse.getJSONObject("data");
            boolean status = jsonresponse.getBoolean("status");
            if (status) {
                userIdApi = jObj.getString("id");
                inputFirstName.setText(jObj.getString("first_name"));
                inputLastName.setText(jObj.getString("last_name"));
                inputEmail.setText(jObj.getString("email"));

                inputPhoneNo.setText(checkNull(jObj, "telephone_no"));
                inputStreetName.setText(checkNull(jObj, "street"));
                strCityName = checkNull(jObj, "city");
                //   Log.e("dd",""+jsonresponse.getString("image_path")+checkNull(jObj, "image"));
                //    ArrayList<String> stringArrayListID = new ArrayList<String>();
                ArrayList<String> stringArrayListNAME = new ArrayList<String>();
                JSONArray jsonArray = jsonresponse.getJSONArray("postal_code_details");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String postCode = jsonObject.getString("postal_code");
                    stringArrayListNAME.add(postCode);

                }

                allPostalName_array = stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
            }
        } catch (Exception ex) {
            Log.e("dd", "" + ex.toString());
        }

    }

    private String checkNull(final JSONObject json, final String key) {
        return json.isNull(key) ? "" : json.optString(key);
    }

    private void getCityList(final String postalId) {
        mDialog.show();
        String tag_string_req = "getCityList";

        NetworkUtils.callGetCityList(postalId, tag_string_req, FacebookSignUpActivity.this);
    }

    @Override
    public void getCityListCallbackSuccess(String success) {
        //   Log.e(TAG, "cHARITY Response: " + response);
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        // ArrayList<String> stringArrayListNAME = new ArrayList<String>();
        JSONObject jObj;
        try {
            jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            if (status) {
                strCityName = jObj.getString("city_name");
                cityNameButton.setText(strCityName);
            } else {
                // Error occurred in registration. Get the error
                // message
                String errorMsg = jObj.getString("status");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getCityListCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        Toast.makeText(getApplicationContext(),
                volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void initToolBar() {
        TextView title = (TextView) findViewById(dk.eatmore.demo.R.id.tootlbar_title);
        title.setText(getResources().getString(dk.eatmore.demo.R.string.registration));

        imgback = (ImageView) findViewById(dk.eatmore.demo.R.id.imgback);
        imgback.setVisibility(View.GONE);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class SignUpCall extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            mDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(ApiCall.EDIT_PROFILE);
            HttpPost httppost = new HttpPost(ApiCall.FACEBOOK_SIGN_UP);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                //publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                try {
                    InputStream is = new URL(imagePath).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                    byte[] data = bos.toByteArray();

                    entity.addPart("image", new ByteArrayBody(data,
                            "image/jpeg", "" + System.currentTimeMillis() + ".jpg"));
                    bitmap.recycle();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

//                entity.addPart("id",
//                        new StringBody(userIdApi));
                entity.addPart("fb_id",
                        new StringBody(fbIdString));

                entity.addPart("r_token",
                        new StringBody(ApiCall.R_TOKEN));
                entity.addPart("r_key",
                        new StringBody(ApiCall.R_KEY));
                entity.addPart("email",
                        new StringBody(strEmail, Charset.forName("UTF-8")));

                entity.addPart("first_name",
                        new StringBody(strFirstName, Charset.forName("UTF-8")));
                entity.addPart("last_name",
                        new StringBody(strLastName, Charset.forName("UTF-8")));
                entity.addPart("telephone_no",
                        new StringBody(strPhoneNo, Charset.forName("UTF-8")));
                entity.addPart("street",
                        new StringBody(strStreetName, Charset.forName("UTF-8")));
                entity.addPart("address",
                        new StringBody(strFloorDoor, Charset.forName("UTF-8")));
                entity.addPart("postal_code",
                        new StringBody(strPostalCode, Charset.forName("UTF-8")));
                entity.addPart("city",
                        new StringBody(strCityName, Charset.forName("UTF-8")));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                //    Log.e("Tag",""+strPostalCode);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (IOException e) {
                mDialog.dismiss();
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            mDialog.dismiss();
            Log.e("Sign Up", "Response from server: " + result);
            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        String msg = null, dalert = null;
        boolean status = false;
        try {
            JSONObject jObj = new JSONObject(message);
            status = jObj.getBoolean("status");

            if (status) {
                dalert = getString(R.string.congrats);

                mSharedPrefClass.setFacebookLogin(true);
                ApplicationClass.getInstance().getPrefManager().
                        setBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN, true);
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.KEY_USER_ID, jObj.getJSONObject("data").getString("id"));
                msg = getResources().getString(dk.eatmore.demo.R.string.sign_up_done_msg);
            } else {
                dalert = getString(dk.eatmore.demo.R.string.alert);
                String errorKey = jObj.getString("errorkey");
                msg = jObj.getJSONObject("errors").getString(errorKey);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View titleview = getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
        title.setText(dalert);
        builder.setCustomTitle(titleview);
        final boolean finalStatus = status;
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(getString(dk.eatmore.demo.R.string.dialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (finalStatus) {
                            if (is_from_cart) {
                                CartFragment fragment = new CartFragment();
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.add(dk.eatmore.demo.R.id.fragment_container, fragment);
                                fragmentTransaction.addToBackStack(FacebookSignUpActivity.class.getName());
                                fragmentTransaction.commit();
                            } else {
                                Intent intent = new Intent(FacebookSignUpActivity.this,
                                        MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        } else
                            dialog.dismiss();


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FacebookSignUpActivity.this
        );
        View titleview = LayoutInflater.from(FacebookSignUpActivity.this).inflate(dk.eatmore.demo.R.layout.delete_account_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.deletedialogtitle);
        title.setText("Are you sure want to cancel ?");
        alertDialog.setCustomTitle(titleview);
        //   .setMessage("Are you sure you want to delete this entry?")
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                LoginManager.getInstance().logOut();
                ApplicationClass.getInstance().getPrefManager().clearSharedPreference();
                Intent i = new Intent(FacebookSignUpActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
//                        deleteUserAccount();

            }
        })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })

                .show();
    }
}
