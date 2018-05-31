package dk.eatmore.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import dk.eatmore.demo.R;
import dk.eatmore.demo.model.AttributeCartAddingModel;
import dk.eatmore.demo.model.ProductAttributeValue;
import dk.eatmore.demo.netutils.AttributeInterface;

import java.util.ArrayList;

public class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.RecyclerViewHolder> {
    private static final String TAG = AttributeAdapter.class.getSimpleName();
    private ArrayList<ProductAttributeValue> productAttributeLists;
    private Context context;
    private LayoutInflater inflater;
    private int lastCheckedPosition = 0;
    private AttributeInterface attributeInterface;
    private ListView expandableListView;
    private int parentPos;
    private TextView extratopping_ind;

    public AttributeAdapter(Context context, ArrayList<ProductAttributeValue> mproductAttributeLists,
                            AttributeInterface mattributeInterface, ListView mexpandableListView, TextView mextratopping_ind, int mparentPos) {
        this.context = context;
        this.productAttributeLists = mproductAttributeLists;
        inflater = LayoutInflater.from(context);
        this.attributeInterface = mattributeInterface;
        this.expandableListView = mexpandableListView;
        this.parentPos = mparentPos;
        this.extratopping_ind = mextratopping_ind;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.attribute_list_row, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        holder.rbtn.setText(productAttributeLists.get(position).getAttrubuteName());

        holder.attribute_price.setText(productAttributeLists.get(position).getAttributePrice() + " kr");
        holder.rbtn.setChecked(position == lastCheckedPosition);

        holder.rbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

//    View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            RecyclerViewHolder vholder = (RecyclerViewHolder) v.getTag();
//            int position = vholder.getPosition();
//            Toast.makeText(context, "This is position " + position, Toast.LENGTH_LONG).show();
//        }
//    };

    @Override
    public int getItemCount() {
        return productAttributeLists.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView attribute_price;
        LinearLayout rbSelect;
        RadioButton rbtn;

        public RecyclerViewHolder(final View itemView) {
            super(itemView);
            attribute_price = (TextView) itemView.findViewById(R.id.attribute_price);
            rbSelect = (LinearLayout) itemView.findViewById(R.id.radioButton);
            rbtn = (RadioButton) itemView.findViewById(R.id.btn_radioButton_attributeName);
            //  imageView= (RadioButton) itemView.findViewById(R.id.radio_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemRangeChanged(0, productAttributeLists.size());

                    Log.e(TAG, String.valueOf(productAttributeLists.get(lastCheckedPosition)));

                    AttributeCartAddingModel attributeCartAddingModel = new AttributeCartAddingModel();
                    attributeCartAddingModel.setTm_id(productAttributeLists.get(lastCheckedPosition).getTmId());
                    attributeCartAddingModel.setPad_id(productAttributeLists.get(lastCheckedPosition).getPadId());
                    attributeCartAddingModel.setPam_id(productAttributeLists.get(lastCheckedPosition).getPamId());
                    attributeCartAddingModel.setA_value(productAttributeLists.get(lastCheckedPosition).getAttrubuteName());
                    attributeCartAddingModel.setA_price(productAttributeLists.get(lastCheckedPosition).getAttributePrice());

                    // For Dynamically Changing Extra Toppings
                    attributeInterface.attributeClick(getAdapterPosition(), expandableListView,
                            productAttributeLists.get(getAdapterPosition()).getTmId(),
                            attributeCartAddingModel, extratopping_ind, parentPos, productAttributeLists);

                    Log.e(TAG, "Radio Button Clicked!");

                }
            });
        }
    }
}