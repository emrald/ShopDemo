package dk.eatmore.demo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dk.eatmore.demo.R;
import dk.eatmore.demo.interfaces.RecyclerViewOnClickListener;
import dk.eatmore.demo.model.MyOrders;

import java.util.ArrayList;

import static dk.eatmore.demo.myutils.Utility.convertCurrencyToDanish;

/**
 * Created by sachi on 2/7/2017.
 */

public class NewMyOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private static final int EMPTY_VIEW = 10;

    private RecyclerViewOnClickListener onAdapterItemClickListener;
    private ArrayList<MyOrders> myOrderList = new ArrayList<>();

    public NewMyOrdersAdapter(Context mContext, RecyclerViewOnClickListener listener) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.onAdapterItemClickListener = listener;
    }

    public void setArrayList(ArrayList<MyOrders> arrayList) {
        this.myOrderList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == EMPTY_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            EmptyViewHolder evh = null;
            evh = new EmptyViewHolder(v);
            return evh;
        }

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_item, parent, false);
        MyOrdersViewHolder vh = null;
        vh = new MyOrdersViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyOrdersViewHolder) {
            MyOrdersViewHolder vh = (MyOrdersViewHolder) holder;

            final int itemPosition = vh.getAdapterPosition();

            MyOrders myOrders = myOrderList.get(itemPosition);
            String orderNumber = myOrders.getOrder_no();
            String orderDate = myOrders.getOrder_date();
            String totalPayment = myOrders.getTotal_to_pay();
            String discount = myOrders.getDiscount();
            String expectedTime = myOrders.getExpectedTime();
//
            vh.orderNumberTextView.setText(orderNumber);
            vh.orderDateTextView.setText(orderDate);
            vh.totalPaymentTextView.setText(convertCurrencyToDanish(Double.parseDouble(totalPayment)));
            if (discount == null || discount.equalsIgnoreCase("null") || Double.parseDouble(discount) <= 0) {
                vh.discountLinearLayout.setVisibility(View.INVISIBLE);
            } else {
                vh.discountLinearLayout.setVisibility(View.VISIBLE);
                vh.discountTextView.setText(convertCurrencyToDanish(Double.parseDouble(discount)));
            }
            vh.orderDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAdapterItemClickListener.buttonOnClick(v, itemPosition);
                }
            });

            vh.expectedTimeTextView.setText(expectedTime);
        } else {
//            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class MyOrdersViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView orderNumberTextView;
        TextView orderDateTextView;
        TextView totalPaymentTextView;
        TextView discountTextView;
        TextView expectedTimeTextView;

        LinearLayout discountLinearLayout;
        LinearLayout orderDetailsButton;

        MyOrdersViewHolder(View itemView) {
            super(itemView);
//            cv = (CardView) itemView.findViewById(R.id.cv_my_orders);
            orderNumberTextView = (TextView) itemView.findViewById(R.id.tv_order_no_value);
            orderDateTextView = (TextView) itemView.findViewById(R.id.tv_order_date_value);
            totalPaymentTextView = (TextView) itemView.findViewById(R.id.tv_total_payment_value);
            discountTextView = (TextView) itemView.findViewById(R.id.tv_discount_value);
            expectedTimeTextView = (TextView) itemView.findViewById(R.id.tv_expected_time_value);

            discountLinearLayout = (LinearLayout) itemView.findViewById(R.id.ll_discount);
            orderDetailsButton = (LinearLayout) itemView.findViewById(R.id.btn_order_details);
        }
    }
}
