package dk.eatmore.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;

import dk.eatmore.demo.R;
import dk.eatmore.demo.interfaces.UpdateProductCartQuantityCallback;
import dk.eatmore.demo.model.CartDetails;
import dk.eatmore.demo.model.MyCartCheckOutList;
import dk.eatmore.demo.model.OrderProductExtraToppingGroupObject;
import dk.eatmore.demo.model.OrderedProductAttributes;
import dk.eatmore.demo.model.RemovedIngredientsObject;
import dk.eatmore.demo.myutils.Utility;
import dk.eatmore.demo.netutils.DeleteClickInterface;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import dk.eatmore.demo.network.NetworkUtils;

import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sachi on 2/15/2017.
 */

public class ViewMyCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements UpdateProductCartQuantityCallback {
    private static final String TAG = ViewMyCartAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mInflater = null;
    private static final int EMPTY_VIEW = 10;
    private SimpleArcDialog pDialog;
    //   private LayoutInflater layoutInflater;
    private int mCounter = 1;
    ArrayList<MyCartCheckOutList> cartListItem;
    DeleteClickInterface deleteClickInterface;
    //  IncrementDecrement incrementDecrement;

    private ArrayList<CartDetails> mCartDetails = null;
    private RemovedIngredientsAdapter removedIngredientsAdapter = null;
    private OrderProductExtraToppingGroupAdapter orderProductExtraToppingGroupAdapter = null;
    private OrderedProductAttributesAdapter orderedProductAttributesAdapter = null;

    private boolean productDetailsVisible = false;
    private boolean haveExtraDetails = false;

//    private ArrayList<Boolean> productDetailsVisibleBoolean = null;
//    private boolean[] productDetailsVisibleBooleenArray;

    public ViewMyCartAdapter(Context mContext, ArrayList<CartDetails> mCartDetails, ArrayList<MyCartCheckOutList> mCartListItem,
                             DeleteClickInterface mdeleteClickInterface) {
        this.mContext = mContext;
        this.cartListItem = mCartListItem;
        this.deleteClickInterface = mdeleteClickInterface;
        //   this.incrementDecrement=mincrementDecrement;
        this.mCartDetails = mCartDetails;

//        productDetailsVisibleBoolean = new ArrayList<>();
//        int size = mCartDetails.size();
//        productDetailsVisibleBooleenArray = new boolean[size];
//
//        for (int i = 0; i < size; i++) {
//            productDetailsVisibleBoolean.add(false);
//            productDetailsVisibleBooleenArray[i] = false;
//        }
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

        v = LayoutInflater.from(parent.getContext()).inflate(dk.eatmore.demo.R.layout.view_cart_item, parent, false);
        CartDetailsViewHolder vh = null;
        vh = new CartDetailsViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CartDetailsViewHolder) {
            final CartDetailsViewHolder vh = (CartDetailsViewHolder) holder;

            final int itemPosition = vh.getAdapterPosition();

            haveExtraDetails = false;
            CartDetails mCartDetail = mCartDetails.get(itemPosition);
            MyCartCheckOutList myCartCheckOutList = cartListItem.get(itemPosition);
            String productImageUrl = myCartCheckOutList.getP_image();
//            Picasso.with(mContext)
//                    .load(myCartCheckOutList.getP_image())
//                    .resize(100, 115)
//                    .into(vh.prodImage);
            if (productImageUrl == null || productImageUrl.isEmpty() || productImageUrl.equalsIgnoreCase("null")) {
                vh.prodImage.setVisibility(View.GONE);
//            holder.imageLayout.setVisibility(View.GONE);
                //   holder.textitemDesc.setPadding(16,0,0,0);
//            holder.textprodId.setGravity(Gravity.CENTER | Gravity.LEFT);
                //  holder.textprodId.setPadding(20,0,0,0);
            } else {
                vh.prodImage.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(productImageUrl)
                        .into(vh.prodImage);
            }

            vh.cartItemName.setText(mCartDetail.getProduct_no() + " " + mCartDetail.getProduct_name());
            //vh.cartProductId.setText(myCartCheckOutList.getP_id());
//            String pPrice = myCartCheckOutList.getP_price();
//            vh.cartItemPrice.setText(Utility.convertCurrencyToDanish(Double.parseDouble(pPrice)) + " kr");
            String pPrice = myCartCheckOutList.getP_price();
            vh.cartItemPrice.setText(pPrice + " kr");

            mCounter = Integer.parseInt(myCartCheckOutList.getP_quantity());

            vh.itemCount.setText(myCartCheckOutList.getP_quantity());
            vh.itemCount.setTag(myCartCheckOutList.getOp_id());

            vh.cart_deleteImage.setClickable(true);
            vh.imgIncrement.setClickable(true);
            vh.imgDecrement.setClickable(true);

            Log.e("param", "op id" + myCartCheckOutList.getOp_id());

            // Product Details
            List<RemovedIngredientsObject> mRemovedIngredientsBeen
                    = mCartDetails.get(itemPosition).getRemoved_ingredients();
            List<OrderedProductAttributes> mOrderedProductAttributesBeen
                    = mCartDetails.get(itemPosition).getOrdered_product_attributes();
            List<OrderProductExtraToppingGroupObject> mOrderProductExtraToppingGroupBeen
                    = mCartDetails.get(itemPosition).getOrder_product_extra_topping_group();

            if (mRemovedIngredientsBeen != null) {
                int removedIngredientsSize = mRemovedIngredientsBeen.size();
                if (removedIngredientsSize > 0) {
                    haveExtraDetails = true;
                    removedIngredientsAdapter = new RemovedIngredientsAdapter(mContext);
                    removedIngredientsAdapter.setArrayList(mRemovedIngredientsBeen);

//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//                LinearLayoutManager mLayoutManager
//                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    StaggeredGridLayoutManager mLayoutManager =
//                            new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
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
                    ViewCompat.setLayoutDirection(vh.removedIngredientsRecyclerView, ViewCompat.LAYOUT_DIRECTION_LTR);
                    vh.removedIngredientsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    vh.removedIngredientsRecyclerView.setAdapter(removedIngredientsAdapter);
                } else {
                    vh.removedIngredientsRecyclerView.setVisibility(View.GONE);
                }
            } else {
                vh.removedIngredientsRecyclerView.setVisibility(View.GONE);
            }

            if (mOrderProductExtraToppingGroupBeen != null) {
                int orderProductExtraToppingGroupSize = mOrderProductExtraToppingGroupBeen.size();
                if (orderProductExtraToppingGroupSize > 0) {
                    haveExtraDetails = true;
                    orderProductExtraToppingGroupAdapter = new OrderProductExtraToppingGroupAdapter(mContext);
                    orderProductExtraToppingGroupAdapter.setArrayList(mOrderProductExtraToppingGroupBeen);

//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//                LinearLayoutManager mLayoutManager
//                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//                    StaggeredGridLayoutManager mLayoutManager =
//                            new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//                    vh.orderProductExtraToppingGroupRecyclerView.setLayoutManager(mLayoutManager);
                    vh.orderProductExtraToppingGroupRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
                    vh.orderProductExtraToppingGroupRecyclerView.setAdapter(orderProductExtraToppingGroupAdapter);
                } else {
                    vh.orderProductExtraToppingGroupRecyclerView.setVisibility(View.GONE);
                }
            } else {
                vh.orderProductExtraToppingGroupRecyclerView.setVisibility(View.GONE);
            }

            if (mOrderedProductAttributesBeen != null) {
                int orderedProductAttributesSize = mOrderedProductAttributesBeen.size();
                if (orderedProductAttributesSize > 0) {
                    haveExtraDetails = true;
                    orderedProductAttributesAdapter = new OrderedProductAttributesAdapter(mContext);
                    orderedProductAttributesAdapter.setArrayList(mOrderedProductAttributesBeen);

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

//                    ViewGroup viewGroup = (ViewGroup) v.getParent();
//                    ImageView showHideProductDetailsIV = (ImageView) v.findViewById(R.id.iv_show_hide_product_details);
//                    LinearLayout showHideProductDetailsLL = (LinearLayout) viewGroup.findViewById(R.id.ll_container_product_details);
//
//                    if (productDetailsVisible) {
//                        showHideProductDetailsIV.setImageResource(R.drawable.ic_plus);
//                        showHideProductDetailsLL.setVisibility(View.GONE);
//                    } else {
//                        showHideProductDetailsIV.setImageResource(R.drawable.ic_minus);
//                        showHideProductDetailsLL.setVisibility(View.VISIBLE);
//                    }
//                    productDetailsVisible = !productDetailsVisible;
                }
            });

//            vh.showProductDetailsLinearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (productDetailsVisibleBooleenArray[position]) {
//                        vh.showHideProductDetailsImageView.setImageResource(dk.eatmore.demo.R.drawable.ic_plus);
//                        vh.productDetailsContainerLinearLayout.setVisibility(View.GONE);
//                    } else {
//                        vh.showHideProductDetailsImageView.setImageResource(dk.eatmore.demo.R.drawable.ic_minus);
//                        vh.productDetailsContainerLinearLayout.setVisibility(View.VISIBLE);
//                    }
//                    productDetailsVisibleBooleenArray[position] = !productDetailsVisibleBooleenArray[position];
//
////                    ViewGroup viewGroup = (ViewGroup) v.getParent();
////                    ImageView showHideProductDetailsIV = (ImageView) v.findViewById(R.id.iv_show_hide_product_details);
////                    LinearLayout showHideProductDetailsLL = (LinearLayout) viewGroup.findViewById(R.id.ll_container_product_details);
////
////                    if (productDetailsVisible) {
////                        showHideProductDetailsIV.setImageResource(R.drawable.ic_plus);
////                        showHideProductDetailsLL.setVisibility(View.GONE);
////                    } else {
////                        showHideProductDetailsIV.setImageResource(R.drawable.ic_minus);
////                        showHideProductDetailsLL.setVisibility(View.VISIBLE);
////                    }
////                    productDetailsVisible = !productDetailsVisible;
//                }
//            });

            vh.imgIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = cartListItem.get(itemPosition).getOp_id();
//                    Double itemPrice = Double.parseDouble(cartListItem.get(itemPosition).getP_price());

                    ViewGroup viewGroup = (ViewGroup) view.getParent();
                    TextSwitcher txtLikeStatus = (TextSwitcher) viewGroup.findViewById(dk.eatmore.demo.R.id.itemCount);
                    Log.e("imgIncrement ", "opId" + id);

//                    cart_item_incremet(id, "1", txtLikeStatus, itemPrice, itemPosition);
                    cart_item_incremet(id, "1", txtLikeStatus, itemPosition);
                }
            });

            vh.imgDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String id = cartListItem.get(itemPosition).getOp_id();
//                    Double itemPrice = Double.parseDouble(cartListItem.get(itemPosition).getP_price());

                    ViewGroup viewGroup = (ViewGroup) view.getParent();

                    TextSwitcher txtLikeStatus = (TextSwitcher) viewGroup.findViewById(dk.eatmore.demo.R.id.itemCount);
                    TextView tv = (TextView) txtLikeStatus.getCurrentView();

                    Log.e("imgDecrement ", "opId" + id);

                    int pCounter = Integer.parseInt(tv.getText().toString().trim());

                    if (pCounter != 1) {
//                        cart_item_incremet(id, "0", txtLikeStatus, itemPrice, itemPosition);
                        cart_item_incremet(id, "0", txtLikeStatus, itemPosition);
                    }

                }
            });

            vh.cart_deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteClickInterface.cartDeleteClick(itemPosition);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cartListItem.size();
    }

    private void cart_item_incremet(final String op_id, final String qtyflag, final TextSwitcher itemCountText,
                                    /*final Double itemPrice,*/ final int itemPos) {
        pDialog = new ProgressDialaogView().getProgressDialog(mContext);
        pDialog.show();

        final String tag_string_req = "cart_item_incremet";

        NetworkUtils.callUpdateProductCartQuantity(op_id, qtyflag, itemPos, itemCountText,
                tag_string_req, ViewMyCartAdapter.this);
    }

    @Override
    public void updateProductCartQuantityCallbackSuccess(String success, int itemPos, TextSwitcher itemCountText) {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
        Log.e(TAG, "Success: " + success);
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");

            Intent intent = new Intent("CartCount");
            intent.putExtra("count", jObj.getString("cartcnt"));
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            // Check for error node in json
            if (status) {
                MyCartCheckOutList myCartCheckOutList = cartListItem.get(itemPos);
                String qty = jObj.getString("result");
                myCartCheckOutList.setP_quantity(qty);
                String productPrice = jObj.getString("p_price");

                String deviceLocale = Locale.getDefault().getLanguage();
//                if (deviceLocale.equalsIgnoreCase("en")) {
//                    productPrice = String.valueOf(Utility.formatValueToMoney(Double.parseDouble(productPrice)));
                    productPrice = String.valueOf(Utility.convertCurrencyToDanish(Double.parseDouble(productPrice)));
//                }
                myCartCheckOutList.setP_price(productPrice);
                String order_total = "0";
                if (jObj.has("order_total"))
                    order_total = jObj.getString("order_total");
                Intent totalPriceIntent = new Intent("totalPriceIntent");
                totalPriceIntent.putExtra("totalPrice", Utility.convertCurrencyToDanish(Double.parseDouble(order_total)));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(totalPriceIntent);

//                                String msg = jObj.getString("msg");
                itemCountText.setText("" + qty);
                Toast.makeText(mContext, dk.eatmore.demo.R.string.product_quantity_updated, Toast.LENGTH_SHORT).show();

                notifyDataSetChanged();
            }

        } catch (JSONException | NumberFormatException e) {
            // hideDialog();
            e.printStackTrace();
        }

    }

    @Override
    public void updateProductCartQuantityCallbackError(VolleyError volleyError) {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
        Log.e(TAG, "" + volleyError.toString());

        String msg = mContext.getString(R.string.try_again);
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class CartDetailsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView prodImage;
        ImageView imgDecrement;
        ImageView imgIncrement;
        ImageView cart_deleteImage;
        TextSwitcher itemCount;
        TextView cartItemName;
        TextView cartItemPrice;

        ImageView showHideProductDetailsImageView;
        LinearLayout showProductDetailsLinearLayout;
        LinearLayout productDetailsContainerLinearLayout;

        RecyclerView removedIngredientsRecyclerView;
        RecyclerView orderProductExtraToppingGroupRecyclerView;
        RecyclerView orderedProductAttributesRecyclerView;

        CartDetailsViewHolder(View itemView) {
            super(itemView);
//            cv = (CardView) itemView.findViewById(R.id.cv_my_orders);

            cart_deleteImage = (ImageView) itemView.findViewById(dk.eatmore.demo.R.id.cart_deleteImage);
            prodImage = (ImageView) itemView.findViewById(dk.eatmore.demo.R.id.cartItemImage);
            imgDecrement = (ImageView) itemView.findViewById(dk.eatmore.demo.R.id.imgDecrement);
            imgIncrement = (ImageView) itemView.findViewById(dk.eatmore.demo.R.id.imgIncrement);
            itemCount = (TextSwitcher) itemView.findViewById(dk.eatmore.demo.R.id.itemCount);

            //   cartProductId = (TextView) itemView.findViewById(R.id.cartProductId);
            cartItemName = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.cartItemName);
            cartItemPrice = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.cartItemPrice);

            showHideProductDetailsImageView = (ImageView) itemView.findViewById(dk.eatmore.demo.R.id.iv_show_hide_product_details);
            showProductDetailsLinearLayout = (LinearLayout) itemView.findViewById(dk.eatmore.demo.R.id.ll_product_details);
            productDetailsContainerLinearLayout = (LinearLayout) itemView.findViewById(dk.eatmore.demo.R.id.ll_container_product_details);

            removedIngredientsRecyclerView = (RecyclerView) itemView.findViewById(dk.eatmore.demo.R.id.rv_removed_ingredients);
            orderProductExtraToppingGroupRecyclerView = (RecyclerView) itemView.findViewById(dk.eatmore.demo.R.id.rv_extra_toppings);
            orderedProductAttributesRecyclerView = (RecyclerView) itemView.findViewById(dk.eatmore.demo.R.id.rv_attributes_with_extra_toppings);
        }
    }
}
