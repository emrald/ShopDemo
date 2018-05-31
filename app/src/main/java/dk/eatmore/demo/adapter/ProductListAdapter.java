package dk.eatmore.demo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dk.eatmore.demo.R;
import dk.eatmore.demo.fragments.ProductDescriptionFragment;
import dk.eatmore.demo.model.MenuItemPojo;
import dk.eatmore.demo.myutils.TouchImageView;
import dk.eatmore.demo.myutils.Utility;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by sachi on 2/6/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    public final static String PRODUCT_ID = "product_id";
    public final static String PRODUCT_NO = "product_no";
    private static final String TAG = ProductListAdapter.class.getSimpleName();
    private Context mContext;
    private List<MenuItemPojo> mItems;
    private LinearLayout linearlayout;
    private Fragment fragment;

    public ProductListAdapter(Context context, List<MenuItemPojo> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(mContext).inflate(R.layout.main_meu_row_new_layout,
//                parent, false);
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.product_list_row,
                parent, false);

        return new ProductListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductListAdapter.ViewHolder holder, final int position) {
        final int itemPosition = holder.getAdapterPosition();

        String productImageBaseUrl = mItems.get(itemPosition).getProductImageMainUrl();
        String productThumbnailUrl = mItems.get(itemPosition).getProductThumbnailMainUrl();
        String productImage = mItems.get(itemPosition).getPrductImage();
        String productImageUrl = productThumbnailUrl + productImage;

        if (productImage == null || productImage.isEmpty()) {
            holder.pizzaImage.setVisibility(View.GONE);
//            holder.imageLayout.setVisibility(View.GONE);
            //   holder.textitemDesc.setPadding(16,0,0,0);
//            holder.textprodId.setGravity(Gravity.CENTER | Gravity.LEFT);
            //  holder.textprodId.setPadding(20,0,0,0);
        } else {
            holder.pizzaImage.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(productImageUrl)
//                    .placeholder(R.drawable.loading_image_animation)
                    .into(holder.pizzaImage);
        }

        //    holder.textprodId.setText(mItems.get(itemPosition).getProdut_no());
        String productNumber = mItems.get(itemPosition).getProdut_no();
        String productName = mItems.get(itemPosition).getProductName();
        if (productNumber != null && productNumber.isEmpty())
            holder.prodName.setText(productName);
        else
            holder.prodName.setText(productNumber + " " + productName);

        String productPrice = mItems.get(itemPosition).getProductPrice();
        if (!productPrice.isEmpty()) {
//            productPrice = Utility.formatValueToMoney(Double.parseDouble(productPrice));
            productPrice = Utility.convertCurrencyToDanish(Double.parseDouble(productPrice));
            Log.e(TAG, productPrice);

            holder.prodPrice.setText(productPrice + " kr");
        } else
            holder.prodPrice.setText("");

        String productDescription = mItems.get(itemPosition).getProductDesc();
        if (productDescription != null && !productDescription.isEmpty()) {
            holder.textitemDesc.setVisibility(View.VISIBLE);
            holder.textitemDesc.setText(productDescription);
        } else
            holder.textitemDesc.setVisibility(View.GONE);

        String isAttributePresent = mItems.get(itemPosition).getIsAttribute();
        String isIngredientPresent = mItems.get(itemPosition).getProductIngredients();
        String extraToppingsPresent = mItems.get(itemPosition).getExtra_topping_group();

        if ((isAttributePresent != null && isAttributePresent.equalsIgnoreCase("0"))
                && (extraToppingsPresent == null || extraToppingsPresent.equalsIgnoreCase("")
                || extraToppingsPresent.equalsIgnoreCase("null"))) {
            holder.fromTextView.setVisibility(View.GONE);
        } else {
            holder.fromTextView.setVisibility(View.VISIBLE);
        }
//        if ((isAttributePresent != null && isAttributePresent.equalsIgnoreCase("1"))
//                && (isIngredientPresent != null && !isIngredientPresent.equals("false"))) {
//            holder.fromTextView.setVisibility(View.VISIBLE);
//        } else {
//            holder.fromTextView.setVisibility(View.GONE);
//        }

//        if (isIngredientPresent != null && isIngredientPresent.equals("false")) {
////            holder.fromTextView.setVisibility(View.GONE);
//            holder.image_cart_ectratoping.setImageResource(R.drawable.whitecart);
//        } else {
////            holder.fromTextView.setVisibility(View.VISIBLE);
//            holder.image_cart_ectratoping.setImageResource(R.drawable.ic_add);
//        }

        // Listeners
//        holder.pizzaImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Dialog dialog = new Dialog(mContext);
//                dialog.setContentView(R.layout.image_dilog);
//                ImageView img = (ImageView) dialog.findViewById(R.id.dialogimg);
//                ImageView dialogcross = (ImageView) dialog.findViewById(R.id.dialogcross);
//
//                dialogcross.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//                Picasso.with(mContext)
//                        .load(mItems.get(itemPosition).getProductImageMainUrl() + mItems.get(itemPosition).getPrductImage()) // resizes the image to these dimensions (in pixel)
//                        .into(img);
//                dialog.show();
//            }
//        });

        holder.pizzaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView mbackImageView;
//                ImageView mZoomImageView;
                final TouchImageView mZoomImageView;
                Button okButton;

                final Dialog zoomDialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                zoomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xD9000000));
                zoomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                zoomDialog.setCancelable(true);
                zoomDialog.setContentView(R.layout.fullscreen_image);

                mbackImageView = (ImageView) zoomDialog.findViewById(R.id.iv_back_fs);
                okButton = (Button) zoomDialog.findViewById(R.id.btn_close_image);
//                mZoomImageView = (ImageView) zoomDialog.findViewById(R.id.iv_product_image);
                mZoomImageView = (TouchImageView) zoomDialog.findViewById(R.id.iv_product_image);
//                Picasso.with(mContext)
//                        .load(mItems.get(itemPosition).getProductImageMainUrl() + mItems.get(itemPosition).getPrductImage())
////                        .error(R.drawable.no_image_1)
//                        .into(mZoomImageView);
//                mZoomImageView.setMaxZoom(4f);

                Picasso.with(mContext)
                        .load(mItems.get(itemPosition).getProductImageMainUrl() + mItems.get(itemPosition).getPrductImage())
//                        .placeholder(R.drawable.loading_image_animation)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap,
                                                       final Picasso.LoadedFrom loadedFrom) {
                                mZoomImageView.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(final Drawable drawable) {
                                Log.d(TAG, "Failed");
                            }

                            @Override
                            public void onPrepareLoad(final Drawable drawable) {
                                mZoomImageView.setImageDrawable(drawable);
                            }
                        });

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zoomDialog.dismiss();
                    }
                });

                mbackImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zoomDialog.dismiss();
                    }
                });
                zoomDialog.show();
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ProductDescriptionFragment();
                Bundle bundle = new Bundle();
                bundle.putString(PRODUCT_ID, mItems.get(itemPosition).getProdutId());

                if ((mItems.get(itemPosition).getIsAttribute() != null && mItems.get(itemPosition).getIsAttribute().equalsIgnoreCase("0"))
                        && (mItems.get(itemPosition).getExtra_topping_group() == null || mItems.get(itemPosition).getExtra_topping_group().equalsIgnoreCase("")
                        || mItems.get(itemPosition).getExtra_topping_group().equalsIgnoreCase("null"))
                        && mItems.get(itemPosition).getProductIngredients().equals("false")) {
                    bundle.putBoolean("productHaveAttribute", true);
                } else {
                    bundle.putBoolean("productHaveAttribute", false);
                }

//                if (mItems.get(itemPosition).getProductIngredients() != null
//                        && mItems.get(itemPosition).getProductIngredients().equals("false")) {
//                    bundle.putBoolean("productHaveAttribute", true);
//                } else {
//                    bundle.putBoolean("productHaveAttribute", false);
//                }

//                if (mItems.get(itemPosition).getProductIngredients().equals("false"))
//                    bundle.putBoolean("productHaveAttribute", true);
//                else
//                    bundle.putBoolean("productHaveAttribute", false);

                bundle.putString("pPrice", mItems.get(itemPosition).getProductPrice());

                fragment.setArguments(bundle);

                // title="Explore The MindSets";
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransactionParticularTrans = fragmentManager.beginTransaction();
                fragmentTransactionParticularTrans.add(R.id.fragment_container, fragment);
                fragmentTransactionParticularTrans.addToBackStack(ProductListAdapter.class.getName());
                fragmentTransactionParticularTrans.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textitemDesc;
        private TextView prodName;
        private TextView prodPrice;
        private TextView fromTextView;

        private ImageView pizzaImage;
        private ImageView image_cart_ectratoping;
        View view;
//        LinearLayout imageLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textitemDesc = (TextView) itemView.findViewById(R.id.prodItemDesc);
            // textprodId = (TextView)itemView.findViewById(R.id.prodId);
            prodName = (TextView) itemView.findViewById(R.id.prodName);
            prodPrice = (TextView) itemView.findViewById(R.id.prodPrice);
            fromTextView = (TextView) itemView.findViewById(R.id.tv_from);

            pizzaImage = (ImageView) itemView.findViewById(R.id.pizzaImage);
            image_cart_ectratoping = (ImageView) itemView.findViewById(R.id.image_cart_ectratoping);
//            imagell = (ImageView) itemView.findViewById(R.id.pizzaImage);
//            imageLayout = (LinearLayout) itemView.findViewById(R.id.imageLayout);
            this.view = itemView;
        }
    }
}