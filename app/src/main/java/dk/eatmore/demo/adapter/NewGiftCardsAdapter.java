package dk.eatmore.demo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import dk.eatmore.demo.R;
import dk.eatmore.demo.interfaces.GiftCardsAdapterListener;
import dk.eatmore.demo.model.GiftCardPojo;
import dk.eatmore.demo.myutils.Utility;

import java.util.ArrayList;

/**
 * Created by sachi on 2/3/2017.
 */

public class NewGiftCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private static final int EMPTY_VIEW = 10;
//    private final int VIEW_ITEM = 1;
//    private final int VIEW_PROG = 0;

    private GiftCardsAdapterListener onAdapterItemClickListener;
    private ArrayList<GiftCardPojo> giftCardPojos = new ArrayList<>();

    public NewGiftCardsAdapter(Context mContext, GiftCardsAdapterListener listener) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.onAdapterItemClickListener = listener;
    }

    public void setArrayList(ArrayList<GiftCardPojo> arrayList) {
        this.giftCardPojos = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == EMPTY_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            EmptyViewHolder evh = null;
            evh = new EmptyViewHolder(v);
            return evh;
        }

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_card_item, parent, false);
        GiftCardsViewHolder vh = null;
        vh = new GiftCardsViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GiftCardsViewHolder) {
            GiftCardsViewHolder vh = (GiftCardsViewHolder) holder;

            final int itemPosition = vh.getAdapterPosition();

            GiftCardPojo mGiftCardPojo = giftCardPojos.get(itemPosition);

            String offerCode = mGiftCardPojo.getGiftCardCode();
//            String discountType = mGiftCardPojo.getGiftType();
            String offerCodeValue = mGiftCardPojo.getGiftvalue();
            String balanceValue = mGiftCardPojo.getBalance();
            String validity = mGiftCardPojo.getGiftExpDate();

            vh.discountTypeTextView.setText(offerCode);
//            if (discountType.equalsIgnoreCase("Percentage")) {
//                vh.offerCodeValueLeftTextView.setText(offerCodeValue + "%");
//                vh.offerCodeValueRightTextView.setText(offerCodeValue + "%");
//            } else if (discountType.equalsIgnoreCase("Discount")) {
            vh.offerCodeValueLeftTextView.setText(Utility.convertCurrencyToDanish(Double.parseDouble(offerCodeValue)) + "Kr");
            vh.offerCodeValueRightTextView.setText(Utility.convertCurrencyToDanish(Double.parseDouble(balanceValue))+ "Kr");
//            }
            vh.giftValidityTextView.setText(mContext.getResources().getString(R.string.giftcard_valid_till)
                    + " " + validity + " ( " + mContext.getResources().getString(R.string.limited_time_offer) + " )");

            vh.copyGiftCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAdapterItemClickListener.buttonOnClick(v, itemPosition);
                }
            });
        } else {
//            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return giftCardPojos.size();
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class GiftCardsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView offerCodeValueLeftTextView;
        TextView discountTypeTextView;
        TextView giftValidityTextView;
        TextView offerCodeValueRightTextView;

        Button copyGiftCodeButton;

        GiftCardsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_gift);
            offerCodeValueLeftTextView = (TextView) itemView.findViewById(R.id.tv_offer_code_val_left);
            offerCodeValueRightTextView = (TextView) itemView.findViewById(R.id.tv_offer_code_val_right);
            discountTypeTextView = (TextView) itemView.findViewById(R.id.tv_discount_type);
            giftValidityTextView = (TextView) itemView.findViewById(R.id.tv_gift_validity);

            copyGiftCodeButton = (Button) itemView.findViewById(R.id.btn_copy_gift_code);

        }
    }
}
