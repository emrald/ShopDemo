package dk.eatmore.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import dk.eatmore.demo.R;
import dk.eatmore.demo.model.ShippingChargeObject;
import dk.eatmore.demo.myutils.Utility;

import java.util.ArrayList;

/**
 * Created by sachi on 3/14/2017.
 */
public class ShippingChargeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ShippingChargeObject> mShippingChargeObjects;

    public ShippingChargeAdapter(Context context, ArrayList<ShippingChargeObject> mShippingChargeObjects) {
        this.context = context;
        this.mShippingChargeObjects = mShippingChargeObjects;
    }

    @Override
    public int getCount() {
        return mShippingChargeObjects.size();
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
        ShippingChargeViewHolder mShippingChargeViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shipping_charge_row, parent, false);
            mShippingChargeViewHolder = new ShippingChargeViewHolder();
            mShippingChargeViewHolder.fromTextView = (TextView) convertView.findViewById(R.id.tv_from_shipping_charge);
            mShippingChargeViewHolder.toTextView = (TextView) convertView.findViewById(R.id.tv_to_shipping_charge);
            mShippingChargeViewHolder.chargeTextView = (TextView) convertView.findViewById(R.id.tv_charge_shipping_charge);

            convertView.setTag(mShippingChargeViewHolder);
        } else {
            mShippingChargeViewHolder = (ShippingChargeViewHolder) convertView.getTag();
        }

        ShippingChargeObject mShippingChargeObject = mShippingChargeObjects.get(position);
        String from = mShippingChargeObject.getFrom_pd();
        String to = mShippingChargeObject.getTo_pd();
        String price = mShippingChargeObject.getPrice();

        from = Utility.convertCurrencyToDanish(Double.parseDouble(from));
        price = Utility.convertCurrencyToDanish(Double.parseDouble(price));

        if (to == null || to.equalsIgnoreCase("null"))
            to = context.getString(R.string.onwards);
        else
            to = Utility.convertCurrencyToDanish(Double.parseDouble(to));

        mShippingChargeViewHolder.fromTextView.setText(from);
        mShippingChargeViewHolder.toTextView.setText(to);
        mShippingChargeViewHolder.chargeTextView.setText(price);
        return convertView;
    }

    private static class ShippingChargeViewHolder {
        TextView fromTextView;
        TextView toTextView;
        TextView chargeTextView;
    }
}