package dk.eatmore.demo.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dk.eatmore.demo.fragments.LoginFragmentMain;

public class LoginActivity extends AppCompatActivity {
    //for custom alert dialog
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(dk.eatmore.demo.R.layout.activity_login);
//        initBroadcastReceiver();
        Intent intent = getIntent();
        if (intent != null) {
            String deletedUser = intent.getStringExtra("deletedUser");

            if (deletedUser != null && deletedUser.equals("1")) {
                callAlertDialog();
            }
        }
        openFragment(new LoginFragmentMain());
    }


    private void openFragment(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(dk.eatmore.demo.R.id.frgamentContainer, fragment)
                .commit();
    }

    private void callAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        View view = inflater.inflate(dk.eatmore.demo.R.layout.dilaog_info_layout, null);

        TextView textViewTitle = (TextView) view.findViewById(dk.eatmore.demo.R.id.textviewinfoTitle);
        Button butonOk = (Button) view.findViewById(dk.eatmore.demo.R.id.buttonAaddtocartOk);
        TextView dialogText = (TextView) view.findViewById(dk.eatmore.demo.R.id.addedtocartText);
        textViewTitle.setText(dk.eatmore.demo.R.string.alert);
        builder.setView(view);
        dialogText.setTypeface(null, Typeface.BOLD);
        dialogText.setText("Sorry for inconvenience your account was deleted.");

        final AlertDialog forgotDialog = builder.create();
        forgotDialog.show();


        butonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotDialog.dismiss();
            }
        });
    }
}
