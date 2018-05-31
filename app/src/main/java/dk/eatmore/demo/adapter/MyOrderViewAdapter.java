package dk.eatmore.demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import dk.eatmore.demo.model.MyCartCheckOutList;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by param on 16-May-16.
 */
public class MyOrderViewAdapter extends BaseAdapter {
    SimpleArcDialog pDialog;
    private Context mContext;
    //   private LayoutInflater layoutInflater;
    private int mCounter = 1;
    ArrayList<MyCartCheckOutList> cartListItem;
    //  IncrementDecrement incrementDecrement;

    public MyOrderViewAdapter(Context mContext, ArrayList<MyCartCheckOutList> mCartListItem
    ) {
        this.mContext = mContext;
        this.cartListItem = mCartListItem;
        //   this.incrementDecrement=mincrementDecrement;
    }

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
        return 0;
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

            rowView = LayoutInflater.from(mContext).inflate(dk.eatmore.demo.R.layout.mycart_fragment, parent, false);

            holder.cart_deleteImage = (ImageView) rowView.findViewById(dk.eatmore.demo.R.id.cart_deleteImage);
            holder.prodImage = (ImageView) rowView.findViewById(dk.eatmore.demo.R.id.careItemImage);
            holder.imgDecrement = (ImageView) rowView.findViewById(dk.eatmore.demo.R.id.imgDecrement);
            holder.imgIncrement = (ImageView) rowView.findViewById(dk.eatmore.demo.R.id.imgIncrement);
            holder.itemCount = (TextSwitcher) rowView.findViewById(dk.eatmore.demo.R.id.itemCount);


            holder.imgDecrement.setVisibility(View.INVISIBLE);
            holder.imgIncrement.setVisibility(View.INVISIBLE);
            holder.cart_deleteImage.setVisibility(View.INVISIBLE);

            holder.cartProductId = (TextView) rowView.findViewById(dk.eatmore.demo.R.id.cartProductId);
            holder.cartItemName = (TextView) rowView.findViewById(dk.eatmore.demo.R.id.cartItemName);
            holder.cartItemPrice = (TextView) rowView.findViewById(dk.eatmore.demo.R.id.cartItemPrice);


            // store the holder with the view.
            rowView.setTag(holder);

        } else

            holder = (Holder) rowView.getTag();


        //clickedPosition = position;
        MyCartCheckOutList myCartCheckOutList = cartListItem.get(position);


        holder.cartItemName.setText(myCartCheckOutList.getP_name());
        holder.cartProductId.setText(myCartCheckOutList.getP_id());
        holder.cartItemPrice.setText("Price kr:" + myCartCheckOutList.getP_price());

        Picasso.with(mContext).load(myCartCheckOutList.getP_image())
                .resize(100, 115).into(holder.prodImage);


        holder.itemCount.setText(myCartCheckOutList.getP_quantity());


        Log.e("param", "op id" + myCartCheckOutList.getOp_id());

//        qtyflag =
//        1 - Increment
//        0 - Decrement


        return rowView;
    }

    public class Holder {
        ImageView prodImage, imgDecrement, imgIncrement, cart_deleteImage;
        TextSwitcher itemCount;
        TextView cartProductId, cartItemName, cartItemPrice;
    }


}