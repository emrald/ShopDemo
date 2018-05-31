package dk.eatmore.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import dk.eatmore.demo.model.MyOrders;

import java.util.ArrayList;

/**
 * Created by pallavi.b on 22-Apr-16.
 */
public class MyOrdersAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    ArrayList<MyOrders> myOrderList;

    public MyOrdersAdapter(Context mContext, ArrayList myOrderListm) {
        this.mContext = mContext;
        this.myOrderList = myOrderListm;
    }

    @Override
    public int getCount() {
        return myOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    ViewHolder holder;
    ;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();

        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(dk.eatmore.demo.R.layout.custom_my_orders, parent, false);
            holder.orderNoText = (TextView) convertView.findViewById(dk.eatmore.demo.R.id.myorderIdText);
            holder.orderDateText = (TextView) convertView.findViewById(dk.eatmore.demo.R.id.myorderDaeText);
            holder.amountText = (TextView) convertView.findViewById(dk.eatmore.demo.R.id.myorderampuntText);

            convertView.setTag(holder);
        } else

            holder = (ViewHolder) convertView.getTag();

        MyOrders myOrders = myOrderList.get(position);

        holder.orderNoText.setText(myOrders.getOrder_no());
        holder.orderDateText.setText(myOrders.getOrder_date());
        holder.amountText.setText(myOrders.getTotal_to_pay());


        return convertView;
    }

    public class ViewHolder {

        TextView orderNoText, orderDateText, amountText;
    }
}
