package dk.eatmore.demo.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dk.eatmore.demo.model.PaymentMethodPojo;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class PaymentMethodAdaptor extends RecyclerView.Adapter<PaymentMethodAdaptor.PaymentMethodViewHolder> {

    List<PaymentMethodPojo> list = Collections.emptyList();
    Context context;

    public PaymentMethodAdaptor(List<PaymentMethodPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PaymentMethodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_row, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(dk.eatmore.demo.R.layout.payment_method_item, parent, false);
        PaymentMethodViewHolder holder = new PaymentMethodViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(PaymentMethodViewHolder holder, int position) {
        holder.paymentTitle.setText(list.get(position).getPaymetTitle());

        if (!list.get(position).getPaymentImage().isEmpty())
            Picasso.with(context)
                    .load(list.get(position).getMainImagePath() +
                            list.get(position).getPaymentImage())
//                    .resize(100, 105)
                    .into(holder.paymentImage);
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class PaymentMethodViewHolder extends RecyclerView.ViewHolder {
        TextView paymentTitle;
        ImageView paymentImage;

        PaymentMethodViewHolder(View itemView) {
            super(itemView);
            paymentTitle = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.paymentTitle);
            paymentImage = (ImageView) itemView.findViewById(dk.eatmore.demo.R.id.paymentImage);
        }

    }
}