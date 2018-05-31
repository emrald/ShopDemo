package dk.eatmore.demo.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;

import dk.eatmore.demo.R;
import dk.eatmore.demo.interfaces.RecyclerViewOnClickListener;
import dk.eatmore.demo.model.OrderProductExtraToppingGroupObject;
import dk.eatmore.demo.model.OrderProductsDetails;
import dk.eatmore.demo.model.OrderedProductAttributes;
import dk.eatmore.demo.model.RemovedIngredientsObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static dk.eatmore.demo.myutils.Utility.convertCurrencyToDanish;

/**
 * Created by sachi on 2/8/2017.
 */
public class OrderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private static final int EMPTY_VIEW = 10;

    private RecyclerViewOnClickListener onAdapterItemClickListener;
    private ArrayList<OrderProductsDetails> myOrderDetailsList = new ArrayList<>();
    private RemovedIngredientsAdapter removedIngredientsAdapter = null;
    private OrderProductExtraToppingGroupAdapter orderProductExtraToppingGroupAdapter = null;
    private OrderedProductAttributesAdapter orderedProductAttributesAdapter = null;

    private boolean productDetailsVisible = false;
    private boolean haveExtraDetails = false;

    private DecimalFormat df = new DecimalFormat("#.##");

    public OrderDetailsAdapter(Context mContext, RecyclerViewOnClickListener listener) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.onAdapterItemClickListener = listener;
    }

    public void setArrayList(ArrayList<OrderProductsDetails> arrayList) {
        this.myOrderDetailsList = arrayList;
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

        v = LayoutInflater.from(parent.getContext()).inflate(dk.eatmore.demo.R.layout.my_order_details_item, parent, false);
        OrderDetailsViewHolder vh = null;
        vh = new OrderDetailsViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderDetailsViewHolder) {
            final OrderDetailsViewHolder vh = (OrderDetailsViewHolder) holder;
            haveExtraDetails = false;
            final int itemPosition = vh.getAdapterPosition();

            OrderProductsDetails orderProductDetails = myOrderDetailsList.get(itemPosition);
            String productNo = orderProductDetails.getProducts().getProduct_no();
            String productName = "";
            if (productNo == null || productNo.isEmpty() || productNo.equalsIgnoreCase("null"))
                productName = orderProductDetails.getProducts().getP_name();
            else
                productName = productNo + " " + orderProductDetails.getProducts().getP_name();

            String qty = orderProductDetails.getQuantity();
            String total = orderProductDetails.getP_price();
            String pricePerQty = convertCurrencyToDanish(Double.parseDouble(df.format(Double.parseDouble(total) / Double.parseDouble(qty)))) + mContext.getString(R.string.dkk_per_qty);
            List<RemovedIngredientsObject> removedIngredientsObjects = orderProductDetails.getRemoved_ingredients();
            List<OrderProductExtraToppingGroupObject> mOrderProductExtraToppingGroupObjects
                    = orderProductDetails.getOrder_product_extra_topping_group();
            List<OrderedProductAttributes> mOrderedProductAttributes
                    = orderProductDetails.getOrdered_product_attributes();

            vh.productNameTextView.setText(productName);
            vh.productQtyTextView.setText(qty);
            vh.productTotalPriceTextView.setText(convertCurrencyToDanish(Double.parseDouble(total)));
            vh.productPricePerQtyTextView.setText(pricePerQty);

            if (removedIngredientsObjects != null) {
                int removedIngredientsSize = removedIngredientsObjects.size();
                if (removedIngredientsSize > 0) {
                    haveExtraDetails = true;
                    removedIngredientsAdapter = new RemovedIngredientsAdapter(mContext);
                    removedIngredientsAdapter.setArrayList(removedIngredientsObjects);

//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//                LinearLayoutManager mLayoutManager
//                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    StaggeredGridLayoutManager mLayoutManager =
                            new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//                    vh.removedIngredientsRecyclerView.setLayoutManager(mLayoutManager);
//                    vh.removedIngredientsRecyclerView.setLayoutManager(new FlowLayoutManager());
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
                    vh.removedIngredientsRecyclerView.setLayoutManager(chipsLayoutManager);
                    ViewCompat.setLayoutDirection(vh.orderProductExtraToppingGroupRecyclerView, ViewCompat.LAYOUT_DIRECTION_LTR);
                    vh.removedIngredientsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    vh.removedIngredientsRecyclerView.setAdapter(removedIngredientsAdapter);
                } else {
                    vh.removedIngredientsRecyclerView.setVisibility(View.GONE);
                }
            } else {
                vh.removedIngredientsRecyclerView.setVisibility(View.GONE);

            }

            if (mOrderProductExtraToppingGroupObjects != null) {
                int orderProductExtraToppingGroupSize = mOrderProductExtraToppingGroupObjects.size();
                if (orderProductExtraToppingGroupSize > 0) {
                    haveExtraDetails = true;
                    orderProductExtraToppingGroupAdapter = new OrderProductExtraToppingGroupAdapter(mContext);
                    orderProductExtraToppingGroupAdapter.setArrayList(mOrderProductExtraToppingGroupObjects);

//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//                LinearLayoutManager mLayoutManager
//                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    StaggeredGridLayoutManager mLayoutManager =
//                            new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//                    vh.orderProductExtraToppingGroupRecyclerView.setLayoutManager(mLayoutManager);
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
                    vh.orderProductExtraToppingGroupRecyclerView.setLayoutManager(chipsLayoutManager);
                    ViewCompat.setLayoutDirection(vh.orderProductExtraToppingGroupRecyclerView, ViewCompat.LAYOUT_DIRECTION_LTR);
                    vh.orderProductExtraToppingGroupRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    vh.orderProductExtraToppingGroupRecyclerView.setAdapter(orderProductExtraToppingGroupAdapter);
                } else {
                    vh.orderProductExtraToppingGroupRecyclerView.setVisibility(View.GONE);
                }
            } else {
                vh.orderProductExtraToppingGroupRecyclerView.setVisibility(View.GONE);
            }

            if (mOrderedProductAttributes != null) {
                int orderedProductAttributesSize = mOrderedProductAttributes.size();
                if (orderedProductAttributesSize > 0) {
                    haveExtraDetails = true;
                    orderedProductAttributesAdapter = new OrderedProductAttributesAdapter(mContext);
                    orderedProductAttributesAdapter.setArrayList(mOrderedProductAttributes);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//                LinearLayoutManager mLayoutManager
//                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    StaggeredGridLayoutManager mLayoutManager =
//                            new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                    vh.orderedProductAttributesRecyclerView.setLayoutManager(mLayoutManager);
                    vh.orderedProductAttributesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    vh.orderedProductAttributesRecyclerView.setAdapter(orderedProductAttributesAdapter);
                } else {
                    vh.orderedProductAttributesRecyclerView.setVisibility(View.GONE);
                }
            } else {
                vh.orderedProductAttributesRecyclerView.setVisibility(View.GONE);
            }

            if (haveExtraDetails) {
                vh.showProductDetailsLinearLayout.setVisibility(View.VISIBLE);
            } else {
                vh.showProductDetailsLinearLayout.setVisibility(View.GONE);
            }

            vh.showProductDetailsLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productDetailsVisible) {
                        vh.showHideProductDetailsImageView.setImageResource(dk.eatmore.demo.R.drawable.ic_plus);
                        vh.productDetailsContainerLinearLayout.setVisibility(View.GONE);
                    } else {
                        vh.showHideProductDetailsImageView.setImageResource(dk.eatmore.demo.R.drawable.ic_minus);
                        vh.productDetailsContainerLinearLayout.setVisibility(View.VISIBLE);
                    }
                    productDetailsVisible = !productDetailsVisible;
                }
            });
//            vh.orderDetailsButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onAdapterItemClickListener.buttonOnClick(v, itemPosition);
//                }
//            });
        } else {
//            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return myOrderDetailsList.size();
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView productNameTextView;
        TextView productQtyTextView;
        TextView productTotalPriceTextView;
        TextView productPricePerQtyTextView;

        ImageView showHideProductDetailsImageView;
        LinearLayout showProductDetailsLinearLayout;
        LinearLayout productDetailsContainerLinearLayout;

        RecyclerView removedIngredientsRecyclerView;
        RecyclerView orderProductExtraToppingGroupRecyclerView;
        RecyclerView orderedProductAttributesRecyclerView;

        OrderDetailsViewHolder(View itemView) {
            super(itemView);
//            cv = (CardView) itemView.findViewById(R.id.cv_my_orders);
            productNameTextView = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.tv_product_name);
            productQtyTextView = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.tv_qty);
            productTotalPriceTextView = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.tv_product_total_price);
            productPricePerQtyTextView = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.tv_product_price_per_qty);

            showHideProductDetailsImageView = (ImageView) itemView.findViewById(dk.eatmore.demo.R.id.iv_show_hide_product_details);
            showProductDetailsLinearLayout = (LinearLayout) itemView.findViewById(dk.eatmore.demo.R.id.ll_product_details);
            productDetailsContainerLinearLayout = (LinearLayout) itemView.findViewById(dk.eatmore.demo.R.id.ll_container_product_details);

            removedIngredientsRecyclerView = (RecyclerView) itemView.findViewById(dk.eatmore.demo.R.id.rv_removed_ingredients);
            orderProductExtraToppingGroupRecyclerView = (RecyclerView) itemView.findViewById(dk.eatmore.demo.R.id.rv_extra_toppings);
            orderedProductAttributesRecyclerView = (RecyclerView) itemView.findViewById(dk.eatmore.demo.R.id.rv_attributes_with_extra_toppings);
        }
    }
}
