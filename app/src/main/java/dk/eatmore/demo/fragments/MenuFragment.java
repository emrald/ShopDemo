package dk.eatmore.demo.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.LoginActivity;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.adapter.ContactsPagerAdapter;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.interfaces.ProductListCallback;
import dk.eatmore.demo.model.MenuItemPojo;
import dk.eatmore.demo.model.menuPojo;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import dk.eatmore.demo.network.NetworkUtils;
import com.leo.simplearcloader.SimpleArcDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static dk.eatmore.demo.activity.MainActivity.isRestaurantClosed;
import static dk.eatmore.demo.activity.MainActivity.restaurantClosedMsg;

public class MenuFragment extends Fragment implements ProductListCallback {

    private static final String TAG = MenuFragment.class.getSimpleName();
    private View rootView;
    private EditText searchEditText;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private SimpleArcDialog mSimpleArcDialog = null;

    private ArrayList<menuPojo> menuArrayList = null;
    private ArrayList<MenuItemPojo> arraymenuItem = null;
    private ContactsPagerAdapter mAdapter;
    private String serchProductName, prodImage;
    // private MaterialSearchView searchView;
    public static String SEARCHED_PROD = "serach_prod";
    Double prodPrice = 0.0;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    //    private ImageView resImage;
    private ImageView backImage;
    private TextView resNameTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstaceState) {
        rootView = inflater.inflate(R.layout.activity_contacts_layout, parent, false);
        // Showing Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).showBottomNavigation();
//        rootView = inflater.inflate(R.layout.test, parent, false);
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        bindViews();

        menuArrayList = new ArrayList<>();

        String resImg = ApplicationClass.getInstance().getPrefManager().
                getStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE);
//        Picasso.with(getActivity())
//                .load(resImg)
//                .into(resImage);

        String resName = ApplicationClass.getInstance().getPrefManager().
                getStringPreferences(MyPreferenceManager.RESTAURANT_NAME);
        resNameTextView.setText(resName);

        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);

        backImage.setVisibility(View.INVISIBLE);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    serchProductName = searchEditText.getText().toString().trim();
                    if (!serchProductName.isEmpty()) {
                        searchEditText.setText(null);

                        SearchProductFragment fragment = new SearchProductFragment();
                        // title="Explore The MindSets";
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        // fragmentManager= ((Activity)context).getFragmentManager();
                        FragmentTransaction fragmentTransactionLogin = fragmentManager.beginTransaction();
                        //   fragmentTransactionLogin.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
                        Bundle args = new Bundle();
                        args.putString(SEARCHED_PROD, serchProductName);
                        fragment.setArguments(args);

                        fragmentTransactionLogin.add(R.id.fragment_container, fragment);
                        fragmentTransactionLogin.addToBackStack(MenuFragment.class.getName());
                        fragmentTransactionLogin.commit();
                    }

                    return true;
                }
                return false;
            }
        });

        searchEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() <= (searchEditText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // Toast.makeText(getActivity(),"hello",1000).show();
                        if (searchEditText.getText().length() > 0)
                            searchEditText.setText(null);
                        return true;
                    }
                }
                return false;
            }
        });

        getMenuListArray();
        return rootView;
    }

    private void bindViews() {
//        resImage = (ImageView) rootView.findViewById(R.id.tool_logo);
        backImage = (ImageView) rootView.findViewById(R.id.imgback);
        backImage.setVisibility(View.GONE);
        resNameTextView = (TextView) rootView.findViewById(R.id.tootlbar_title);
        searchEditText = (EditText) rootView.findViewById(R.id.serchText);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    private void getMenuListArray() {
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mSimpleArcDialog.show();

        String tag_string_req = "get_prod";

        NetworkUtils.getProductList(tag_string_req, MenuFragment.this);
    }

    @Override
    public void getProductListCallbackSuccess(String success) {
        //     Log.e("get_prod", "get_prod" + response);
//                        mSimpleArcDialog.dismiss();
        new ProgressDialaogView().dismissSimpleArcDialog(mSimpleArcDialog);

        menuPojo data = null;
        boolean isUserDeleted = false;
        try {
            JSONObject jObj = new JSONObject(success);
            if (ApplicationClass.getInstance().getPrefManager()
                    .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN))
                isUserDeleted = jObj.getBoolean("is_user_deleted");

            boolean status = jObj.getBoolean("status");
//                            isRestaurantClosed = jObj.getBoolean("isrestaurantclosed");
            if (!isUserDeleted) {
                if (status) {
                    prodImage = jObj.getString("image_path");
                    String imageThumbnailPath = jObj.getString("product_image_thumbnail_path");
                    JSONArray jsonArray = jObj.getJSONArray("menu");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        try {
                            arraymenuItem = new ArrayList<>();
                            data = new menuPojo();
                            JSONArray prod_jsonArray = jsonObject.getJSONArray("product_list");
                            data.setMenuName(jsonObject.getString("c_name"));

                            for (int k = 0; k < prod_jsonArray.length(); k++) {

                                prodPrice = 0.0;

                                JSONObject itemjosn = prod_jsonArray.getJSONObject(k);
                                MenuItemPojo itempojo = new MenuItemPojo();
                                itempojo.setCategoryId(checkNull(itemjosn, "c_id"));
                                itempojo.setIsAttribute(checkNull(itemjosn, "is_attributes"));
                                itempojo.setProdutId(checkNull(itemjosn, "p_id"));
                                itempojo.setProdut_no(checkNull(itemjosn, "product_no"));
                                itempojo.setProductName(checkNull(itemjosn, "p_name"));
                                itempojo.setProductDesc(checkNull(itemjosn, "p_desc"));

                                itempojo.setProductIngredients(checkNull(itemjosn, "product_ingredients"));

                                itempojo.setPrductImage(checkNull(itemjosn, "p_image"));
                                itempojo.setExtra_topping_group(checkNull(itemjosn, "extra_topping_group"));

                                itempojo.setProductImageMainUrl(prodImage);
                                itempojo.setProductThumbnailMainUrl(imageThumbnailPath);

                                if (itemjosn.getString("is_attributes").equals("1")) {

                                    JSONArray pricrArray = itemjosn.getJSONArray("product_attribute");

                                    for (int priceArrayI = 0; priceArrayI < pricrArray.length(); priceArrayI++) {
                                        if (!pricrArray.getJSONObject(priceArrayI).isNull("default_attribute_value")) {
                                            JSONObject priceJson = pricrArray.getJSONObject(priceArrayI).
                                                    getJSONObject("default_attribute_value");
                                            prodPrice = prodPrice + Double.parseDouble(priceJson.getString("a_price"));
                                        }

                                        itempojo.setProductPrice("" + prodPrice);
                                    }
                                } else
                                    itempojo.setProductPrice(checkNull(itemjosn, "p_price"));

                                arraymenuItem.add(itempojo);
                            }
                            data.setMenuItem(arraymenuItem);
                        } catch (Exception ex) {
                            Log.e(TAG, "main menu error" + ex.toString());
                            ex.printStackTrace();
                            continue;
                        }
                        menuArrayList.add(data);

                    }
                    mAdapter.addData(menuArrayList);
                }

                mAdapter.notifyDataSetChanged();
            } else {
                // Deleted Account
                ApplicationClass.getInstance().getPrefManager().clearSharedPreference();

                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("deletedUser", "1");
                getActivity().startActivity(i);
                getActivity().finish();
//
//                                Intent intent = new Intent("DeletedAccount");
//                                // You can also include some extra data.
//                                intent.putExtra("deletedUser", "1");
//                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("storePayment", "storePayment Error: " + e.toString());
        }
        if (isRestaurantClosed) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dilaog_info_layout, null);

            TextView textViewTitle = (TextView) view.findViewById(R.id.textviewinfoTitle);
            Button butonOk = (Button) view.findViewById(R.id.buttonAaddtocartOk);
            TextView dialogText = (TextView) view.findViewById(R.id.addedtocartText);
            textViewTitle.setText("Restaurant is closed");
            builder.setView(view);
            dialogText.setTypeface(null, Typeface.BOLD);
            dialogText.setText(restaurantClosedMsg);

            final AlertDialog forgotDialog = builder.create();
            forgotDialog.show();


            butonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forgotDialog.dismiss();
                }
            });
        }

    }

    @Override
    public void getProductListCallbackError(VolleyError volleyError) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e(TAG, "storePayment Error: " + volleyError.getMessage());
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableProductList();
        }
    }

    public void showDialogInternetUnavailableProductList() {
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
                getMenuListArray();
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

    private String checkNull(final JSONObject json, final String key) {
        return json.isNull(key) ? "" : json.optString(key);
    }

    private boolean checkBooleanNull(final JSONObject json, final String key) {
        return json.isNull(key) ? true : false;
    }

    private void setupViewPager(ViewPager viewPager) {
        //   arraymenuItem=new ArrayList<>();
        mAdapter = new ContactsPagerAdapter(getActivity().getSupportFragmentManager());
        mAdapter.addData(menuArrayList);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(0);
        mAdapter.notifyDataSetChanged();
    }
}