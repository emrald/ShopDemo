package dk.eatmore.demo.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import dk.eatmore.demo.R;
import dk.eatmore.demo.model.OrderProductExtraToppingGroupObject;
import dk.eatmore.demo.model.OrderedProductAttributes;

import java.util.List;

/**
 * Created by sachi on 2/8/2017.
 */
public class OrderedProductAttributesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private static final int EMPTY_VIEW = 10;

    private List<OrderedProductAttributes> orderedProductAttributes;
    private OrderProductExtraToppingGroupAdapter orderProductExtraToppingGroupAdapter = null;

    public OrderedProductAttributesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setArrayList(List<OrderedProductAttributes> orderedProductAttributes) {
        this.orderedProductAttributes = orderedProductAttributes;
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

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_product_attributes_item, parent, false);
        OrderedProductAttributesViewHolder vh = null;
        vh = new OrderedProductAttributesViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderedProductAttributesViewHolder) {
            OrderedProductAttributesViewHolder vh = (OrderedProductAttributesViewHolder) holder;

            final int itemPosition = vh.getAdapterPosition();
            OrderedProductAttributes mOrderedProductAttributes = orderedProductAttributes.get(itemPosition);
//            String ingredientName = mOrderedProductAttributes
//                    .getOrder_product_extra_topping_group().get(itemPosition).getIngredient_name();
            String productAttribute = "";
            String valueName = mOrderedProductAttributes.getAttribute_value_name();
            String attributeName = mOrderedProductAttributes.getAttribute_name();
            String price = mOrderedProductAttributes.getA_price();
            if (valueName != null && !valueName.equalsIgnoreCase("null")
                    && attributeName != null && !attributeName.equalsIgnoreCase("null")
                    && price != null && !price.equalsIgnoreCase("null")) {
                productAttribute = valueName + " - " + attributeName + " " + price;
            } else if (attributeName != null && !attributeName.equalsIgnoreCase("null")
                    && price != null && !price.equalsIgnoreCase("null")) {
                productAttribute = attributeName + " " + price;
            } else if (valueName != null && !valueName.equalsIgnoreCase("null")
                    && price != null && !price.equalsIgnoreCase("null")) {
                productAttribute = valueName + " - " + price;
            } else if (valueName != null && !valueName.equalsIgnoreCase("null")
                    && attributeName != null && !attributeName.equalsIgnoreCase("null")) {
                productAttribute = valueName + " - " + attributeName;
            }

            List<OrderProductExtraToppingGroupObject> mOrderProductExtraToppingGroupObjects
                    = mOrderedProductAttributes.getOrder_product_extra_topping_group();

            if (mOrderProductExtraToppingGroupObjects != null) {
                int orderProductExtraToppingGroupSize = mOrderProductExtraToppingGroupObjects.size();
                if (orderProductExtraToppingGroupSize > 0) {
                    orderProductExtraToppingGroupAdapter = new OrderProductExtraToppingGroupAdapter(mContext);
                    orderProductExtraToppingGroupAdapter.setArrayList(mOrderProductExtraToppingGroupObjects);

//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//                LinearLayoutManager mLayoutManager
//                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    StaggeredGridLayoutManager mLayoutManager =
//                            new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//                    vh.extraToppingsRecyclerView.setLayoutManager(mLayoutManager);
                    ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                            //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                            .setChildGravity(Gravity.TOP)
                            //whether RecyclerView can scroll. TRUE by default
                            .setScrollingEnabled(true)
                            //set maximum views count in a particular row
//                            .setMaxViewsInRow(2)
                            //set gravity resolver where you can determine gravity for item in position.
                            //This method have priority over previous one
                            .setGravityResolver(new IChildGravityResolver() {
                                @Override
                                public int getItemGravity(int position) {
                                    return Gravity.CENTER;
                                }
                            })
                            //you are able to break row due to your conditions. Row breaker should return true for that views
//                            .setRowBreaker(new IRowBreaker() {
//                                @Override
//                                public boolean isItemBreakRow(@IntRange(from = 0) int position) {
//                                    return position == 6 || position == 11 || position == 2;
//                                }
//                            })
                            //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                            .setOrientation(ChipsLayoutManager.HORIZONTAL)
                            // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                            //STRATEGY_FILL_SPACE or STRATEGY_CENTER or STRATEGY_CENTER_DENSE
                            .setRowStrategy(ChipsLayoutManager.STRATEGY_FILL_SPACE)
                            // whether strategy is applied to last row. FALSE by default
                            .withLastRow(true)
                            .build();
                    vh.extraToppingsRecyclerView.setLayoutManager(chipsLayoutManager);
                    ViewCompat.setLayoutDirection(vh.extraToppingsRecyclerView, ViewCompat.LAYOUT_DIRECTION_LTR);
                    vh.extraToppingsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    vh.extraToppingsRecyclerView.setAdapter(orderProductExtraToppingGroupAdapter);
                } else {
                    vh.separatorProductAttribute.setVisibility(View.GONE);
                    vh.extraToppingsRecyclerView.setVisibility(View.GONE);
                }
            } else {
                vh.separatorProductAttribute.setVisibility(View.GONE);
                vh.extraToppingsRecyclerView.setVisibility(View.GONE);
            }
            vh.productAttributeTextView.setText(productAttribute);
        }
    }

    @Override
    public int getItemCount() {
        return orderedProductAttributes.size();
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class OrderedProductAttributesViewHolder extends RecyclerView.ViewHolder {
        TextView productAttributeTextView;
        View separatorProductAttribute;
        RecyclerView extraToppingsRecyclerView;

        OrderedProductAttributesViewHolder(View itemView) {
            super(itemView);
            productAttributeTextView = (TextView) itemView.findViewById(R.id.tv_product_attribute);
            separatorProductAttribute = (View) itemView.findViewById(R.id.separator_product_attribute);
            extraToppingsRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_extra_toppings_inside);
        }
    }
}
