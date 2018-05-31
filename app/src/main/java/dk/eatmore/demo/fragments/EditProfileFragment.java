package dk.eatmore.demo.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.activity.LoginActivity;
import dk.eatmore.demo.interfaces.DeleteUserAccountCallback;
import dk.eatmore.demo.interfaces.GetCityListCallback;
import dk.eatmore.demo.interfaces.GetUserProfileDataCallback;
import dk.eatmore.demo.myutils.AndroidMultiPartEntity;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.myutils.CircleTransform;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.SharedPrefClass;
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

public class EditProfileFragment extends Fragment implements GetCityListCallback, GetUserProfileDataCallback, DeleteUserAccountCallback {

    private static final String TAG = EditProfileFragment.class.getSimpleName();
    private View view;
    private Button btnSave;
    private Button cityNameButton;
    private Button btnCancel;
    private Button postalCodeButton;

    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassowrd;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputStreetName;
    private EditText floorDoorEditText;
    private EditText inputPhoneNo;

    CustomAlertAdapter arrayAdapter;
    private ArrayList<String> array_sort;
    int textlength = 0;
    private android.app.AlertDialog myalertDialog = null;

    private ImageView imgback, settingImage;
    SimpleArcDialog mDialog;

    private String strEmail;
    private String strFirstName;
    private String strLastName;
    private String strStreetName;
    private String floorDoorString = "";
    private String strPostalCode;
    private String strCityName;
    private String strPhoneNo;

    ImageView inputprofileimage;
    String imagePath, userId;
    long totalSize = 0;
    Uri fileUri, destination;
    public static final int REQUEST_CAMERA = 0x1;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 0x4;
    android.app.AlertDialog dialog;
    String[] allPostalName_array;
    private SharedPrefClass mSharedPrefClass = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(dk.eatmore.demo.R.layout.edit_profile_view, container, false);
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();
        setupUI(view.findViewById(dk.eatmore.demo.R.id.edit_profile_view));
        initObjects();
        initToolBar();
        initView();
        initDialog();
        captureImageInitialization();
        setOnClickListeners();
        getUserProfileData();
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

    private void initObjects() {
        mSharedPrefClass = new SharedPrefClass(getActivity());
    }

    private void initToolBar() {
        settingImage = (ImageView) view.findViewById(dk.eatmore.demo.R.id.toolbar_setting);
        imgback = (ImageView) view.findViewById(dk.eatmore.demo.R.id.imgback_edit);
        TextView title = (TextView) view.findViewById(dk.eatmore.demo.R.id.tootlbar_title_edit);
        title.setText(getResources().getString(dk.eatmore.demo.R.string.edit_profile));

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        settingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), settingImage);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(dk.eatmore.demo.R.menu.poupup_menu, popup.getMenu());
                if (mSharedPrefClass.isFacebookLogin()) {
                    popup.getMenu().getItem(0).setVisible(false);
                } else {
                    popup.getMenu().getItem(0).setVisible(true);
                }

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals(getActivity().getResources().getString(dk.eatmore.demo.R.string.change_password))) {
                            ChangePassswordFragment fragment = new ChangePassswordFragment();
                            // title="Explore The MindSets";
                            FragmentManager fragmentManager = ((FragmentActivity)
                                    getActivity()).getSupportFragmentManager();
                            FragmentTransaction fragment_changepass = fragmentManager.beginTransaction();
                            fragment_changepass.add(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragment_changepass.addToBackStack(InfoBarFragment.class.getName());
                            fragment_changepass.commit();
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                            View titleview = LayoutInflater.from(getActivity()).inflate(dk.eatmore.demo.R.layout.delete_account_title, null);
                            TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.deletedialogtitle);
                            title.setText(dk.eatmore.demo.R.string.delete_account_confirm);
                            alertDialog.setCustomTitle(titleview);
                            //   .setMessage("Are you sure you want to delete this entry?")
                            alertDialog
                                    .setPositiveButton(dk.eatmore.demo.R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteUserAccount();
                                        }
                                    })
                                    .setNegativeButton(dk.eatmore.demo.R.string.dialog_no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })

                                    .show();
                        }
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });
    }

    private void initView() {
        inputprofileimage = (ImageView) view.findViewById(dk.eatmore.demo.R.id.edit_inputprofileimage);
        inputEmail = (EditText) view.findViewById(dk.eatmore.demo.R.id.edt_inputEmail);
        inputPassword = (EditText) view.findViewById(dk.eatmore.demo.R.id.edit_inputPassword);
        inputConfirmPassowrd = (EditText) view.findViewById(dk.eatmore.demo.R.id.edit_inputCPassword);
        inputFirstName = (EditText) view.findViewById(dk.eatmore.demo.R.id.edit_nputFirstName);
        inputLastName = (EditText) view.findViewById(dk.eatmore.demo.R.id.edit_inputLastName);
        inputStreetName = (EditText) view.findViewById(dk.eatmore.demo.R.id.edit_inputStreetName);
        floorDoorEditText = (EditText) view.findViewById(dk.eatmore.demo.R.id.et_floor_door_edit);

        inputStreetName.setSingleLine(true);
        inputStreetName.setLines(10);
        inputStreetName.setHorizontallyScrolling(false);

        postalCodeButton = (Button) view.findViewById(dk.eatmore.demo.R.id.edit_inputtxtPostelCode);
        cityNameButton = (Button) view.findViewById(dk.eatmore.demo.R.id.edit_btnCityName);
        inputPhoneNo = (EditText) view.findViewById(dk.eatmore.demo.R.id.edit_inputTelephone);
        btnSave = (Button) view.findViewById(dk.eatmore.demo.R.id.edit_BtnSave);
        btnCancel = (Button) view.findViewById(dk.eatmore.demo.R.id.edit_btnCancel);
    }

    private void initDialog() {
        mDialog = new SimpleArcDialog(getActivity());
        mDialog.setCancelable(false);

        ArcConfiguration configuration = new ArcConfiguration(getActivity());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(getString(dk.eatmore.demo.R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mDialog.setConfiguration(configuration);
    }

    private void captureImageInitialization() {
        /**
         * a selector dialog to display two image source options, from camera
         * ‘Take from camera’ and from existing files ‘Select from gallery’
         */
        final String[] items = new String[]{getActivity().getResources().getString(dk.eatmore.demo.R.string.take_from_camera),
                getActivity().getResources().getString(dk.eatmore.demo.R.string.select_from_gallery)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.select_dialog_item, items);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

        View titleview = getActivity().getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
        title.setText(getActivity().getResources().getString(dk.eatmore.demo.R.string.select_image));
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
                    //  Crop.pickImage(getActivity());
                    Crop.pickImage(getContext(), EditProfileFragment.this);
                }
            }
        });

        dialog = builder.create();
    }

    private void setOnClickListeners() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        //   getActivity().getSupportFragmentManager().popBackStack();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getValidation())
                    new EditProfileCall().execute();
            }
        });

        inputprofileimage.setOnClickListener(new View.OnClickListener() {
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
                android.app.AlertDialog.Builder myDialog = new android.app.AlertDialog.Builder(getActivity());

                View titleview = getActivity().getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
                TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
                title.setText(getActivity().getResources().getString(dk.eatmore.demo.R.string.postal_code));
                myDialog.setCustomTitle(titleview);

                final EditText editText = new EditText(getActivity());
                editText.setTextColor(Color.BLACK);
                editText.setHint(dk.eatmore.demo.R.string.enter_postal_code);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                int maxLength = 4;
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

                final ListView listview = new ListView(getActivity());

                editText.setCompoundDrawablesWithIntrinsicBounds(dk.eatmore.demo.R.drawable.ic_search_grey_500_24dp, 0, 0, 0);
                array_sort = new ArrayList<String>(Arrays.asList(allPostalName_array));

                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(editText);
                layout.addView(listview);
                myDialog.setView(layout);
                arrayAdapter = new CustomAlertAdapter(getActivity(), array_sort);
                listview.setAdapter(arrayAdapter);
                listview.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
                        myalertDialog.dismiss();
                        postalCodeButton.setText(array_sort.get(position));
                        strPostalCode = array_sort.get(position);
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
                        listview.setAdapter(new CustomAlertAdapter(getActivity(), array_sort));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            //  Uri u = result.getData();
            beginCrop(fileUri);

        }
    }

    private void beginCrop(Uri source) {
        destination = getOutputMediaFileUri();
        Crop.of(source, destination).asSquare().start(getContext(), EditProfileFragment.this, Crop.REQUEST_CROP);

        //    Crop.of(source, destination).asSquare().start(getActivity());
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Picasso.with(getActivity())
                    .load(Crop.getOutput(result))
                    .transform(new CircleTransform())
                    .placeholder(dk.eatmore.demo.R.drawable.user_avatar_mainpicture)
                    .into(inputprofileimage);

            imagePath = destination.toString();
            //   Log.e("handleCrop","handleCrop"+Crop.getOutput(result));

        } else if (resultCode == Crop.RESULT_ERROR) {
            //  Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
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

    private boolean getValidation() {
        strFirstName = inputFirstName.getText().toString().trim();
        strLastName = inputLastName.getText().toString().trim();
        strEmail = inputEmail.getText().toString().trim();
        strPhoneNo = inputPhoneNo.getText().toString().trim();
        strStreetName = inputStreetName.getText().toString().trim();
        floorDoorString = floorDoorEditText.getText().toString().trim();

        if (!(userId != null && userId.length() > 0)) {
            Toast.makeText(getActivity(), dk.eatmore.demo.R.string.try_again, Toast.LENGTH_SHORT).show();
            return false;
        } else if (strFirstName.isEmpty()) { // Check First Name
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

//        if (!isValidEmail(strEmail)) {
//            inputEmail.setError(getString(R.string.invalid_email));
//            // inputEmail.requestFocus();
//        }
//        if (!strFirstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
//            inputFirstName.setError("First name charaters limit should be come between 3-100");
//            //  inputFirstName.requestFocus();
//        } else
//            inputFirstName.setError(null);
//
//        if (strFirstName.length() <= 2) {
//            inputFirstName.setError("First name charaters limit should be come between 3-100");
//            // inputLastName.requestFocus();
//        } else
//            inputFirstName.setError(null);
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
//        if (strStreetName.isEmpty()) {
//            inputStreetName.setError("Address is not valid.");
//            //  inputStreetName.requestFocus();
//        }
//        if (strPostalCode == null) {
//            postalCodeButton.setError("Postal code name is not valid.");
//            // postalCodeButton.requestFocus();
//
//        } else
//            postalCodeButton.setError(null);
//        if (!(strCityName != null && strCityName.length() > 0)) {
//            cityNameButton.setError("Empty city name");
//            //  cityNameButton.requestFocus();
//        }
//
//        if (strPhoneNo.isEmpty()) {
//            inputPhoneNo.setError("Phone number is not valid.");
//            // inputPhoneNo.requestFocus();
//        }
//
//        if (strPhoneNo.length() <= 7) {
//            inputPhoneNo.setError("Telephone No should contain at least 8 characters.");
//            //   inputPhoneNo.requestFocus();
//        }
//
//        if (!(userId != null && userId.length() > 0)) {
//            Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        strEmail = inputEmail.getText().toString().trim();
//        if (!isValidEmail(strEmail)) {
//            inputEmail.setError(getString(R.string.invalid_email));
//            return false;
//        }
//
//        if (!strFirstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
//            inputFirstName.setError("Only alpha numeric characters are allowed.");
//            //inputFirstName.requestFocus();
//            return false;
//        }
//
//        if (!strLastName.matches("^[A-Za-z_ÆØÅæøå ]+$") && strLastName.length() >= 3) {
//            inputLastName.setError("Only alpha numeric characters are allowed.");
//            // inputLastName.requestFocus();
//            return false;
//        }
//
//        if (strLastName.length() <= 2) {
//            inputLastName.setError("Last name charaters limit should be come between 3-100");
//            // inputLastName.requestFocus();
//            return false;
//        }
//
//        if (strStreetName.isEmpty()) {
//            inputStreetName.setError("Address is not valid.");
//            //  inputStreetName.requestFocus();
//
//            return false;
//        }
//        if (!(strPostalCode != null && strPostalCode.length() > 0)) {
//            postalCodeButton.setError("Postal code name is not valid.");
////            postalCodeButton.requestFocus();
//            return false;
//        }
//
//        if (strPhoneNo.isEmpty() && strPhoneNo.length() >= 8) {
//            inputPhoneNo.setError("Empty phone number");
//            //    inputPhoneNo.requestFocus();
//            return false;
//        }
//
//        if (strPhoneNo.length() <= 7) {
//            inputPhoneNo.setError("Telephone No should contain at least 8 characters.");
//            //   inputPhoneNo.requestFocus();
//            return false;
//        }
        return true;
    }

    private void getCityList(final String postalId) {
        mDialog.show();
        String tag_string_req = "getCityList";
        NetworkUtils.callGetCityList(postalId, tag_string_req, EditProfileFragment.this);
    }

    @Override
    public void getCityListCallbackSuccess(String success) {
        Log.e(TAG, "getCityList Response: " + success);
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        ArrayList<String> stringArrayListNAME = new ArrayList<String>();
        JSONObject jObj;
        try {
            jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            if (status) {

                cityNameButton.setText(jObj.getString("city_name"));
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
        Toast.makeText(getActivity(),
                volleyError.getMessage(), Toast.LENGTH_LONG).show();

    }

    private void getUserProfileData() {
        String tag_string_req = "suggested_user";
        mDialog.show();

        NetworkUtils.callGetUserProfileData(tag_string_req, EditProfileFragment.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.GET_USER_PROFILE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //   Log.e("GET_INDIVIDUAL_MEMBER", "GET_INDIVIDUAL_MEMBER Response: " + response.toString());
//                if (mDialog != null && mDialog.isShowing())
//                    mDialog.dismiss();
//
//                if (response != null) {
//                    JSONObject jsonresponse = null;
//                    try {
//                        jsonresponse = new JSONObject(response);
//                        parseJsonFeed(jsonresponse);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Log.e("param", "Login Error: " + error.getMessage());
//                        if (mDialog != null && mDialog.isShowing())
//                            mDialog.dismiss();
//                    }
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                params.put("id", ApplicationClass.getInstance().getPrefManager()
//                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
//                return params;
//            }
//
//        };
//
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void getUserProfileDataCallbackSuccess(String success) {
        //   Log.e("GET_INDIVIDUAL_MEMBER", "GET_INDIVIDUAL_MEMBER Response: " + response.toString());
        if (mDialog != null && mDialog.isShowing())
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
    public void getUserProfileDataCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableUserProfile();
        }
    }

    public void showDialogInternetUnavailableUserProfile() {
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
                getUserProfileData();
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
            Log.e("edit p", "edit  " + jsonresponse);
            JSONObject jObj = jsonresponse.getJSONObject("data");
            boolean status = jsonresponse.getBoolean("status");
            if (status) {
                userId = jObj.getString("id");
                inputFirstName.setText(jObj.getString("first_name"));
                inputLastName.setText(jObj.getString("last_name"));
                inputEmail.setText(jObj.getString("email"));

                inputPhoneNo.setText(checkNull(jObj, "telephone_no"));
                inputStreetName.setText(checkNull(jObj, "street"));
                floorDoorEditText.setText(checkNull(jObj, "address"));

                strPostalCode = checkNull(jObj, "postal_code");
                postalCodeButton.setText(checkNull(jObj, "postal_code"));


                strCityName = checkNull(jObj, "city");
                cityNameButton.setText(checkNull(jObj, "city"));

                if (mSharedPrefClass.isFacebookLogin()) {
                    String imagePathFB = ApplicationClass.getInstance().getPrefManager().
                            getStringPreferences(MyPreferenceManager.FACEBOOK_IMAGE);
                    Picasso.with(getActivity())
                            .load(imagePathFB)
                            .transform(new CircleTransform())
                            //.transform(new CircleTransformation())
                            .placeholder(dk.eatmore.demo.R.drawable.user_avatar_mainpicture)
                            .into(inputprofileimage);
                } else {
                    Picasso.with(getActivity())
                            .load(checkNull(jsonresponse, "image_path") + checkNull(jObj, "image"))
                            .transform(new CircleTransform())
                            //.transform(new CircleTransformation())
                            .placeholder(dk.eatmore.demo.R.drawable.user_avatar_mainpicture)
                            .into(inputprofileimage);
                }

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
            ex.printStackTrace();
            Log.e(TAG, "" + ex.toString());
        }
    }

    private String checkNull(final JSONObject json, final String key) {
        return json.isNull(key) ? "" : json.optString(key);
    }

    private class EditProfileCall extends AsyncTask<Void, Integer, String> {
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
            HttpPost httppost = new HttpPost(ApiCall.EDIT_PROFILE);

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

                entity.addPart("id", new StringBody(userId));

                entity.addPart("r_token",
                        new StringBody(ApiCall.R_TOKEN, Charset.forName("UTF-8")));
                entity.addPart("r_key",
                        new StringBody(ApiCall.R_KEY, Charset.forName("UTF-8")));
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
                        new StringBody(floorDoorString, Charset.forName("UTF-8")));
                entity.addPart("postal_code",
                        new StringBody(strPostalCode, Charset.forName("UTF-8")));
//                entity.addPart("city",
//                        new StringBody(strCityName));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Log.e("Tag",""+strPostalCode+" "+"  "+strCityName);
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

            } catch (Exception e) {
                mDialog.dismiss();
                responseString = e.toString();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mDialog != null && mDialog.isShowing())
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
        String msg = null;
        String alertMsg = null;
        boolean status = false;
        try {
            JSONObject jObj = new JSONObject(message);
            status = jObj.getBoolean("status");

            if (status) {
                alertMsg = getString(R.string.congrats);

                ApplicationClass.getInstance().getPrefManager().
                        setBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN, true);
                ApplicationClass.getInstance().getPrefManager().
                        setStringPreferences(MyPreferenceManager.KEY_USER_ID, jObj.getJSONObject("data").getString("id"));
                msg = getResources().getString(dk.eatmore.demo.R.string.edit_done);
            } else {
                alertMsg = getString(dk.eatmore.demo.R.string.alert);
                if (jObj.getString("error_id").equals("2")) {
                    msg = jObj.getJSONObject("errors").getString(jObj.getString("errorkey"));
                } else
                    msg = jObj.getString("msg");
            }
        } catch (Exception ex) {
            alertMsg = getString(dk.eatmore.demo.R.string.alert);
            msg = getString(dk.eatmore.demo.R.string.try_again);
            ex.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        View titleview = getActivity().getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
        title.setText(alertMsg);
        builder.setCustomTitle(titleview);
        final boolean finalStatus = status;
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(getString(dk.eatmore.demo.R.string.dialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (finalStatus) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else
                            dialog.dismiss();


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void deleteUserAccount() {
        mDialog.show();
        String tag_string_req = "deleteUserAccount";

        NetworkUtils.callDeleteUserAccount(tag_string_req, EditProfileFragment.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.DELETE_ACCOUNT, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                mDialog.dismiss();
//                Log.e(TAG, "storePayment Response: " + response);
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//                    // Check for error node in json
//                    if (status) {
//                        ApplicationClass.getInstance().getPrefManager().clearSharedPreference();
//                        showDeleteAccountDialog("Account deleted successfully", true);
//                    } else {
//                        showDeleteAccountDialog(getString(R.string.try_again), false);
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
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void deleteUserAccountCallbackSuccess(String success) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        Log.e(TAG, "storePayment Response: " + success);
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
                ApplicationClass.getInstance().getPrefManager().clearSharedPreference();
                showDeleteAccountDialog(getString(dk.eatmore.demo.R.string.deleted_account_success), true);
            } else {
                showDeleteAccountDialog(getString(dk.eatmore.demo.R.string.try_again), false);
            }
        } catch (JSONException e) {
            // hideDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUserAccountCallbackError(VolleyError volleyError) {
        Log.e(TAG, "deleteUserAccount Error: " + volleyError.getMessage());
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        String msg = getResources().getString(R.string.try_again);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void showDeleteAccountDialog(String message, final boolean actionFlag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        View titleview = getActivity().getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
        title.setText(dk.eatmore.demo.R.string.alert);
        builder.setCustomTitle(titleview);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(dk.eatmore.demo.R.string.dialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (actionFlag) {
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getActivity().startActivity(i);
                            ((Activity) getActivity()).finish();
                        } else
                            dialog.dismiss();


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);

        // int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int memorycardPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermissionMemory = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

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
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
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
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA) ||

                                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                                ) {
                            showDialogOK("Camera and Gallery access Permission required for this app",
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
                            Toast.makeText(getActivity(), "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(getString(dk.eatmore.demo.R.string.dialog_positive), okListener)
                .setNegativeButton(getString(dk.eatmore.demo.R.string.Cancel), okListener)
                .create()
                .show();
    }

}
