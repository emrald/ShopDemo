package dk.eatmore.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import dk.eatmore.demo.R;
import dk.eatmore.demo.model.PaymentOption;

import java.util.ArrayList;

/**
 * Created by ADMIN on 25-11-2016.
 */

public class PaymentOptionAdapter extends BaseAdapter {
    Context context;
    ArrayList<PaymentOption> paymentOptions;

    public PaymentOptionAdapter(Context context, ArrayList<PaymentOption> paymentOptions) {
        this.context = context;
        this.paymentOptions = paymentOptions;
    }

    @Override
    public int getCount() {
        return paymentOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.payment_option_row, parent, false);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.paymentOptionImage);
        imageView.setVisibility(View.GONE);
        TextView textView = (TextView) convertView.findViewById(R.id.paymentOptionText);
        float scale = context.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (16 * scale + 0.5f);
        textView.setPadding(dpAsPixels, 0, 0, 0);

        PaymentOption paymentOption = paymentOptions.get(position);

//        Picasso.with(context).load(paymentOption.getPayentImage())
//                .resize(100,105).into( imageView);


        textView.setText(paymentOption.getPayentName());
        return convertView;
    }
}
