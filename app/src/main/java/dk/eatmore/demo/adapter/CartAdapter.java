package dk.eatmore.demo.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dk.eatmore.demo.model.MyCartCheckOutList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.View_Holder> {

    // List<PaymentMethodPojo> list = Collections.emptyList();
    Context context;
    Fragment fragment = null;
    // private FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private Context mContext;
    ArrayList<MyCartCheckOutList> cartListItem;

    public CartAdapter(Context mContext, ArrayList<MyCartCheckOutList> mCartListItem) {
        this.mContext = mContext;
        this.cartListItem = mCartListItem;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(dk.eatmore.demo.R.layout.cart_adapter, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {

        MyCartCheckOutList cartdetailsImage = cartListItem.get(position);

        Picasso.with(context).load(cartdetailsImage.getP_image())
                .resize(100, 105).into(holder.pizzaImgae);

        holder.cartItemName.setText(cartdetailsImage.getP_id()+" "+
                cartdetailsImage.getP_name());
        //holder.cartProductId.setText(cartdetailsImage.getP_id());
        holder.cartItemPrice.setText("Price kr:" + cartdetailsImage.getP_price());
        holder.prodyQty.setText("Qty "+cartdetailsImage.getP_quantity());

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        //  return list.size();
        return cartListItem.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView pizzaImgae;
        TextView  cartItemName, cartItemPrice,
                prodyQty;

        public View_Holder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);

          //  cartProductId = (TextView) itemView.findViewById(R.id.finalCartItemId);
            cartItemName = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.finalCartItemName);
            cartItemPrice = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.finalCartItemPrice);
            prodyQty= (TextView) itemView.findViewById(dk.eatmore.demo.R.id.prodyQty);
            pizzaImgae = (ImageView) itemView.findViewById(dk.eatmore.demo.R.id.finalCartImage);


        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();


        }
    }
//
}