package dk.eatmore.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import dk.eatmore.demo.R;
import dk.eatmore.demo.interfaces.UpdateProductCartQuantityCallback;
import dk.eatmore.demo.model.CartDetails;
import dk.eatmore.demo.model.MyCartCheckOutList;
import dk.eatmore.demo.model.OrderProductExtraToppingGroupObject;
import dk.eatmore.demo.model.OrderedProductAttributes;
import dk.eatmore.demo.model.RemovedIngredientsObject;
import dk.eatmore.demo.netutils.DeleteClickInterface;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import dk.eatmore.demo.network.NetworkUtils;
import com.leo.simplearcloader.SimpleArcDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by param on 16-May-16.
 */
public class MyCartCheckOutFragmentAdapter extends BaseAdapter implements UpdateProductCartQuantityCallback {
    private static final String TAG = MyCartCheckOutFragmentAdapter.class.getSimpleName();
    private Context mContext;
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

    public MyCartCheckOutFragmentAdapter(Context mContext, ArrayList<CartDetails> mCartDetails, ArrayList<MyCartCheckOutList> mCartListItem,
                                         DeleteClickInterface mdeleteClickInterface) {
        this.mContext = mContext;
        this.cartListItem = mCartListItem;
        this.deleteClickInterface = mdeleteClickInterface;
        //   this.incrementDecrement=mincrementDecrement;
        this.mCartDetails = mCartDetails;
    }

//    public MyCartCheckOutFragmentAdapter(Context mContext, ArrayList<CartDetails> mCartDetails,
//                                         DeleteClickInterface mdeleteClickInterface) {
//        this.mContext = mContext;
//        this.mCartDetails = mCartDetails;
//        this.deleteClickInterface = mdeleteClickInterface;
//        //   this.incrementDecrement=mincrementDecrement;
//    }

    @Override
    public int getCount() {
        return cartListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    Holder holder;
    //  int clickedPosition;
    // View rowView;
    int count;

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        //  final Holder holder=new Holder();
        holder = new Holder();
        //  rowView= convertView;
        if (rowView == null) {

//            rowView = LayoutInflater.from(mContext).inflate(R.layout.mycart_fragment, parent, false);
            rowView = LayoutInflater.from(mContext).inflate(R.layout.view_cart_item, parent, false);

            holder.cart_deleteImage = (ImageView) rowView.findViewById(R.id.cart_deleteImage);
            holder.prodImage = (ImageView) rowView.findViewById(R.id.careItemImage);
            holder.imgDecrement = (ImageView) rowView.findViewById(R.id.imgDecrement);
            holder.imgIncrement = (ImageView) rowView.findViewById(R.id.imgIncrement);
            holder.itemCount = (TextSwitcher) rowView.findViewById(R.id.itemCount);

            //   holder.cartProductId = (TextView) rowView.findViewById(R.id.cartProductId);
            holder.cartItemName = (TextView) rowView.findViewById(R.id.cartItemName);
            holder.cartItemPrice = (TextView) rowView.findViewById(R.id.cartItemPrice);

            holder.showHideProductDetailsImageView = (ImageView) rowView.findViewById(R.id.iv_show_hide_product_details);
            holder.showProductDetailsLinearLayout = (LinearLayout) rowView.findViewById(R.id.ll_product_details);
            holder.productDetailsContainerLinearLayout = (LinearLayout) rowView.findViewById(R.id.ll_container_product_details);

            holder.removedIngredientsRecyclerView = (RecyclerView) rowView.findViewById(R.id.rv_removed_ingredients);
            holder.orderProductExtraToppingGroupRecyclerView = (RecyclerView) rowView.findViewById(R.id.rv_extra_toppings);
            holder.orderedProductAttributesRecyclerView = (RecyclerView) rowView.findViewById(R.id.rv_attributes_with_extra_toppings);


            // store the holder with the view.
            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }
        holder.imgDecrement.setTag(position);
        holder.imgIncrement.setTag(position);
        holder.cartItemPrice.setTag(position);

        holder.cart_deleteImage.setTag(position);
        //clickedPosition = position;
        MyCartCheckOutList myCartCheckOutList = cartListItem.get(position);


        holder.cartItemName.setText(myCartCheckOutList.getP_id() + " " + myCartCheckOutList.getP_name());
        //holder.cartProductId.setText(myCartCheckOutList.getP_id());
        holder.cartItemPrice.setText("Price kr: " + myCartCheckOutList.getP_price());
//
//        Picasso.with(mContext).load(myCartCheckOutList.getP_image())
//                .resize(100, 115).into(holder.prodImage);

        mCounter = Integer.parseInt(myCartCheckOutList.getP_quantity());

        holder.itemCount.setText(myCartCheckOutList.getP_quantity());

        holder.itemCount.setTag(myCartCheckOutList.getOp_id());

        holder.cart_deleteImage.setClickable(true);
        holder.imgIncrement.setClickable(true);
        holder.imgDecrement.setClickable(true);

        Log.e("param", "op id" + myCartCheckOutList.getOp_id());

        // Product Details
        List<RemovedIngredientsObject> mRemovedIngredientsBeen
                = mCartDetails.get(position).getRemoved_ingredients();
        List<OrderedProductAttributes> mOrderedProductAttributesBeen
                = mCartDetails.get(position).getOrdered_product_attributes();
        List<OrderProductExtraToppingGroupObject> mOrderProductExtraToppingGroupBeen
                = mCartDetails.get(position).getOrder_product_extra_topping_group();

        if (mRemovedIngredientsBeen != null) {
            int removedIngredientsSize = mRemovedIngredientsBeen.size();
            if (removedIngredientsSize > 0) {
                haveExtraDetails = true;
                removedIngredientsAdapter = new RemovedIngredientsAdapter(mContext);
                removedIngredientsAdapter.setArrayList(mRemovedIngredientsBeen);

//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//                LinearLayoutManager mLayoutManager
//                        = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                StaggeredGridLayoutManager mLayoutManager =
                        new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                holder.removedIngredientsRecyclerView.setLayoutManager(mLayoutManager);
                holder.removedIngredientsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                holder.removedIngredientsRecyclerView.setAdapter(removedIngredientsAdapter);
            } else {
                holder.removedIngredientsRecyclerView.setVisibility(View.GONE);
            }
        } else {
            holder.removedIngredientsRecyclerView.setVisibility(View.GONE);

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
                StaggeredGridLayoutManager mLayoutManager =
                        new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                holder.orderProductExtraToppingGroupRecyclerView.setLayoutManager(mLayoutManager);
                holder.orderProductExtraToppingGroupRecyclerView.setItemAnimator(new DefaultItemAnimator());
                holder.orderProductExtraToppingGroupRecyclerView.setAdapter(orderProductExtraToppingGroupAdapter);
            } else {
                holder.orderProductExtraToppingGroupRecyclerView.setVisibility(View.GONE);
            }
        } else {
            holder.orderProductExtraToppingGroupRecyclerView.setVisibility(View.GONE);
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
                holder.orderedProductAttributesRecyclerView.setLayoutManager(mLayoutManager);
                holder.orderedProductAttributesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                holder.orderedProductAttributesRecyclerView.setAdapter(orderedProductAttributesAdapter);
            } else {
                holder.orderedProductAttributesRecyclerView.setVisibility(View.GONE);
            }
        } else {
            holder.orderedProductAttributesRecyclerView.setVisibility(View.GONE);
        }

        if (haveExtraDetails) {
            holder.showProductDetailsLinearLayout.setVisibility(View.VISIBLE);
        } else {
            holder.showProductDetailsLinearLayout.setVisibility(View.GONE);
        }

        holder.showProductDetailsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = (ViewGroup) v.getParent();
                ImageView showHideProductDetailsIV = (ImageView) v.findViewById(R.id.iv_show_hide_product_details);
                LinearLayout showHideProductDetailsLL = (LinearLayout) viewGroup.findViewById(R.id.ll_container_product_details);

                if (productDetailsVisible) {
                    showHideProductDetailsIV.setImageResource(R.drawable.ic_plus);
                    showHideProductDetailsLL.setVisibility(View.GONE);
                } else {
                    showHideProductDetailsIV.setImageResource(R.drawable.ic_minus);
                    showHideProductDetailsLL.setVisibility(View.VISIBLE);
                }
                productDetailsVisible = !productDetailsVisible;
            }
        });

//        qtyflag =
//        1 - Increment
//        0 - Decrement

        holder.imgIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                String id = cartListItem.get(position).getOp_id();
                Double itemPrice = Double.parseDouble(cartListItem.get(position).getP_price());


                ViewGroup viewGroup = (ViewGroup) view.getParent();
                TextSwitcher txtLikeStatus = (TextSwitcher) viewGroup.findViewById(R.id.itemCount);


                Log.e("imgIncrement ", "opId" + id);

                cart_item_incremet(id, "1", txtLikeStatus, itemPrice, position);
            }
        });

        holder.imgDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = (Integer) view.getTag();
                String id = cartListItem.get(position).getOp_id();
                Double itemPrice = Double.parseDouble(cartListItem.get(position).getP_price());

                ViewGroup viewGroup = (ViewGroup) view.getParent();

                TextSwitcher txtLikeStatus = (TextSwitcher) viewGroup.findViewById(R.id.itemCount);
                TextView tv = (TextView) txtLikeStatus.getCurrentView();

                Log.e("imgDecrement ", "opId" + id);

                int pCounter = Integer.parseInt(tv.getText().toString().trim());

                if (pCounter != 1) {
                    cart_item_incremet(id, "0", txtLikeStatus, itemPrice, position);
                }

            }
        });

        holder.cart_deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClickInterface.cartDeleteClick((Integer) v.getTag());
            }
        });

        return rowView;
    }

    public class Holder {
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

    }

    private void cart_item_incremet(final String op_id, final String qtyflag,
                                    final TextSwitcher itemCountText
            , final Double itemPrice, final int itemPos) {

        pDialog = new ProgressDialaogView().getProgressDialog(mContext);
        pDialog.show();

        final String tag_string_req = "cart_item_incremet";

        NetworkUtils.callUpdateProductCartQuantity(op_id, qtyflag, itemPos, itemCountText, tag_string_req, MyCartCheckOutFragmentAdapter.this);
    }

    @Override
    public void updateProductCartQuantityCallbackSuccess(String success, int itemPos, TextSwitcher itemCountText) {
        pDialog.dismiss();
        Log.e(TAG, success);
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
                myCartCheckOutList.setP_price(jObj.getString("p_price"));

                Intent totalPriceIntent = new Intent("totalPriceIntent");
                totalPriceIntent.putExtra("totalPrice", jObj.getString("order_total"));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(totalPriceIntent);


                itemCountText.setText("" + qty);
                Toast.makeText(mContext, "Product quantity is updated Successfully", Toast.LENGTH_SHORT).show();

                notifyDataSetChanged();
            }

        } catch (JSONException | NumberFormatException e) {
            // hideDialog();
            e.printStackTrace();
        }

    }

    @Override
    public void updateProductCartQuantityCallbackError(VolleyError volleyError) {
        pDialog.dismiss();
        Log.e(TAG, "" + volleyError.toString());

    }

}