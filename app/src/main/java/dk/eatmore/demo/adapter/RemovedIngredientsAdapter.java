package dk.eatmore.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dk.eatmore.demo.model.RemovedIngredientsObject;

import java.util.List;

/**
 * Created by sachi on 2/8/2017.
 */
public class RemovedIngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private static final int EMPTY_VIEW = 10;

    private List<RemovedIngredientsObject> removedIngredientsObjects;

    public RemovedIngredientsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setArrayList(List<RemovedIngredientsObject> removedIngredientsObjects) {
        this.removedIngredientsObjects = removedIngredientsObjects;
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

        v = LayoutInflater.from(parent.getContext()).inflate(dk.eatmore.demo.R.layout.removed_ingredients_item, parent, false);
        RemovedIngredientsViewHolder vh = null;
        vh = new RemovedIngredientsViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RemovedIngredientsViewHolder) {
            RemovedIngredientsViewHolder vh = (RemovedIngredientsViewHolder) holder;

            final int itemPosition = vh.getAdapterPosition();

            RemovedIngredientsObject mRemovedIngredientsObject = removedIngredientsObjects.get(itemPosition);
            String ingredientName = mRemovedIngredientsObject.getIngredient_name();
            vh.ingredientNameTextView.setText(ingredientName);
        }
    }

    @Override
    public int getItemCount() {
        return removedIngredientsObjects.size();
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class RemovedIngredientsViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientNameTextView;

        RemovedIngredientsViewHolder(View itemView) {
            super(itemView);
            ingredientNameTextView = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.tv_ingredient_name);
        }
    }
}
