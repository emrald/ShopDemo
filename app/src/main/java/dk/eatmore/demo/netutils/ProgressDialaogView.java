package dk.eatmore.demo.netutils;

import android.content.Context;
import android.graphics.Color;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

/**
 * Created by ADMIN on 21-11-2016.
 */

public class ProgressDialaogView {
    private SimpleArcDialog mSimpleArcDialog;
    private ArcConfiguration configuration;

    public ProgressDialaogView() {
    }

    public SimpleArcDialog getProgressDialog(Context mContext) {
        mSimpleArcDialog = new SimpleArcDialog(mContext);
        mSimpleArcDialog.setCancelable(false);

        configuration = new ArcConfiguration(mContext);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(mContext.getString(dk.eatmore.demo.R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mSimpleArcDialog.setConfiguration(configuration);

        return mSimpleArcDialog;
    }

    public SimpleArcDialog getProgressDialog(Context mContext, String msg) {
        mSimpleArcDialog = new SimpleArcDialog(mContext);
        mSimpleArcDialog.setCancelable(false);

        configuration = new ArcConfiguration(mContext);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        if (msg != null)
            configuration.setText(msg);
        else
            configuration.setText(mContext.getString(dk.eatmore.demo.R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mSimpleArcDialog.setConfiguration(configuration);

        return mSimpleArcDialog;
    }

    public void dismissSimpleArcDialog(SimpleArcDialog mSimpleArcDialog) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
    }

    public void setMessage(String msg) {
        configuration.setText(msg);
    }
}
