package dk.eatmore.demo.myutils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by ADMIN on 26-07-2016.
 */
public class ExpandableListUtility {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

//            if(numberOfItems==1)
//            {
//
//                // Get total height of all items.
//                int totalItemsHeight = 0;
//                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
//                    View item = listAdapter.getView(itemPos, null, listView);
//                    float px = 500 * (listView.getResources().getDisplayMetrics().density);
//                    item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                    totalItemsHeight += item.getMeasuredHeight();
//
//                }
//                totalItemsHeight += totalItemsHeight;
////                totalItemsHeight += totalItemsHeight;
////                totalItemsHeight += totalItemsHeight;
//                // Get total height of all item dividers.
//                int totalDividersHeight = listView.getDividerHeight() *
//                        (numberOfItems - 1);
//                // Get padding
//                int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();
//
//                // Set list height.
//                ViewGroup.LayoutParams params = listView.getLayoutParams();
//                params.height = totalItemsHeight + totalDividersHeight + totalPadding;
//                listView.setLayoutParams(params);
//                listView.requestLayout();
//            }
//
//
//           else if(numberOfItems==2)
//            {
//
//                // Get total height of all items.
//                int totalItemsHeight = 0;
//                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
//                    View item = listAdapter.getView(itemPos, null, listView);
//                    float px = 500 * (listView.getResources().getDisplayMetrics().density);
//                    item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                    totalItemsHeight += item.getMeasuredHeight();
//
//                }
//               // totalItemsHeight += (totalItemsHeight/2);
//                totalItemsHeight += totalItemsHeight;
//                // Get total height of all item dividers.
//                int totalDividersHeight = listView.getDividerHeight() *
//                        (numberOfItems - 1);
//                // Get padding
//                int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();
//
//                // Set list height.
//                ViewGroup.LayoutParams params = listView.getLayoutParams();
//                params.height = totalItemsHeight + totalDividersHeight + totalPadding;
//                listView.setLayoutParams(params);
//                listView.requestLayout();
//            }
//            else
//            {
                // Get total height of all items.
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = listAdapter.getView(itemPos, null, listView);
                    float px = 500 * (listView.getResources().getDisplayMetrics().density);
                    item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    totalItemsHeight += item.getMeasuredHeight();
                }

                // Get total height of all item dividers.
                int totalDividersHeight = listView.getDividerHeight() *
                        (numberOfItems - 1);
                // Get padding
                int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

                // Set list height.
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight + totalPadding;
                listView.setLayoutParams(params);
                listView.requestLayout();
            }




      //  }
    }

    }
