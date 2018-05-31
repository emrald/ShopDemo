package dk.eatmore.demo.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import dk.eatmore.demo.R;
import dk.eatmore.demo.model.IngredientPojo;

import java.util.Collections;
import java.util.List;

public class IngredientsAdapterView extends RecyclerView.Adapter<IngredientsAdapterView.View_Holder> {

    List<IngredientPojo> list = Collections.emptyList();
    Context context;
    Intent intent;
    Fragment fragment = null;
    private int mContainerId;
    private FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    private OnItemClickListener mItemClickListener;

    public IngredientsAdapterView(List<IngredientPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_row, parent, false);
        View_Holder holder = new View_Holder(v);


        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {


        holder.mycheckBox.setText(list.get(position).getIngredientName());

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        //  return list.size();
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //        LinearLayout cv;
//        TextView paymentTitle;
        CheckBox mycheckBox;

        View_Holder(View itemView) {

            super(itemView);

            itemView.setOnClickListener(this);
            mycheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, position);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


}