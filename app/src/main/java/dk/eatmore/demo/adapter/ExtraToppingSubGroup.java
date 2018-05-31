package dk.eatmore.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import dk.eatmore.demo.R;
import dk.eatmore.demo.model.ExtraToppeningCategory;

import java.util.ArrayList;

/**
 * Created by ADMIN on 13-12-2016.
 */

public class ExtraToppingSubGroup extends BaseAdapter {

    private String padId;
    private ArrayList<ExtraToppeningCategory> productAttributeLists = new ArrayList<>();
    private Context context;

    public ExtraToppingSubGroup(ArrayList<ExtraToppeningCategory> productAttributeLists, Context context) {
        this.productAttributeLists = productAttributeLists;
        this.context = context;
//        Toast.makeText(context, ""+productAttributeLists.size(), Toast.LENGTH_SHORT).show();
    }

    public ExtraToppingSubGroup(ArrayList<ExtraToppeningCategory> productAttributeLists, String padId, Context context) {
        this.productAttributeLists = productAttributeLists;
        this.context = context;
        this.padId = padId;
//        Toast.makeText(context, ""+productAttributeLists.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCount() {
        return productAttributeLists == null ? 0 : productAttributeLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.attribute_extratopping_row, parent, false);

            viewHolder = new ViewHolderItem();

            viewHolder.ectraTopping_Title = (TextView) convertView.findViewById(R.id.extraToppingTitle);
            viewHolder.ectraTopping_list = (TextView) convertView.findViewById(R.id.extraToppingSubList);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }
        ExtraToppeningCategory extraToppeningCategory = productAttributeLists.get(position);

        viewHolder.ectraTopping_Title.setText(extraToppeningCategory.name);
        return convertView;
    }

    private static class ViewHolderItem {
        TextView ectraTopping_Title;
        TextView ectraTopping_list;
    }

}
