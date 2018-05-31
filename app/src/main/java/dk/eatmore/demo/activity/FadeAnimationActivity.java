package dk.eatmore.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import dk.eatmore.demo.netutils.ProgressDialaogView;
import com.leo.simplearcloader.SimpleArcDialog;

public class FadeAnimationActivity extends AppCompatActivity {

    ImageView fadeImage;
    SimpleArcDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(dk.eatmore.demo.R.layout.fade_animation_view);
        pDialog = new ProgressDialaogView().getProgressDialog(this);

        //  fade_image = (LinearLayout) findViewById(R.id.fade_image);
        //  smallBang = SmallBang.attach2Window(this);
        // mIvSplashAnim = AnimationUtils.loadAnimation(this, R.anim.splash);
        // fade_image.startAnimation(mIvSplashAnim);
//        fadeImage = (ImageView) findViewById(R.id.fadeImage);
//
//        String resImg = ApplicationClass.getInstance().getPrefManager().
//                getStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE);
//
//        Picasso.with(this)
//                .load(resImg)
//                .into(fadeImage);

        pDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pDialog.dismiss();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}
