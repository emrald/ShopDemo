package dk.eatmore.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dk.eatmore.demo.model.OrderProductExtraToppingGroupObject;

import java.util.List;

/**
 * Created by sachi on 2/8/2017.
 */
public class OrderProductExtraToppingGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private static final int EMPTY_VIEW = 10;

    private List<OrderProductExtraToppingGroupObject> orderProductExtraToppingGroupObject;

    public OrderProductExtraToppingGroupAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setArrayList(List<OrderProductExtraToppingGroupObject> orderProductExtraToppingGroupObject) {
        this.orderProductExtraToppingGroupObject = orderProductExtraToppingGroupObject;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == EMPTY_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(dk.eatmore.demo.R.layout.empty_view, parent, false);
            EmptyViewHolder evh = null;
            evh = new EmptyViewHolder(v);
            return evh;
        }

        v = LayoutInflater.from(parent.getContext()).inflate(dk.eatmore.demo.R.layout.order_product_extra_topping_group_item, parent, false);
        OrderProductExtraToppingGroupViewHolder vh = null;
        vh = new OrderProductExtraToppingGroupViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderProductExtraToppingGroupViewHolder) {
            OrderProductExtraToppingGroupViewHolder vh = (OrderProductExtraToppingGroupViewHolder) holder;

            final int itemPosition = vh.getAdapterPosition();

            OrderProductExtraToppingGroupObject mOrderProductExtraToppingGroupObject = orderProductExtraToppingGroupObject.get(itemPosition);
            String ingredientName = mOrderProductExtraToppingGroupObject.getIngredient_name();
            vh.extraToppingGroupTextView.setText(ingredientName);
        }
    }

    @Override
    public int getItemCount() {
        return orderProductExtraToppingGroupObject.size();
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class OrderProductExtraToppingGroupViewHolder extends RecyclerView.ViewHolder {
        TextView extraToppingGroupTextView;

        OrderProductExtraToppingGroupViewHolder(View itemView) {
            super(itemView);
            extraToppingGroupTextView = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.tv_extra_topping_group_name);
        }
    }
}
