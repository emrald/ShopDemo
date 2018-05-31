package dk.eatmore.demo.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.interfaces.GetCityListCallback;
import dk.eatmore.demo.interfaces.GetPostalCodesCallback;
import dk.eatmore.demo.myutils.AndroidMultiPartEntity;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.myutils.CircleTransform;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.PasswordView;
import dk.eatmore.demo.myutils.Utility;
import dk.eatmore.demo.network.NetworkUtils;
import dk.eatmore.demo.searchbardialog.CustomAlertAdapter;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.soundcloud.android.crop.Crop;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by pallavi.b on 04-May-16.
 */
public class NewUserActivity extends AppCompatActivity implements GetPostalCodesCallback, GetCityListCallback {

    private static final String TAG = NewUserActivity.class.getSimpleName();
    private ImageView imgback;
    private TextView titleTextView;

    private ImageView profilePicImageView;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText streetNameEditText;
    private EditText floorDoorEditText;
    private EditText phoneNoEditText;

    private Button signUpButton;
    private Button postalCodeButton;
    private Button cityNameButton;

    private SimpleArcDialog mSimpleArcDialog;

    private android.app.AlertDialog myalertDialog = null;
    private ArrayList<String> array_sort;
    int textlength = 0;
    CustomAlertAdapter arrayAdapter;
    public static final int REQUEST_CAMERA = 0x1;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 0x4;
    long totalSize = 0;

    private String strEmail;
    private String strPassword;
    private String strConfirmPassowrd;
    private String strFirstName;
    private String strLastName;
    private String strStreetName;
    private String strFloorDoor = "";
    private String strPostalCode;
    private String strCityName;
    private String strPhoneNo;

    private Uri fileUri, destination;
    private String imagePath;
    android.app.AlertDialog dialog;
    String[] allPostalName_array, kummonNameArray, kummonNoArray, cityListArray;
    int postelSelectFlag = 0, kummonSelectFlag = 0, citySelectFlag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(dk.eatmore.demo.R.layout.sign_up_view);
        setupUI(findViewById(dk.eatmore.demo.R.id.signupcoordinatorLayout));
        initToolBar();
        initView();
        initDialog();
        captureImageInitialization();
        getAllPostel();
        setOnclickListeners();
    }

    // getAllPostal
    private void getAllPostel() {
        String tag_string_req = "getAllCharity";
        mSimpleArcDialog.show();

        NetworkUtils.callGetPostalCodes(tag_string_req, NewUserActivity.this);
    }

    @Override
    public void getPostalCodesCallbackSuccess(String success) {
        //    Log.e(TAG, "cHARITY Response: " + response);
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();

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
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        //   Log.e(TAG, "getAllCharity Error: " + error.getMessage());
        Toast.makeText(getApplicationContext(),
                volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void getCityList(final String postalId) {
        mSimpleArcDialog.show();
        String tag_string_req = "getCityList";

        NetworkUtils.callGetCityList(postalId, tag_string_req, NewUserActivity.this);
    }

    @Override
    public void getCityListCallbackSuccess(String success) {
        //   Log.e(TAG, "cHARITY Response: " + response);
        mSimpleArcDialog.dismiss();

        ArrayList<String> stringArrayListNAME = new ArrayList<String>();
        JSONObject jObj;
        try {
            jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            if (status) {
                cityNameButton.setText(jObj.getString("city_name"));
                strCityName = jObj.getString("city_name");
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
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Toast.makeText(getApplicationContext(),
                volleyError.getMessage(), Toast.LENGTH_LONG).show();

    }

    private void setOnclickListeners() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getValidation())
                    new SignUpCall().execute();
            }
        });

        profilePicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkAndRequestPermissions())
                        dialog.show();
                } else
                    dialog.show();

            }
        });

        postalCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder myDialog = new android.app.AlertDialog.Builder(NewUserActivity.this);

                View titleview = getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
                TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
                title.setText("Enter postal code");
                myDialog.setCustomTitle(titleview);

                final EditText editText = new EditText(NewUserActivity.this);
                editText.setTextColor(Color.BLACK);
                editText.setHint("Enter postal code");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                int maxLength = 4;
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                //   editText.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                final ListView listview = new ListView(NewUserActivity.this);

                editText.setCompoundDrawablesWithIntrinsicBounds(dk.eatmore.demo.R.drawable.ic_search_grey_500_24dp, 0, 0, 0);
                array_sort = new ArrayList<String>(Arrays.asList(allPostalName_array));

                LinearLayout layout = new LinearLayout(NewUserActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(editText);
                layout.addView(listview);
                myDialog.setView(layout);
                arrayAdapter = new CustomAlertAdapter(NewUserActivity.this, array_sort);
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
                        listview.setAdapter(new CustomAlertAdapter(NewUserActivity.this, array_sort));
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
    }

    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Utility.hideSoftKeyboard(NewUserActivity.this);
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

    private void initToolBar() {
        titleTextView = (TextView) findViewById(dk.eatmore.demo.R.id.tootlbar_title);
        titleTextView.setText(getResources().getString(dk.eatmore.demo.R.string.create_new_user));

        imgback = (ImageView) findViewById(dk.eatmore.demo.R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        profilePicImageView = (ImageView) findViewById(dk.eatmore.demo.R.id.inputprofileimage);
        emailEditText = (EditText) findViewById(dk.eatmore.demo.R.id.edttxtEmail);
        passwordEditText = (PasswordView) findViewById(dk.eatmore.demo.R.id.edttxtPassword);
        confirmPasswordEditText = (PasswordView) findViewById(dk.eatmore.demo.R.id.edttxtConfirmPassword);
        firstNameEditText = (EditText) findViewById(dk.eatmore.demo.R.id.edttxtFirstName);
        lastNameEditText = (EditText) findViewById(dk.eatmore.demo.R.id.edttxtLastName);
        streetNameEditText = (EditText) findViewById(dk.eatmore.demo.R.id.edttxtStreetName);
        floorDoorEditText = (EditText) findViewById(dk.eatmore.demo.R.id.et_floor_door);

//        streetNameEditText.setSingleLine(true);
//        streetNameEditText.setLines(10);
//        streetNameEditText.setHorizontallyScrolling(false);

        postalCodeButton = (Button) findViewById(dk.eatmore.demo.R.id.edttxtPostelCode);
        cityNameButton = (Button) findViewById(dk.eatmore.demo.R.id.btnCityName);
        phoneNoEditText = (EditText) findViewById(dk.eatmore.demo.R.id.edttxtTelePhoneSignUp);
        signUpButton = (Button) findViewById(dk.eatmore.demo.R.id.btnSignUp);
    }

    private void initDialog() {
        mSimpleArcDialog = new SimpleArcDialog(this);
        mSimpleArcDialog.setCancelable(false);

        ArcConfiguration configuration = new ArcConfiguration(this);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(getString(dk.eatmore.demo.R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mSimpleArcDialog.setConfiguration(configuration);
    }

    private boolean getValidation() {
        strFirstName = firstNameEditText.getText().toString().trim();
        strLastName = lastNameEditText.getText().toString().trim();
        strEmail = emailEditText.getText().toString().trim();
        strPhoneNo = phoneNoEditText.getText().toString().trim();
        strPassword = passwordEditText.getText().toString().trim();
        strConfirmPassowrd = confirmPasswordEditText.getText().toString().trim();
        strStreetName = streetNameEditText.getText().toString().trim();
        strFloorDoor = floorDoorEditText.getText().toString().trim();

        if (strFirstName.isEmpty()) { // Check First Name
            firstNameEditText.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
            firstNameEditText.requestFocus();
            return false;
        } else if (strFirstName.length() <= 2 || strFirstName.length() > 100) {
            firstNameEditText.setError(getString(dk.eatmore.demo.R.string.first_name_limit));
            firstNameEditText.requestFocus();
            return false;
        } else if (!strFirstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
            firstNameEditText.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
            firstNameEditText.requestFocus();
            return false;
        } else if (strLastName.isEmpty()) { // Check Last Name
            lastNameEditText.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
            lastNameEditText.requestFocus();
            return false;
        } else if (strLastName.length() <= 2 || strLastName.length() > 100) {
            lastNameEditText.setError(getString(dk.eatmore.demo.R.string.last_name_limit));
            lastNameEditText.requestFocus();
            return false;
        } else if (!strLastName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
            lastNameEditText.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
            lastNameEditText.requestFocus();
            return false;
        } else if (strEmail.isEmpty()) { // Check Email
            emailEditText.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
            emailEditText.requestFocus();
            return false;
        } else if (!isValidEmail(strEmail)) {
            emailEditText.setError(getString(dk.eatmore.demo.R.string.invalid_email));
            emailEditText.requestFocus();
            return false;
        } else if (strPhoneNo.isEmpty()) { // Check Phone
            phoneNoEditText.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
            phoneNoEditText.requestFocus();
            return false;
        } else if (strPhoneNo.length() <= 7) {
            phoneNoEditText.setError(getString(dk.eatmore.demo.R.string.invalid_phone_number));
            phoneNoEditText.requestFocus();
            return false;
        } else if (strPassword.length() <= 5) { // Check Password
            passwordEditText.setError(getString(dk.eatmore.demo.R.string.password_minimum_length));
            passwordEditText.requestFocus();
            return false;
        } else if (!strPassword.equals(strConfirmPassowrd)) {
            confirmPasswordEditText.setError(getString(dk.eatmore.demo.R.string.confirm_password_not_match));
            confirmPasswordEditText.requestFocus();
            return false;
        } else if (strStreetName.isEmpty()) { // Check Street Name
            streetNameEditText.setError(getString(dk.eatmore.demo.R.string.empty_street_number));
            streetNameEditText.requestFocus();
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

//        if (!strFirstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
//            firstNameEditText.setError(getString(R.string.first_name_limit));
//            //  firstNameEditText.requestFocus();
//        } else
//            firstNameEditText.setError(null);
//
//        if (strFirstName.length() <= 2 || strFirstName.length() > 100) {
//            firstNameEditText.setError(getString(R.string.first_name_limit));
//            // lastNameEditText.requestFocus();
//        } else
//            firstNameEditText.setError(null);
//
//        if (!isValidEmail(strEmail)) {
//            emailEditText.setError(getString(R.string.invalid_email));
//            //   emailEditText.requestFocus();
//        } else
//            emailEditText.setError(null);
//
//        if (strPassword.length() <= 5) {
//            passwordEditText.setError("Password should contain at least 6 characters");
//            ///   passwordEditText.requestFocus();
//        } else
//            passwordEditText.setError(null);
//
//        if(strConfirmPassowrd.length()<=5)
//        {
//            confirmPasswordEditText.setError("Empty confirm  passoword");
//            ///   passwordEditText.requestFocus();
//        }
//        else
//            confirmPasswordEditText.setError(null);
//
//        if (!strPassword.equals(strConfirmPassowrd)) {
//            // passwordEditText.setError("Confirm password didn't match.");
//            confirmPasswordEditText.setError("Confirm password didn't match.");
//        }
//
//        if (!strLastName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
//            lastNameEditText.setError(getString(R.string.last_name_limit));
//            // lastNameEditText.requestFocus();
//        } else
//            lastNameEditText.setError(null);
//
//        if (strLastName.length() <= 2) {
//            lastNameEditText.setError(getString(R.string.last_name_limit));
//            // lastNameEditText.requestFocus();
//        } else
//            lastNameEditText.setError(null);
//
//        if (strStreetName.isEmpty()) {
//            streetNameEditText.setError("Street and number is not valid.");
//            // streetNameEditText.requestFocus();
//        } else
//            streetNameEditText.setError(null);
        // strPostalCode=postalCodeButton.getText().toString().trim();
//        if (strPostalCode == null) {
//            postalCodeButton.setError("Postal code name is not valid.");
//            // postalCodeButton.requestFocus();
//
//        } else
//            postalCodeButton.setError(null);
//
//        if(strCityName==null)
//        {
//            cityNameButton.setError("Empty city name");
//            cityNameButton.requestFocus();
//
//        }
//        else
//            cityNameButton.setError(null);
////
//        if (strPhoneNo.isEmpty()) {
//            phoneNoEditText.setError("Phone number is not valid.");
//            //  phoneNoEditText.requestFocus();
//        } else
//            phoneNoEditText.setError(null);
//
//        if (strPhoneNo.length() <= 7) {
//            phoneNoEditText.setError("Phone number is not valid.");
//             phoneNoEditText.requestFocus();
//        } else
//            phoneNoEditText.setError(null);
//
//        if(imagePath==null)
//        {
//           Toast.makeText(NewUserActivity.this,R.string.image_error,Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (!isValidEmail(strEmail)) {
//            emailEditText.setError(getString(R.string.invalid_email));
//            // emailEditText.requestFocus();
//            return false;
//        }
//
//        if (strPassword.length() <= 5) {
//            passwordEditText.setError("Password should contain at least 6 characters");
//            //  passwordEditText.requestFocus();
//            return false;
//        }
//
//        if (!strPassword.equals(strConfirmPassowrd)) {
//            confirmPasswordEditText.setError("Confirm password didn't match.");
//            return false;
//        }
//
//        if (!strFirstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
//            firstNameEditText.setError("Only alpha numeric characters are allowed");
//            //    firstNameEditText.requestFocus();
//            return false;
//        }
//
//        if (!strLastName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
//            lastNameEditText.setError("Only alpha numeric characters are allowed");
//            //    lastNameEditText.requestFocus();
//            return false;
//        }
//
//        if (strLastName.length() <= 2) {
//            lastNameEditText.setError(getString(R.string.last_name_limit));
//            //    lastNameEditText.requestFocus();
//            return false;
//        }
//
//        if (strStreetName.isEmpty()) {
//            streetNameEditText.setError("Street and number is not valid.");
//            // streetNameEditText.requestFocus();
//            return false;
//        }
//
//        // strPostalCode=postalCodeButton.getText().toString().trim();
//        if (strPostalCode == null) {
//            postalCodeButton.setError("Postal code name is not valid.");
//            //  postalCodeButton.requestFocus();
//            return false;
//        }
//
//        if (strCityName == null) {
//            cityNameButton.setError("City name is not valid.");
//            //  cityNameButton.requestFocus();
//            return false;
//        } else
//            cityNameButton.setError(null);
//
//        if (strPhoneNo.isEmpty()) {
//            phoneNoEditText.setError("Phone number is not valid.");
//            //  phoneNoEditText.requestFocus();
//            return false;
//        }
//
//        if (strPhoneNo.length() <= 7) {
//            phoneNoEditText.setError("Telephone No should contain at least 8 characters.");
//            //   phoneNoEditText.requestFocus();
//            return false;
//        }
//
//        if (strPhoneNo.length() > 15) {
//            phoneNoEditText.setError("Telephone No should  not contain at most 15 characters.");
//            //   phoneNoEditText.requestFocus();
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

    private void captureImageInitialization() {
        /**
         * a selector dialog to display two image source options, from camera
         * ‘Take from camera’ and from existing files ‘Select from gallery’
         */
        final String[] items = new String[]{getResources().getString(dk.eatmore.demo.R.string.take_from_camera),
                getResources().getString(dk.eatmore.demo.R.string.select_from_gallery)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        View titleview = getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
        title.setText(getResources().getString(dk.eatmore.demo.R.string.select_image));
        builder.setCustomTitle(titleview);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    fileUri = getOutputMediaFileUri();

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    // start the image capture Intent
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Log.e(TAG, " item 111");
                    Crop.pickImage(NewUserActivity.this);
                }
            }
        });

        dialog = builder.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        //   Log.e("onActivityResult","onActivityResult"+result.toString());
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            //  Uri u = result.getData();
            beginCrop(fileUri);
        }
    }

    private void beginCrop(Uri source) {
        destination = getOutputMediaFileUri();
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Picasso.with(NewUserActivity.this).load(Crop.getOutput(result))
                    .transform(new CircleTransform()).into(profilePicImageView);
            imagePath = destination.toString();
            //   Log.e("handleCrop","handleCrop"+Crop.getOutput(result));

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Webshop");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("mediaStorageDir", "mediaStorageDir");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        //   android.util.Log.e("mediaFile","mediaFile"+mediaFile);
        return mediaFile;
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        // int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int memorycardPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermissionMemory = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (ReadPermissionMemory != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (memorycardPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                //  perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            &&
                            perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            &&
                            perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //   Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        // Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||

                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                                ) {
                            showDialogOK("Camera and  Gallery  access  Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(dk.eatmore.demo.R.string.dialog_positive), okListener)
                .setNegativeButton(getString(dk.eatmore.demo.R.string.Cancel), okListener)
                .create()
                .show();
    }

    private class SignUpCall extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            mSimpleArcDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            final String deviceLocale = Locale.getDefault().getLanguage();
            Log.e(TAG, deviceLocale);

            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(ApiCall.USER_SIGNUP);

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

                entity.addPart("r_token",
                        new StringBody(ApiCall.R_TOKEN));
                entity.addPart("r_key",
                        new StringBody(ApiCall.R_KEY));
                entity.addPart("email",
                        new StringBody(strEmail, Charset.forName("UTF-8")));
                entity.addPart("password_hash",
                        new StringBody(strPassword, Charset.forName("UTF-8")));
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
                entity.addPart("language",
                        new StringBody(deviceLocale, Charset.forName("UTF-8")));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                //  Log.e("Tag",""+strPostalCode+" "+"  "+strCityName);
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
                mSimpleArcDialog.dismiss();
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            mSimpleArcDialog.dismiss();
            Log.e(TAG, "Sign Up Response from server: " + result);
            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        String msg = null, dtitle = null;
        boolean status = false;
        try {
            JSONObject jObj = new JSONObject(message);
            status = jObj.getBoolean("status");

            if (status) {
                dtitle = getString(R.string.congrats);
                ApplicationClass.getInstance().getPrefManager().
                        setBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN, true);
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.KEY_USER_ID, jObj.getJSONObject("data").getString("id"));
                msg = getResources().getString(dk.eatmore.demo.R.string.sign_up_done_msg);
            } else {
                dtitle = "alert";

                if (jObj.getString("error_id").equals("1"))
                    msg = jObj.getString("msg");
                else {
                    String errorKey = jObj.getString("errorkey");
                    msg = jObj.getJSONObject("errors").getString(errorKey);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View titleview = getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
        title.setText(dtitle);
        builder.setCustomTitle(titleview);
        final boolean finalStatus = status;

        TextView myMsg = new TextView(this);
        myMsg.setText(msg);
        myMsg.setGravity(Gravity.START);
        myMsg.setPadding(15, 15, 15, 15);
        myMsg.setTextColor(Color.BLACK);
        builder.setView(myMsg);

        //  builder.setMessage(msg)
        builder.setCancelable(false)
                .setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (finalStatus) {
                            Intent intent = new Intent(NewUserActivity.this,
                                    MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else
                            dialog.dismiss();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
