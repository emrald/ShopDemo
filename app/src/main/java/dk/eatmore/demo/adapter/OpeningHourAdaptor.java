package dk.eatmore.demo.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dk.eatmore.demo.model.OpeningHourPojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpeningHourAdaptor extends RecyclerView.Adapter<OpeningHourAdaptor.View_Holder> {

    List<OpeningHourPojo> list = Collections.emptyList();
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

    public OpeningHourAdaptor(List<OpeningHourPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(dk.eatmore.demo.R.layout.opening_hour_row, parent, false);
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {

        OpeningHourPojo infoBarPojo = list.get(position);

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.nameOfDays.setText(list.get(position).getOpeningDays());
        holder.openingHour.setText(list.get(position).getOpeningTime());
        // holder.cv.setCardBackgroundColor(Color.parseColor("#FFC9CCF1"));
        if (list.get(position).getOpeningFlag()) {
            holder.cv.setBackgroundColor(Color.parseColor("#019E0F"));
            holder.nameOfDays.setTextColor(Color.parseColor("#ffffff"));
            holder.openingHour.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.cv.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.nameOfDays.setTextColor(Color.parseColor("#000000"));
            holder.openingHour.setTextColor(Color.parseColor("#000000"));
        }


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

//    // Insert a new item to the RecyclerView on a predefined position
//    public void insert(int position, OpeningHourAdaptor data) {
//        list.add(position, data);
//        notifyItemInserted(position);
//    }
//
//    // Remove a RecyclerView item containing a specified Data object
//    public void remove(InfoBarPojo data) {
//        int position = list.indexOf(data);
//        list.remove(position);
//        notifyItemRemoved(position);
//    }


    class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout cv;
        TextView nameOfDays, openingHour;
        ArrayList<OpeningHourPojo> infoBarPojos = new ArrayList<OpeningHourPojo>();
        Context cntxt;

        View_Holder(View itemView) {

            super(itemView);
            this.infoBarPojos = infoBarPojos;
            this.cntxt = cntxt;
            itemView.setOnClickListener(this);
            cv = (LinearLayout) itemView.findViewById(dk.eatmore.demo.R.id.openingHourCardView);
            nameOfDays = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.openDays);
            openingHour = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.openingTiming);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //  getActivity().getSupportFragmentManager().popBackStack();
                    //  Toast.makeText(getActivity(), "Left Swipe", Toast.LENGTH_SHORT).show();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Toast.makeText(getActivity(), "Right Swipe", Toast.LENGTH_SHORT).show();
                    // getActivity().getSupportFragmentManager().popBackStack();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}