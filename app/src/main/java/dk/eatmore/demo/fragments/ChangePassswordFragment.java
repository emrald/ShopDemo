package dk.eatmore.demo.fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.interfaces.ChangePasswordCallback;
import dk.eatmore.demo.myutils.PasswordView;
import dk.eatmore.demo.myutils.SharedPrefClass;
import dk.eatmore.demo.myutils.Utility;
import dk.eatmore.demo.network.NetworkUtils;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassswordFragment extends Fragment implements ChangePasswordCallback {
    private static final String TAG = ChangePassswordFragment.class.getSimpleName();
    private View view;
    private ImageView backImageView;
    private SimpleArcDialog mDialog;
    private PasswordView inputOldPassword;
    private PasswordView inputNewPassword;
    private PasswordView inputConfirmPassword;
    private Button btnChnagePassword;

    // Variables
    private String oldPasswordString;
    private String newPassword;
    private String confirmPassword;

    private SharedPrefClass mSharedPrefClass = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.change_passsword_view, container, false);
        // Hiding Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).hideBottomNavigation();
        setupUI(view);
        initToolBar();
        initView();
        initDialog();
        initObjects();
        btnChnagePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getValidation())
                    changePassowrd(newPassword);
            }
        });

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

    private void initToolBar() {
        TextView title = (TextView) view.findViewById(R.id.tootlbar_title);
        title.setText(getResources().getString(R.string.change_password));
        backImageView = (ImageView) view.findViewById(R.id.imgback);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        inputOldPassword = (PasswordView) view.findViewById(R.id.et_old_password);
        inputNewPassword = (PasswordView) view.findViewById(R.id.editChangePass);
        inputConfirmPassword = (PasswordView) view.findViewById(R.id.editconfirmPass);
        btnChnagePassword = (Button) view.findViewById(R.id.btnChangePassword);
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

    private void initObjects() {
        mSharedPrefClass = new SharedPrefClass(getActivity());
    }

    private boolean getValidation() {
        oldPasswordString = inputOldPassword.getText().toString().trim();
        newPassword = inputNewPassword.getText().toString().trim();
        confirmPassword = inputConfirmPassword.getText().toString().trim();

        if (oldPasswordString.isEmpty()) {
            inputOldPassword.setError(getString(R.string.cannot_be_blank_field));
            inputOldPassword.requestFocus();
            return false;
        } else if (newPassword.isEmpty()) {
            inputNewPassword.setError(getString(R.string.cannot_be_blank_field));
            inputNewPassword.requestFocus();
            return false;
        } else if (newPassword.length() <= 5) {
            inputNewPassword.setError(getString(R.string.password_minimum_length));
            inputNewPassword.requestFocus();
            return false;
        } else if (confirmPassword.isEmpty()) {
            inputConfirmPassword.setError(getString(R.string.cannot_be_blank_field));
            inputConfirmPassword.requestFocus();
            return false;
        } else if (!newPassword.equals(confirmPassword)) {
            inputConfirmPassword.setError(getString(R.string.confirm_password_not_match));
            inputConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void changePassowrd(final String mNewPassword) {
        String tag_string_req = "changePassword";
        mDialog.show();

        NetworkUtils.callChangePassword(oldPasswordString, mNewPassword, tag_string_req,
                ChangePassswordFragment.this);
    }

    @Override
    public void changePasswordCallbackSuccess(String success) {
        Log.e(TAG, "changePassowrd Response: " + success);
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

        try {
            JSONObject data = new JSONObject(success);
            if (data.getBoolean("status"))
                showAlert("Your Password has been changed succcessfully");
            else {
                if (data.has("msg")) {
                    showAlert(data.getString("msg"));
                } else {
                    if (data.getString("error_id").equals("1"))
                        showAlert(data.getString("msg"));
                    else {
                        String errorKey = data.getString("errorkey");
                        showAlert(data.getJSONObject("errors").getString(errorKey));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void changePasswordCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();

//        if (volleyError instanceof NoConnectionError) {
        String msg = getResources().getString(R.string.try_again);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//        }
    }

    public void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        View titleview = getActivity().getLayoutInflater().inflate(R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(R.id.dialogtitle);
        title.setText(R.string.alert);
        builder.setCustomTitle(titleview);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        getActivity().getSupportFragmentManager().popBackStack();
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
