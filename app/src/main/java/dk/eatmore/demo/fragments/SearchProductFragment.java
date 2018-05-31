package dk.eatmore.demo.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.adapter.ProductListAdapter;
import dk.eatmore.demo.interfaces.ProductSearchListCallback;
import dk.eatmore.demo.model.MenuItemPojo;
import dk.eatmore.demo.model.menuPojo;
import dk.eatmore.demo.network.NetworkUtils;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ADMIN on 19-09-2016.
 */
public class SearchProductFragment extends Fragment implements ProductSearchListCallback {
    private static final String TAG = SearchProductFragment.class.getSimpleName();
    private View view;
    private ImageView imgback;
    private EditText product_search;
    private RecyclerView mRecyclerView;
    private RelativeLayout errorLayout;
    private SimpleArcDialog mDialog;

    private ArrayList<MenuItemPojo> menuItem;
    private ProductListAdapter mAdapter;
    private String searchProductName;

    public SearchProductFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstaceState) {
        view = inflater.inflate(R.layout.search_result_view, parent, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.serachRecyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        errorLayout = (RelativeLayout) view.findViewById(R.id.error_layout);
        errorLayout.setVisibility(View.GONE);
        TextView txt_error_message = (TextView) view.findViewById(R.id.txt_error_message);
        txt_error_message.setText(R.string.no_result);

        initToolbar();
        initDialog();
        searchProductName = getArguments().getString(MenuFragment.SEARCHED_PROD);

        product_search = (EditText) view.findViewById(R.id.product_search);

        product_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchProductName = product_search.getText().toString().trim();
                    if (!searchProductName.isEmpty()) {
                        getSearchedResult(searchProductName);
                    }
                    return true;
                }
                return false;
            }
        });

        product_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() <= (product_search.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // Toast.makeText(getActivity(),"hello",1000).show();
                        if (product_search.getText().length() > 0)
                            product_search.setText(null);

                        return true;
                    }
                }
                return false;
            }
        });

        getSearchedResult(searchProductName);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setDataToView();
    }

    private void initToolbar() {
        TextView title = (TextView) view.findViewById(R.id.tootlbar_title);
        title.setText(getResources().getString(R.string.search_result));

        imgback = (ImageView) view.findViewById(R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDialog() {
        mDialog = new SimpleArcDialog(getActivity());
        mDialog.setCancelable(false);

        ArcConfiguration configuration = new ArcConfiguration(getActivity());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText(getString(R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mDialog.setConfiguration(configuration);
    }

    private void getSearchedResult(final String searchText) {
        menuItem = new ArrayList<>();
        mDialog.show();
        String tag_string_req = "getSearchedResult";

        NetworkUtils.getProductSearchList(searchText, tag_string_req, SearchProductFragment.this);
    }

    @Override
    public void getProductSearchListCallbackSuccess(String success) {
        Log.e(TAG, "response : " + success);

        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        menuPojo data = null;
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            if (status) {
                String prodImage = jObj.getString("product_image_path");
                String imageThumbnailPath = jObj.getString("product_image_thumbnail_path");

                errorLayout.setVisibility(View.GONE);
                //   menuItem=new ArrayList<>();
                JSONArray jsonArray = jObj.getJSONArray("searched_product_list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    double prodPrice = 0.0;
                    JSONObject itemjosn = jsonArray.getJSONObject(i);

                    MenuItemPojo itempojo = new MenuItemPojo();
                    itempojo.setCategoryId(checkNull(itemjosn, "c_id"));
                    itempojo.setIsAttribute(checkNull(itemjosn, "is_attributes"));
                    itempojo.setProdutId(checkNull(itemjosn, "p_id"));
                    itempojo.setProdut_no(checkNull(itemjosn, "product_no"));
                    itempojo.setProductName(checkNull(itemjosn, "p_name"));
                    itempojo.setProductDesc(checkNull(itemjosn, "p_desc"));
                    itempojo.setProductPrice(checkNull(itemjosn, "p_price"));

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

                    menuItem.add(itempojo);
                }

            } else {
                errorLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), getString(R.string.no_result),
                        Toast.LENGTH_LONG).show();
                // getActivity().getSupportFragmentManager().popBackStack();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "storePayment Error: " + e.toString());
        }
//                mAdapter = new PizzaFragmentAdapter(getActivity(), menuItem);
        mAdapter = new ProductListAdapter(getActivity(), menuItem);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void getProductSearchListCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        Log.e(TAG, "storePayment Error: " + volleyError.getMessage());
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableSearch();
        }
    }

    public void showDialogInternetUnavailableSearch() {
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
                getSearchedResult(searchProductName);
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
}
