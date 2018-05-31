package dk.eatmore.demo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import dk.eatmore.demo.interfaces.ContactUsCallback;
import dk.eatmore.demo.interfaces.ContactUsSettingsCallback;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.Utility;
import dk.eatmore.demo.network.NetworkUtils;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ContactUsFragment extends Fragment implements ContactUsCallback, ContactUsSettingsCallback {

    private static final String TAG = ContactUsFragment.class.getSimpleName();
    private View view;
    private ImageView imgback;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText inputEmail;
    private EditText inputAddress;
    private EditText inputMsg;
    private EditText inputCity;
    private EditText input_phoneNO;
    private EditText inputPostelCode;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;
    private LinearLayout contactusll;
    private String firstName, lastName, email, address, message, postalCode, phonoNO, sendMailCopy = "0", userId;
    private CheckBox mCheckbox;
    private Button mButtonLogin;
    private SimpleArcDialog mDialog;
    private String[] allPostalName_array;
    //    private LinearLayout fnameLnameLayout;
    private ImageView restorentImage;
    private TextView textViewrestroName, aboutDescription, textViewphoneNO, textViewaddress;
    private boolean firstNameVisible = false;
    private boolean lastNameVisible = false;
    private boolean addressVisible = false;
    private boolean telephoneVisible = false;
    private boolean postalCodeVisible = false;
    private boolean cityeVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contact_us_new, container, false);
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();
        setupUI(view);
        userId = ApplicationClass.getInstance().getPrefManager()
                .getStringPreferences(MyPreferenceManager.KEY_USER_ID);

        initToolbar();
        initView();
        initGesture();
        initDialog();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getValidation())
                    contactUs();
            }


        });

        mCheckbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked)
                            sendMailCopy = "1";
                        else
                            sendMailCopy = "0";
                    }
                }
        );

        setPrefValue();
        getContactUsSetting();

        return view;
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

    private void setPrefValue() {
        String name = ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.RESTAURANT_NAME);
        String no = ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.restaurant_num);
        String add = ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.restaurant_ADDRESS);
        String image = ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE);

        if (name != null)
            textViewrestroName.setText(name);

        if (add != null)
            textViewaddress.setText(add);

        if (no != null)
            textViewphoneNO.setText(no);

//        restorentImage.setVisibility(View.GONE);
//        if (image != null) {
//            restorentImage.setVisibility(View.VISIBLE);
//            textViewrestroName.setVisibility(View.GONE);
//            Picasso.with(getActivity()).load(image)
//                    .into(restorentImage);
//        } else {
        restorentImage.setVisibility(View.GONE);
        textViewrestroName.setVisibility(View.VISIBLE);
//        }

    }

    private boolean getValidation() {
        firstName = firstNameEditText.getText().toString().trim();
        lastName = lastNameEditText.getText().toString().trim();
        message = inputMsg.getText().toString().trim();
        address = inputAddress.getText().toString().trim();
        phonoNO = input_phoneNO.getText().toString().trim();
        postalCode = inputPostelCode.getText().toString().trim();
        email = inputEmail.getText().toString().trim();

        if (firstNameVisible && firstName.isEmpty()) {
            firstNameEditText.setError(getString(R.string.cannot_be_blank_field));
            //  firstNameEditText.requestFocus();
            return false;
        }

        if (lastNameVisible && lastName.isEmpty()) {
            lastNameEditText.setError(getString(R.string.cannot_be_blank_field));
            return false;
        }

        if (email.isEmpty()) {
            inputEmail.setError(getString(R.string.cannot_be_blank_field));
            // inputEmail.requestFocus();
            return false;
        }

        if (!isValidEmail(email)) {
            inputEmail.setError(getString(R.string.invalid_email));
            //   inputEmail.requestFocus();
            return false;
        }

        if (message.isEmpty()) {
            inputMsg.setError(getString(R.string.cannot_be_blank_field));
            // inputMsg.requestFocus();
            return false;
        }

        if (addressVisible && address.isEmpty()) {
            address = "";
        }

        if (telephoneVisible && phonoNO.isEmpty()) {
            phonoNO = "";
        }

        if (postalCodeVisible && postalCode.isEmpty()) {
            postalCode = "";
        }

//        if (email.isEmpty()) {
//            inputEmail.setError("Please enter email id");
//            //  inputEmail.requestFocus();
//            return false;
//        } else
//            inputEmail.setError(null);
//
//        if (!isValidEmail(email)) {
//            inputEmail.setError("Please enter valid email id");
//            // inputEmail.requestFocus();
//            return false;
//        }
//
//        if (message.isEmpty()) {
//            inputMsg.setError("Please enter message");
//            // inputMsg.requestFocus();
//            return false;
//        } else
//            inputMsg.setError(null);

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

    private void initView() {
        restorentImage = (ImageView) view.findViewById(R.id.contactrestroImage);
        textViewrestroName = (TextView) view.findViewById(R.id.contactrestroName);
        textViewaddress = (TextView) view.findViewById(R.id.conatctabtaddress);
        textViewphoneNO = (TextView) view.findViewById(R.id.conatctphoneNO);

        contactusll = (LinearLayout) view.findViewById(R.id.contactusll);
        mCheckbox = (CheckBox) view.findViewById(R.id.contact_checkbox);
        mButtonLogin = (Button) view.findViewById(R.id.cntact_btnSubmit);

//        fnameLnameLayout = (LinearLayout) view.findViewById(R.id.fnameLnameLayout);
        firstNameEditText = (EditText) view.findViewById(R.id.contact_edttxtFirstName);
        lastNameEditText = (EditText) view.findViewById(R.id.contact_edttxtLastName);
        inputEmail = (EditText) view.findViewById(R.id.contact_email);
        inputAddress = (EditText) view.findViewById(R.id.contact_address);
        inputMsg = (EditText) view.findViewById(R.id.contact_msg);

        inputMsg.setSingleLine(true);
        inputMsg.setLines(10);
        inputMsg.setHorizontallyScrolling(false);

        inputPostelCode = (EditText) view.findViewById(R.id.contact_inputtxtPostelCode);
        //  btnCity = (Button) view.findViewById(R.id.contact_cityname);
        inputCity = (EditText) view.findViewById(R.id.contact_cityname);
        input_phoneNO = (EditText) view.findViewById(R.id.contact_phone);
        //inputKummonName=(EditText) view.findViewById(R.id.contact_inputtxtKummonName);
    }

    private void initDialog() {
        mDialog = new SimpleArcDialog(getActivity());
        mDialog.setCancelable(false);

        ArcConfiguration configuration = new ArcConfiguration(getActivity());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(getString(R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mDialog.setConfiguration(configuration);
    }

    private void initToolbar() {
        TextView title = (TextView) view.findViewById(R.id.tootlbar_title);
        title.setText(getResources().getString(R.string.contact_us));

        imgback = (ImageView) view.findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initGesture() {

        gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        view.setOnTouchListener(gestureListener);
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
//                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                  //  Toast.makeText(getActivity(), "Left Swipe", Toast.LENGTH_SHORT).show();
//                }  else
                if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //  Toast.makeText(getActivity(), "Right Swipe", Toast.LENGTH_SHORT).show();
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

    private void contactUs() {
        mDialog.show();
        String tag_string_req = "deleteAccount";
        Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email);
//                params.put("password", password);
        params.put("r_token", ApiCall.R_TOKEN);
        params.put("r_key", ApiCall.R_KEY);
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("email", email);
        params.put("address", address);
        params.put("telephone", phonoNO);
        params.put("postal_code", postalCode);
        params.put("message", message);
        params.put("send_copy", sendMailCopy);

        NetworkUtils.callContactUs(params, tag_string_req, ContactUsFragment.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.CONTACT_US, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "deleteAccount Response: " + response);
//                mDialog.dismiss();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//                    // Check for error node in json
//                    if (status) {
//                        showAlert("Your message was sent", status);
//                    } else {
//                        showAlert(getString(R.string.try_again), status);
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mDialog.dismiss();
//
//                Log.e(TAG, "delete prod Error: " + error.getMessage());
//                Toast.makeText(getActivity(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                //	hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
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
//
//                Log.e("params", "params : " + params);
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void contactUsCallbackSuccess(String success) {
        Log.d(TAG, "deleteAccount Response: " + success);
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
                showAlert("Your message was sent", status);
            } else {
                showAlert(getString(R.string.try_again), status);
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void contactUsCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        Log.e(TAG, "delete prod Error: " + volleyError.getMessage());
//        Toast.makeText(getActivity(),
//                volleyError.getMessage(), Toast.LENGTH_LONG).show();
        //	hideDialog();
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableContactUs();
        }
    }

    private void showDialogInternetUnavailableContactUs() {
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
                    contactUs();
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

    private void showAlert(String msg, final boolean flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        View titleview = getActivity().getLayoutInflater().inflate(R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(R.id.dialogtitle);
        title.setText(R.string.thank_you);
        builder.setCustomTitle(titleview);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (flag) {
                            getActivity().getSupportFragmentManager().popBackStack();

                        } else
                            dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // getContactUsSetting
    private void getContactUsSetting() {
        String tag_string_req = "getContactUsSetting";
        mDialog.show();

        NetworkUtils.callContactUsSettings(userId, tag_string_req, ContactUsFragment.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.GET_CONTACT_SETTING,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("Contact us", "getContactUsSetting Response: " + response);
//                        mDialog.dismiss();
//
//                        if (response != null) {
//                            JSONObject jsonresponse = null;
//                            try {
//                                jsonresponse = new JSONObject(response);
//                                parseJsonFeed(jsonresponse);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        mDialog.dismiss();
//                        //   Log.e(TAG, "getAllCharity Error: " + error.getMessage());
//                        Toast.makeText(getActivity(),
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
//
//                if (userId != null)
//                    params.put("user_id", userId);
//                else
//                    params.put("user_id", "");
//
//                return params;
//            }
//
//        };
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void contactUsSettingsCallbackSuccess(String success) {
        Log.e("Contact us", "getContactUsSetting Response: " + success);
        mDialog.dismiss();

        if (success != null) {
            JSONObject jsonresponse = null;
            try {
                jsonresponse = new JSONObject(success);
                parseJsonFeed(jsonresponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void contactUsSettingsCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        //   Log.e(TAG, "getAllCharity Error: " + error.getMessage());
//        Toast.makeText(getActivity(),
//                volleyError.getMessage(), Toast.LENGTH_LONG).show();
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableContactUsSettings();
        }
    }

    private void showDialogInternetUnavailableContactUsSettings() {
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
                getContactUsSetting();
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

    private void parseJsonFeed(JSONObject jsonresponse) {
        try {
            JSONObject jObj = jsonresponse.getJSONObject("data");
            boolean status = jsonresponse.getBoolean("status");
            if (status) {
                if (jObj.getString("first_name").equals("1")) {
                    firstNameVisible = true;
                    firstNameEditText.setVisibility(View.VISIBLE);
                } else
                    firstNameEditText.setVisibility(View.GONE);

                if (jObj.getString("last_name").equals("1")) {
                    lastNameVisible = true;
                    lastNameEditText.setVisibility(View.VISIBLE);
                } else
                    lastNameEditText.setVisibility(View.GONE);

                if (jObj.getString("address").equals("1")) {
                    addressVisible = true;
                    inputAddress.setVisibility(View.VISIBLE);
                } else
                    inputAddress.setVisibility(View.GONE);

                if (jObj.getString("telephone").equals("1")) {
                    telephoneVisible = true;
                    input_phoneNO.setVisibility(View.VISIBLE);
                } else
                    input_phoneNO.setVisibility(View.GONE);

                if (jObj.getString("postal_code").equals("1")) {
                    postalCodeVisible = true;
                    inputPostelCode.setVisibility(View.VISIBLE);
                } else
                    inputPostelCode.setVisibility(View.GONE);

                if (jObj.getString("city").equals("1")) {
                    cityeVisible = true;
                    inputCity.setVisibility(View.VISIBLE);
                } else
                    inputCity.setVisibility(View.GONE);

                if (!jsonresponse.isNull("user_info")) {
                    JSONObject userData = jsonresponse.getJSONObject("user_info");
                    firstNameEditText.setText(checkNull(userData, "first_name"));

                    lastNameEditText.setText(checkNull(userData, "last_name"));
                    input_phoneNO.setText(checkNull(userData, "telephone_no"));
                    inputAddress.setText(checkNull(userData, "address"));
                    inputPostelCode.setText(checkNull(userData, "postal_code"));
                    inputCity.setText(checkNull(userData, "city"));
                    inputEmail.setText(checkNull(userData, "email"));

                }
                ArrayList<String> stringArrayListNAME = new ArrayList<String>();
                JSONArray jsonArray = jsonresponse.getJSONArray("postal_code_details.");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String postCode = jsonObject.getString("postal_code");
                    stringArrayListNAME.add(postCode);
                }

                allPostalName_array = stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "" + ex.toString());
        }
    }

    private String checkNull(final JSONObject json, final String key) {
        return json.isNull(key) ? "" : json.optString(key);
    }
}