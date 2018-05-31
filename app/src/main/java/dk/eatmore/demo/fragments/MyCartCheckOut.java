package dk.eatmore.demo.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.adapter.ViewMyCartAdapter;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.interfaces.DeleteItemFromCartCallback;
import dk.eatmore.demo.interfaces.ViewCartCallback;
import dk.eatmore.demo.model.CartDetails;
import dk.eatmore.demo.model.MyCartCheckOutList;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.netutils.DeleteClickInterface;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import dk.eatmore.demo.network.NetworkUtils;

import com.google.gson.Gson;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static dk.eatmore.demo.myutils.Utility.convertCurrencyToDanish;

public class MyCartCheckOut extends Fragment implements ViewCartCallback, DeleteItemFromCartCallback {
    private static final String TAG = MyCartCheckOut.class.getSimpleName();
    //    private MyCartCheckOutFragmentAdapter mAdapter;
    TextView txtCheckout;
    Fragment fragment;
    //    private SwipeMenuListView mListView;
    private RecyclerView mViewCartRecyclerView;
    private ViewMyCartAdapter myCartAdapter;
    SimpleArcDialog pDialog;
    TextView cartOrderTotal;
    public static String IS_FROM_CART = "isfrom_cart";

    // private  String allcartRes="";
    public static MyCartCheckOut newInstance() {
        return new MyCartCheckOut();
    }

    ArrayList<MyCartCheckOutList> cartListItem;
    private ArrayList<CartDetails> mCartDetails = null;
    String cartCount = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View view = inflater.inflate(R.layout.activity_cart_checkout, container, false);
        // Showing Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).showBottomNavigation();

        mViewCartRecyclerView = (RecyclerView) view.findViewById(R.id.rv_view_cart);
        mViewCartRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mViewCartRecyclerView.setLayoutManager(layoutManager);
//        myCartAdapter = new ViewMyCartAdapter(getActivity(), new ArrayList<>(), new ArrayList<>(), )
//        mViewCartRecyclerView.setAdapter(myCartAdapter);
//        initSwipe();

//        mListView = (SwipeMenuListView) view.findViewById(R.id.listview);
        txtCheckout = (TextView) view.findViewById(R.id.txtCheckout);
        cartOrderTotal = (TextView) view.findViewById(R.id.cartOrderTotal);
        //  txtCheckout.setVisibility(View.VISIBLE);

        ImageView resImage = (ImageView) view.findViewById(R.id.tool_logo);
        ImageView imgback = (ImageView) view.findViewById(R.id.imgback);
        imgback.setVisibility(View.INVISIBLE);
        String resImg = ApplicationClass.getInstance().getPrefManager().
                getStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE);

        Picasso.with(getActivity())
                .load(resImg)
                .into(resImage);

        TextView resNameTextView = (TextView) view.findViewById(R.id.tootlbar_title);
//        String resName = ApplicationClass.getInstance().getPrefManager().
//                getStringPreferences(MyPreferenceManager.RESTAURANT_NAME);
//        resNameTextView.setText(resName);
        resNameTextView.setText(R.string.my_cart);

        txtCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.ORDER_NO = "";
                if (ApplicationClass.getInstance().getPrefManager()
                        .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN))
                    fragment = new CartFragment();

                else {
                    fragment = new LoginFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(IS_FROM_CART, true);
                    fragment.setArguments(bundle);
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(MyCartCheckOut.class.getName());
                fragmentTransaction.commit();


            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
//                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getActivity());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(dp2px(100));
//                // set item title
//                openItem.setTitle("Edit");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);


                SwipeMenuItem openItem1 = new SwipeMenuItem(
                        getActivity());
                // set item background
                openItem1.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                openItem1.setWidth(dp2px(100));
                // set item title
                openItem1.setTitle("Delete");
                // set item title fontsize
                openItem1.setTitleSize(18);
                // set item title font color
                openItem1.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem1);

            }
        };


//        mListView.setMenuCreator(creator);
//        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
//        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                        //   alertDialog.setCancelable(false);
//                        View titleview = getActivity().getLayoutInflater().inflate(R.layout.delete_account_title, null);
//                        TextView title = (TextView) titleview.findViewById(R.id.deletedialogtitle);
//                        title.setText("Are sure want to delete cart item?");
//                        alertDialog.setCustomTitle(titleview);
//                        //   .setMessage("Are you sure you want to delete this entry?")
//                        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                get_All_CartItem_cum_delete(cartListItem.get(position).getOp_id(), true);
//
//                            }
//                        })
////                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
////                                                public void onClick(DialogInterface dialog, int which) {
////                                                    // do nothing
////                                                }
////                                            })
//
//                                .show();
//
//                        // Toast.makeText(getActivity(),"Delete"+position+" "+index,Toast.LENGTH_SHORT).show();
//
//
//                        break;
//
//                }
//                // false : close the menu; true : not close the menu
//                return false;
//            }
//        });
        return view;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String totalPrice = intent.getStringExtra("totalPrice");

            cartOrderTotal.setText(getString(R.string.order_total) + ": " + totalPrice);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //for get product boolen false and opId=0
        get_All_CartItem_cum_delete("", false);
        //   orderDetails=new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("totalPriceIntent"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);

    }

    private void get_All_CartItem_cum_delete(final String opId, boolean isDelete) {
        pDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        pDialog.show();
//        String urlViewOrDelete;
//        Log.e(TAG, "urlViewOrDelete" + urlViewOrDelete);
        cartListItem = new ArrayList<>();
        mCartDetails = new ArrayList<>();

        if (isDelete)
            deleteCartItem(opId);
        else
            viewCart();
    }

    private void viewCart() {

        Map<String, String> params = null;
        params = new HashMap<>();
        params.put("r_token", ApiCall.R_TOKEN);
        params.put("r_key", ApiCall.R_KEY);
//                    params.put("order_no", MainActivity.ORDER_NO);

        if (ApplicationClass.getInstance().getPrefManager()
                .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN)) {

            params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
                    .getStringPreferences(MyPreferenceManager.KEY_USER_ID));

            params.put("is_login", "1");
        } else {
            params.put("is_login", "0");
            params.put("customer_id", "");
        }

        params.put("ip", ApplicationClass.getInstance().getPrefManager()
                .getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));
//                    Log.e("view cart", "" + params);

        JSONObject jsonParams = new JSONObject(params);
        String viewCartTag = "ViewCart";

        NetworkUtils.callViewCart(jsonParams, viewCartTag, MyCartCheckOut.this);
    }

    @Override
    public void viewCartCallbackSuccess(JSONObject success) {
        Log.e("VIEW_ALL_CART_ITEM", success.toString());
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();

        try {
            if (success.getBoolean("status")) {
                cartCount = success.getString("cartcnt");
                JSONArray jsonArray = success.getJSONArray("result");
                int size = jsonArray.length();

                try {
//                                    cartOrderTotal.setText(getString(R.string.order_total)+": "
//                                            + convertCurrencyToDkk(
//                                            Double.parseDouble(success.getString("order_total"))));

                    cartOrderTotal.setText(getString(R.string.order_total) + ": DKK "
                            + convertCurrencyToDanish(
                            Double.parseDouble(success.getString("order_total"))));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                txtCheckout.setVisibility(View.VISIBLE);
                //     allcartRes=success;
                for (int i = 0; i < size; i++) {

//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("products");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MyCartCheckOutList myCartCheckOutList = new MyCartCheckOutList();

//                            myCartCheckOutList.setOp_id(jsonObject1.getString("op_id"));
//                            myCartCheckOutList.setP_id(jsonObject1.getString("p_id"));
//                            myCartCheckOutList.setP_name(jsonObject1.getString("p_name"));
//                            myCartCheckOutList.setP_image(jsonObject1.getString("p_image"));
//                            myCartCheckOutList.setP_price(jsonObject1.getString("p_price"));
//                            myCartCheckOutList.setP_quantity(jsonObject1.getString("p_quantity"));

                    myCartCheckOutList.setOp_id(jsonObject1.getString("op_id"));
                    myCartCheckOutList.setP_id(jsonObject1.getString("p_id"));
                    myCartCheckOutList.setP_name(jsonObject1.getString("product_name"));
//                            myCartCheckOutList.setP_image(jsonObject1.getString("product_image"));
                    myCartCheckOutList.setP_image(jsonObject1.getString("product_image_thumbnail"));
                    myCartCheckOutList.setP_price(jsonObject1.getString("p_price"));
                    myCartCheckOutList.setP_quantity(jsonObject1.getString("quantity"));
                    cartListItem.add(myCartCheckOutList);

                    Gson gson = new Gson();
                    CartDetails mCartDetail = gson.fromJson(jsonObject1.toString(), CartDetails.class);
                    mCartDetails.add(mCartDetail);
                }


//                        gson.fromJson(jsonArray.toString(), new TypeToken<List<CartDetails>>(){}.getType());
                //  ((MainActivity)getActivity()).setCartCounter(cartCount);
                Log.e("", "");
            } else {
                cartOrderTotal.setText(getString(R.string.order_total) + ": " + "0");
                txtCheckout.setVisibility(View.GONE);
            }
            Intent intent = new Intent("CartCount");
            // You can also include some extra data.
            intent.putExtra("count", cartCount);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

            myCartAdapter = new ViewMyCartAdapter(getActivity(), mCartDetails,
                    cartListItem, new DeleteClickInterface() {
                @Override
                public void cartDeleteClick(final int pos) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    //   alertDialog.setCancelable(false);
                    View titleview = getActivity().getLayoutInflater().inflate(R.layout.delete_account_title, null);
                    TextView title = (TextView) titleview.findViewById(R.id.deletedialogtitle);
                    title.setText(R.string.remove_product_from_cart);
                    alertDialog.setCustomTitle(titleview);
                    //   .setMessage("Are you sure you want to delete this entry?")
                    alertDialog
                            .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    get_All_CartItem_cum_delete(cartListItem.get(pos).getOp_id(), true);
                                }
                            })
                            .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                }
            });
            mViewCartRecyclerView.setAdapter(myCartAdapter);
            myCartAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  Log.e("Token", "sendRegistrationToServer Response: " + success.toString());

    }

    @Override
    public void viewCartCallbackError(VolleyError volleyError) {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableViewCart();
        }
    }

    public void showDialogInternetUnavailableViewCart() {
        final AlertDialog internetAlertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(dk.eatmore.demo.R.layout.dialog_internet_unavailable, null);

        Button okButton = (Button) view.findViewById(R.id.btn_ok_internet);
        Button closeButton = (Button) view.findViewById(R.id.btn_close_internet);
        builder.setView(view);
        internetAlertDialog = builder.create();
        internetAlertDialog.setCancelable(false);
        internetAlertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetAlertDialog.dismiss();
                viewCart();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetAlertDialog.dismiss();
                getActivity().finish();
            }
        });
    }

    private void deleteCartItem(final String opId) {
        String deleteTag = "DeleteCartItem";
        NetworkUtils.callDeleteItemFromCart(opId, deleteTag, MyCartCheckOut.this);
    }

    @Override
    public void deleteItemFromCartCallbackSuccess(String success, String opId) {
        Log.e(TAG, "VIEW_ALL_CART_ITEM: " + success);
        pDialog.hide();

        try {
            JSONObject jsonObject = new JSONObject(success);

            cartCount = jsonObject.getString("cartcnt");
            if (jsonObject.getBoolean("status")) {
                // TODO: 2/15/2017 change order value
                get_All_CartItem_cum_delete(opId, false);
            } else {
                cartOrderTotal.setText(getString(R.string.order_total) + ": " + "0");
                txtCheckout.setVisibility(View.GONE);
            }
            Intent intent = new Intent("CartCount");
            // You can also include some extra data.
            intent.putExtra("count", cartCount);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

            myCartAdapter = new ViewMyCartAdapter(getActivity(), mCartDetails,
                    cartListItem, new DeleteClickInterface() {
                @Override
                public void cartDeleteClick(final int pos) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    //   alertDialog.setCancelable(false);
                    View titleview = getActivity().getLayoutInflater().inflate(R.layout.delete_account_title, null);
                    TextView title = (TextView) titleview.findViewById(R.id.deletedialogtitle);
                    title.setText(R.string.remove_product_from_cart);
                    alertDialog.setCustomTitle(titleview);
                    //   .setMessage("Are you sure you want to delete this entry?")
                    alertDialog
                            .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    get_All_CartItem_cum_delete(cartListItem.get(pos).getOp_id(), true);
                                }
                            })
                            .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                }
            });
            mViewCartRecyclerView.setAdapter(myCartAdapter);
            myCartAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteItemFromCartCallbackError(VolleyError volleyError) {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();

        String msg = getResources().getString(R.string.try_again);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}


