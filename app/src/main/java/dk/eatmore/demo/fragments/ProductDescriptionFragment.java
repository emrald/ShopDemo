package dk.eatmore.demo.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.LoginActivity;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.adapter.AttributeAdapter;
import dk.eatmore.demo.adapter.ExtraToppingSubGroup;
import dk.eatmore.demo.adapter.IngredientsAdapterView;
import dk.eatmore.demo.interfaces.AddToCartCallback;
import dk.eatmore.demo.interfaces.ProductDescriptionCallback;
import dk.eatmore.demo.interfaces.ProductExtraToppingsCallback;
import dk.eatmore.demo.model.AttributeCartAddingModel;
import dk.eatmore.demo.model.ExtraToppeningCategory;
import dk.eatmore.demo.model.ExtraToppingCartAddingModel;
import dk.eatmore.demo.model.ExtraToppingPriceCalculation;
import dk.eatmore.demo.model.IngredientPojo;
import dk.eatmore.demo.model.ProductAttributeList;
import dk.eatmore.demo.model.ProductAttributeValue;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.myutils.ExpandableListUtility;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.ParamExpandableListView;
import dk.eatmore.demo.netutils.AttributeInterface;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import dk.eatmore.demo.network.NetworkUtils;

import com.google.gson.Gson;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.picasso.Picasso;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import static dk.eatmore.demo.myutils.Utility.checkNull;
import static dk.eatmore.demo.myutils.Utility.convertCurrencyToDanish;
import static dk.eatmore.demo.myutils.Utility.formatValueToMoney;

public class ProductDescriptionFragment extends Fragment implements ProductDescriptionCallback, AddToCartCallback, ProductExtraToppingsCallback {
    private static final String TAG = ProductDescriptionFragment.class.getSimpleName();

    private View rootView;
    private TagFlowLayout mFlowLayout;
    private TagAdapter<String> mAdapter;
    private ListView recyclerView_extraTopping;
    private RecyclerView extraToppingRecyclerView;
    private ArrayList<IngredientPojo> ingredientPojos;
    private IngredientsAdapterView adapter;
    private SimpleArcDialog mDialog;
    ArrayList<ProductAttributeList> productAttributeLists_array;
    ArrayList<ProductAttributeValue> productAttributeValues;
    TextView cooseSizeText, atributeTitle;
    LinearLayout sizell;
    // Boolean array for initial selected items
    ImageView imgback;
    private ArrayList<ExtraToppeningCategory> categories = new ArrayList<>();
    Button btnAddToCart;
    String prodcut_id, isAttrubueAvailable;
    TextView productIdName;
    HashMap<String, String> ingredentsMap;

    HashMap<String, String> extraToppingHashMap;

    HashMap<Integer, AttributeCartAddingModel> attributeListHashMap;


    HashMap<String, ExtraToppingCartAddingModel> extraToppingSelectedName;

    //price calculation hash map
    HashMap<Integer, String> attributePriceCalculation;
    HashMap<String, ExtraToppingPriceCalculation> extraToppingPriceCalculation;

    ArrayList<AttributeCartAddingModel> attributeCartAddingModelsArray;

    String ingredients, ingredientsName;
    ExpandableListView paramExpandableListView;
    String prodQuantaty;

    TextSwitcher ProductCountText;
    ImageView prodDecrement, prodIncrement;
    TextView productTotalTextView;
    String productTotalPrice = "0";
    String productName = "";
    private int mCounter = 1;
    Gson gson;
    ScrollView mainScrollView;

    HashMap<String, Boolean> ingridentsListAll;
    String[] mVals;
    //09-12-2016
    AttributeAdapter attributeAdapter;
    RecyclerView recyclerView;
    TextView extratopping_title_indi;
    ExtraToppingSubGroup attributeExtratoppingAdapter;
    TextView ingredientsText;
    boolean productHaveAttribute = false;

    private String addToCartProductPrice = "0";
    private JSONArray ingredientsJsonArray = new JSONArray();
    private String padId = "";
    private int previousMainPos = -1;
    private HashMap<Integer, Integer> extraToppingsParentPositionMap;
    private ArrayList<HashMap<String, Integer>> extraToppingsToRemove;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(dk.eatmore.demo.R.layout.activity_extra_toppings, container, false);
        bindViews();

        Bundle bundle = this.getArguments();
        prodcut_id = bundle.getString("product_id", "0");

        productHaveAttribute = bundle.getBoolean("productHaveAttribute", false);
        productHaveAttribute = false;
//        if (productHaveAttribute) {
//            btnAddToCart.setVisibility(View.GONE);
//            String productPrice = bundle.getString("pPrice");
//
//            JSONObject extratoppingJson = new JSONObject();
//            try {
//                extratoppingJson.accumulate("r_token", ApiCall.R_TOKEN);
//                extratoppingJson.accumulate("r_key", ApiCall.R_KEY);
//                extratoppingJson.accumulate("order_no", MainActivity.ORDER_NO);
//
//                if (ApplicationClass.getInstance().getPrefManager()
//                        .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN)) {
//                    extratoppingJson.accumulate("customer_id", ApplicationClass.getInstance().getPrefManager()
//                            .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
//                    extratoppingJson.accumulate("is_login", "1");
//                } else {
//                    extratoppingJson.accumulate("is_login", "0");
////                    extratoppingJson.accumulate("customer_id", "");
//                }
//                //device id
//                extratoppingJson.accumulate("ip", ApplicationClass.getInstance().getPrefManager()
//                        .getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));
//
//
//                extratoppingJson.accumulate("p_id", prodcut_id);
//                extratoppingJson.accumulate("p_quantity", "1");
//                if (productTotalPrice.equalsIgnoreCase("0"))
//                    extratoppingJson.accumulate("p_price", addToCartProductPrice);
//                else
//                    extratoppingJson.accumulate("p_price", productTotalPrice);
//
//                //setting blank ingridients
//                JSONArray ingredientsArray = new JSONArray();
//                JSONObject ingredientsObj = new JSONObject();
//                //ingredientsObj.put("i_id", "");
//                ingredientsArray.put(ingredientsObj);
//                extratoppingJson.put("ingredients", ingredientsArray);
//
//                //setting blank attributr
//                JSONArray attributeJsonArray = new JSONArray();
//                JSONObject attributeObj = new JSONObject();
////                attributeObj.put("pam_id","");
////                attributeObj.put("pad_id","");
////                attributeObj.put("tm_id","");
////                attributeObj.put("attribute_name","");
//                attributeJsonArray.put(attributeObj);
//                extratoppingJson.put("attrubutes", attributeJsonArray);
//
//                //setting blank extraTopping
//                JSONArray extratoppingJsonArray = new JSONArray();
//                JSONObject extratoppingObj = new JSONObject();
////                extratoppingObj.put("tsgd_id","");
////                extratoppingObj.put("i_name","");
////                extratoppingObj.put("tm_id","");
//                extratoppingJsonArray.put(extratoppingObj);
//                extratoppingJson.put("extratoppings", extratoppingJsonArray);
//
//                Log.e(TAG, "don " + extratoppingJson);
//                addToCartApi(extratoppingJson);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else
        getProductDesc();

        clearSharedPref();

        productAttributeLists_array = new ArrayList<>();
        productAttributeValues = new ArrayList<>();

        ingredentsMap = new HashMap<>();


        // extraToppingCartAddingModelsArray = new ArrayList<>();
        attributeListHashMap = new HashMap<>();
        attributeCartAddingModelsArray = new ArrayList<>();

        attributePriceCalculation = new HashMap<>();
        extraToppingPriceCalculation = new HashMap<>();
        extraToppingSelectedName = new HashMap<>();
        extraToppingsParentPositionMap = new HashMap<>();
        extraToppingsToRemove = new ArrayList<>();

        extraToppingHashMap = new HashMap<>();
        gson = new Gson();

        mainScrollView = (ScrollView) rootView.findViewById(dk.eatmore.demo.R.id.mainlayoutScroll);
        productTotalTextView = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.productTotal);
        prodIncrement = (ImageView) rootView.findViewById(dk.eatmore.demo.R.id.prodIncrement);
        prodDecrement = (ImageView) rootView.findViewById(dk.eatmore.demo.R.id.prodDecrement);
        ProductCountText = (TextSwitcher) rootView.findViewById(dk.eatmore.demo.R.id.prodItemCount);
        ingredientsText = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.ingredientsText);

        ingredientsText.setText(Html.fromHtml(getString(dk.eatmore.demo.R.string.ingredients) + "<font color='red'>" + " <small> " + getString(dk.eatmore.demo.R.string.press_on_an_ingredient_to_remove_it) + "</small>" + "</font>"));


        prodIncrement.setClickable(true);
        prodDecrement.setClickable(true);
        ProductCountText.setText("1");

        prodIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCounter++;
                ProductCountText.setText(String.valueOf(mCounter));
                calculatePrice();
            }
        });


        prodDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PgetroductRecipes();

                if (mCounter != 1)
                    mCounter--;

                ProductCountText.setText(String.valueOf(mCounter));
                calculatePrice();
            }
        });


        paramExpandableListView = new ParamExpandableListView(getActivity());

        productIdName = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.productIdName);
        initToolBar();
        ingredientList();
        //extraToppingList();
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView currentlyShownTextView = (TextView) ProductCountText.getCurrentView();
                prodQuantaty = currentlyShownTextView.getText().toString();

                JSONObject extratoppingJson = new JSONObject();
                try {
                    extratoppingJson.accumulate("r_token", ApiCall.R_TOKEN);
                    extratoppingJson.accumulate("r_key", ApiCall.R_KEY);

                    extratoppingJson.accumulate("order_no", MainActivity.ORDER_NO);


                    if (ApplicationClass.getInstance().getPrefManager()
                            .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN)) {
                        extratoppingJson.accumulate("customer_id", ApplicationClass.getInstance().getPrefManager()
                                .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                        extratoppingJson.accumulate("is_login", "1");
                    } else {
                        extratoppingJson.accumulate("is_login", "0");
//                        extratoppingJson.accumulate("customer_id", "");
                    }

                    //device id
                    extratoppingJson.accumulate("ip", ApplicationClass.getInstance().getPrefManager()
                            .getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));

                    extratoppingJson.accumulate("p_id", prodcut_id);
                    extratoppingJson.accumulate("p_quantity", prodQuantaty);
                    if (productTotalPrice.equalsIgnoreCase("0")
                            || (Double.parseDouble(addToCartProductPrice) > Double.parseDouble(productTotalPrice)))
                        extratoppingJson.accumulate("p_price", addToCartProductPrice);
                    else
                        extratoppingJson.accumulate("p_price", productTotalPrice);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  JSONObject obj = new JSONObject();
//                JSONArray ingredientsArray = new JSONArray();
//                for (String key : ingredentsMap.keySet()) {
//                    JSONObject ingredientsObj = new JSONObject();
//                    try {
//                        ingredientsObj.put("i_id", key);
//                        ingredientsObj.put("p_id", prodcut_id);
//                        //    reqObj.put("ingredientsName", ingredentsMap.get(key));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    ingredientsArray.put(ingredientsObj);
//                }
//
//                try {
//                    extratoppingJson.put("ingredients", ingredientsArray);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                try {
                    extratoppingJson.put("ingredients", ingredientsJsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray attributeJsonArray = new JSONArray();
                for (int key : attributeListHashMap.keySet()) {
                    AttributeCartAddingModel attributeCartAddingModel = attributeListHashMap.get(key);
                    JSONObject attributeObj = new JSONObject();

                    try {
//                        attributeObj.put("pam_id", attributeCartAddingModel.getPam_id());
                        attributeObj.put("pad_id", attributeCartAddingModel.getPad_id());
                        attributeObj.put("p_id", prodcut_id);

//                        attributeObj.put("tm_id", attributeCartAddingModel.getTm_id());
//                        attributeObj.put("attribute_name", attributeCartAddingModel.getA_value());
                        //   reqObj.put("a_value", attributeCartAddingModel.getA_value());
                        //   reqObj.put("a_price", attributeCartAddingModel.getA_price());

                        attributeJsonArray.put(attributeObj);

                        extratoppingJson.put("attrubutes", attributeJsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.e(TAG, "" + extraToppingSelectedName);
                JSONArray extratoppingJsonArray = new JSONArray();
                for (String key : extraToppingSelectedName.keySet()) {
                    ExtraToppingCartAddingModel extraTopping = extraToppingSelectedName.get(key);
                    if (extraTopping.isSelected()) {
                        try {
                            JSONObject extratoppingObj = new JSONObject();
                            extratoppingObj.put("tsgd_id", extraTopping.getTsgd_id());
//                            extratoppingObj.put("i_name", extraTopping.getName());
//                            extratoppingObj.put("tm_id", extraTopping.getTm_id());
                            extratoppingObj.put("pad_id", extraTopping.getPad_id());
                            extratoppingObj.put("p_id", prodcut_id);

                            extratoppingJsonArray.put(extratoppingObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                try {
                    extratoppingJson.put("extratoppings", extratoppingJsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "add to cart error" + e.toString());
                }

                Log.e(TAG, "data add to cart" + extratoppingJson);
                addToCartApi(extratoppingJson);
            }
        });

        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Log.e(TAG, "" + selectPosSet);
                ingredientsJsonArray = null;
                for (int i = 0; i < ingredientPojos.size(); i++) {
                    ingridentsListAll.put(ingredientPojos.get(i).getIngredientId(), true);
                }

                ArrayList<Integer> list = new ArrayList<>(selectPosSet);

                Collections.sort(list);
                Log.e(TAG, "" + list);
                for (Integer pos : list)
                    ingridentsListAll.put(ingredientPojos.get(pos).getIngredientId(), false);
                //     Log.e("sorted hashmap ", " " + ingridentsListAll);

                ingredientsJsonArray = new JSONArray();
                for (String key : ingridentsListAll.keySet()) {
                    JSONObject reqObj = new JSONObject();
                    try {
                        if (ingridentsListAll.get(key)) {
                            reqObj.put("i_id", key);
                            reqObj.put("p_id", prodcut_id);
                            ingredientsJsonArray.put(reqObj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.e(TAG, "" + ingredientsJsonArray);
            }
        });

        return rootView;
    }

    private void bindViews() {
        btnAddToCart = (Button) rootView.findViewById(dk.eatmore.demo.R.id.btnAddToCart);

    }

    private void clearSharedPref() {
        SharedPreferences prefs = getActivity().getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    private void PgetroductRecipes() {
        // formar  extratoppingJson

        JSONObject mainRecipesJson = new JSONObject();

        try {
            mainRecipesJson.accumulate("r_token", ApiCall.R_TOKEN);
            mainRecipesJson.accumulate("r_key", ApiCall.R_KEY);
            //  extratoppingJson.accumulate("c_Id","0");

            if (ApplicationClass.getInstance().getPrefManager()
                    .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN))
                mainRecipesJson.accumulate("customer_id", ApplicationClass.getInstance().getPrefManager()
                        .getStringPreferences(MyPreferenceManager.KEY_USER_ID));

//            else
//                mainRecipesJson.accumulate("customer_id", ApplicationClass.getInstance().getPrefManager()
//                        .getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));


            //device id
            mainRecipesJson.accumulate("ip", ApplicationClass.getInstance().getPrefManager()
                    .getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));


            mainRecipesJson.accumulate("p_id", prodcut_id);
            mainRecipesJson.accumulate("p_quantity", prodQuantaty);
            if (productTotalPrice.equalsIgnoreCase("0")
                    || (Double.parseDouble(addToCartProductPrice) > Double.parseDouble(productTotalPrice)))
                mainRecipesJson.accumulate("p_price", addToCartProductPrice);
            else
                mainRecipesJson.accumulate("p_price", productTotalPrice);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        // ingridients
        JSONArray req = null;
        for (int i = 0; i < ingredientPojos.size(); i++) {
            ingridentsListAll.put(ingredientPojos.get(i).getIngredientId(), true);
        }

        if (mFlowLayout.getSelectedList().size() > 0) {
            ArrayList<Integer> list = new ArrayList(mFlowLayout.getSelectedList());
            Collections.sort(list);

            for (Integer pos : list)
                ingridentsListAll.put(ingredientPojos.get(pos).getIngredientId(), false);
            //     Log.e("sorted hashmap ", " " + ingridentsListAll);

            req = new JSONArray();
            for (String key : ingridentsListAll.keySet()) {
                JSONObject reqObj = new JSONObject();
                try {
                    if (ingridentsListAll.get(key)) {
                        reqObj.put("i_id", key);
                        req.put(reqObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            req = new JSONArray();
            for (String key : ingridentsListAll.keySet()) {
                JSONObject reqObj = new JSONObject();
                try {
                    if (ingridentsListAll.get(key)) {
                        reqObj.put("i_id", key);
                        req.put(reqObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            mainRecipesJson.put("ingredients", req);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("mainRecipesJson ", " " + mainRecipesJson);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }

    private void ingredientList() {
        mFlowLayout = (TagFlowLayout) rootView.findViewById(dk.eatmore.demo.R.id.id_flowlayout);
    }

    private void initToolBar() {
//        TextView title = (TextView) rootView.findViewById(R.id.tootlbar_title);
//        title.setVisibility(View.GONE);

        ImageView tool_logo = (ImageView) rootView.findViewById(dk.eatmore.demo.R.id.tool_logo);
//        tool_logo.setVisibility(View.VISIBLE);

        String resImg = ApplicationClass.getInstance().getPrefManager().
                getStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE);

        Picasso.with(getActivity())
                .load(resImg)
                .into(tool_logo);

        TextView resNameTextView = (TextView) rootView.findViewById(dk.eatmore.demo.R.id.tootlbar_title);
        String resName = ApplicationClass.getInstance().getPrefManager().
                getStringPreferences(MyPreferenceManager.RESTAURANT_NAME);
        resNameTextView.setText(resName);

        imgback = (ImageView) rootView.findViewById(dk.eatmore.demo.R.id.imgback);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String[] attribute_list_name_arraay, attribute_list_price_arraay;
    int attribut_title_size = 0;

    View.OnClickListener dialogAttributeClick = new View.OnClickListener() {
        View myView;

        @Override
        public void onClick(View view) {
            //   Toast.makeText(getActivity(),""+view.getId(),Toast.LENGTH_LONG).show();
            try {
                attribut_title_size = productAttributeLists_array.get(view.getId()).
                        getProductAttributeValues().size();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            attribute_list_name_arraay = new String[attribut_title_size];
            attribute_list_price_arraay = new String[attribut_title_size];
            for (int i = 0; i < attribut_title_size; i++) {

                attribute_list_name_arraay[i] = productAttributeLists_array.get(view.getId()).
                        getProductAttributeValues().get(i).getAttrubuteName() + " "
                        + productAttributeLists_array.get(view.getId()).
                        getProductAttributeValues().get(i).getAttributePrice() + " kr";

                attribute_list_price_arraay[i] = productAttributeLists_array.get(view.getId()).
                        getProductAttributeValues().get(i).getAttributePrice();

            }
            //  attribute_list_arr = stringArrayListNAME.toArray(new String[stringArrayListNAME.size()]);

            this.myView = view;

            setDataToDialaog(view, attribute_list_name_arraay, attribute_list_price_arraay,
                    productAttributeLists_array.get(view.getId()).getTitle(),
                    productAttributeLists_array.get(view.getId()).getProductAttributeValues());
        }
    };

    RecyclerView.OnItemTouchListener extratoppingItemTouch = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

    boolean isItemClicked = false;
    boolean[] itemCheckedDefault;
    int selctedDialogId = 0;
    ListView.OnItemClickListener extratoppingItemClick = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(final AdapterView<?> adapterView, final View view, final int pos, long l) {

            final HashMap<String, ExtraToppingCartAddingModel> tempExtraToppingSelectedName = new HashMap<>();
            // extraToppingCartAddingModelsArray.clear();
            final ArrayList<Integer> itemsSelected = new ArrayList();
            final int mainPosition = adapterView.getId();
            final TextView extraToppingList;

            extraToppingList = (TextView) view.findViewById(dk.eatmore.demo.R.id.extraToppingSubList);

            Dialog dialog;
            // Toast.makeText(getActivity(), ""+mainPosition, Toast.LENGTH_SHORT).show();

            if (extraToppingsParentPositionMap.get(mainPosition) == null)
                extraToppingsParentPositionMap.put(mainPosition, 0);
            Log.e(TAG, extraToppingsParentPositionMap.toString());

            final int size = productAttributeLists_array.get(mainPosition).getCategories().get(pos).children.size();
            final String[] items = new String[size];
            final boolean[] itemChecked = new boolean[size];
            itemCheckedDefault = new boolean[size];

            for (int i = 0; i < size; i++) {
                itemChecked[i] = false;
                ExtraToppeningCategory extraToppeningCategory =
                        productAttributeLists_array.get(mainPosition).getCategories().get(pos).children.get(i);
                items[i] = extraToppeningCategory.name + " " + extraToppeningCategory.price + " kr";

                // extraToppeningCategory.getTsg_id();
            }

            final int extraToppingsParentPosition = extraToppingsParentPositionMap.get(mainPosition);
            if (loadArray(mainPosition + "" + extraToppingsParentPosition + "" + pos).length == 0) {
                itemCheckedDefault = itemChecked;
            } else
                itemCheckedDefault = loadArray(mainPosition + "" + extraToppingsParentPosition + "" + pos);

//            if (loadArray(mainPosition + "" + pos).length == 0) {
//                itemCheckedDefault = itemChecked;
//            } else
//                itemCheckedDefault = loadArray(mainPosition + "" + pos);


            for (int dItem = 0; dItem < itemCheckedDefault.length; dItem++) {
                if (itemCheckedDefault[dItem])
                    itemsSelected.add(dItem);
            }

            String extraToppingDialogTitle = productAttributeLists_array.get(mainPosition).getCategories().get(pos).name;
//            padId = productAttributeLists_array.get(mainPosition).getProductAttributeValues()
//                    .get(0).getPadId();
//            Log.e(TAG, "pad_id" + padId);

            //    final ArrayList<Boolean> itemboolena = new ArrayList();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), dk.eatmore.demo.R.style.MultiChoiceDialogTheme);
            View titleview = LayoutInflater.from(getActivity()).inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
            // View titleview = getLayoutInflater().inflate(R.layout.ustom_alert_title, null);
            TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
//            title.setText("Choose Extratopping");
            title.setText(extraToppingDialogTitle);
            builder.setCustomTitle(titleview);
            builder.setMultiChoiceItems(items, itemCheckedDefault,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedItemId,
                                            boolean isSelected) {
                            selctedDialogId = selectedItemId;
                            ExtraToppeningCategory extraToppeningCategory =
                                    productAttributeLists_array.get(mainPosition).getCategories().get(pos).
                                            children.get(selectedItemId);
//                            String pad_id = productAttributeLists_array.get(mainPosition)
//                                    .getProductAttributeValues().get(mainPosition).getPadId();
//                            String pad_id = padId;
//                            if (padId.isEmpty() || previousMainPos != mainPosition)
//                                padId = pad_id = productAttributeLists_array.get(mainPosition)
//                                        .getProductAttributeValues().get(0).getPadId();
//                            previousMainPos = mainPosition;
//                            Log.e(TAG, "pad_id" + pad_id);
                            //price calculation
                            ExtraToppingPriceCalculation extraToppingPriceObject = new ExtraToppingPriceCalculation();
                            extraToppingPriceObject.setPrice(extraToppeningCategory.price);
                            extraToppingPriceObject.setSelected(isSelected);
                            extraToppingPriceCalculation.put("" + adapterView.getId() + pos + selctedDialogId,
                                    extraToppingPriceObject);

                            //json formation for add to cart
                            ExtraToppingCartAddingModel extraToppingCartAddingModel = new ExtraToppingCartAddingModel();
                            extraToppingCartAddingModel.setTsgd_id(extraToppeningCategory.tsgd_id);
                            extraToppingCartAddingModel.setTsg_id(extraToppeningCategory.tsg_id);
                            extraToppingCartAddingModel.setId(extraToppeningCategory.id);
                            extraToppingCartAddingModel.setTm_id(extraToppeningCategory.tm_id);
                            extraToppingCartAddingModel.setPrice(extraToppeningCategory.price);
                            extraToppingCartAddingModel.setName(extraToppeningCategory.name);
//                            extraToppingCartAddingModel.setPad_id(pad_id);
                            extraToppingCartAddingModel.setPad_id(extraToppeningCategory.getPad_id());
                            extraToppingCartAddingModel.setSelected(isSelected);

//                            extraToppingSelectedName.put("" + adapterView.getId() + pos + selctedDialogId,
//                                    extraToppingCartAddingModel);

                            tempExtraToppingSelectedName.put("" + adapterView.getId() + pos + selctedDialogId,
                                    extraToppingCartAddingModel);


                            if (isSelected) {
                                itemCheckedDefault[selectedItemId] = true;
                                isItemClicked = true;
                                itemsSelected.add(selectedItemId);

                            } else if (itemsSelected.contains(selectedItemId)) {
                                //     Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
                                itemChecked[selectedItemId] = false;
                                isItemClicked = true;
                                itemsSelected.remove(Integer.valueOf(selctedDialogId));
                                extraToppingSelectedName.remove("" + adapterView.getId() + pos + selctedDialogId);
                            }
                        }
                    })
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            int tempSize = tempExtraToppingSelectedName.size();
                            for (int i = 0; i < tempSize; i++) {
                                extraToppingSelectedName.putAll(tempExtraToppingSelectedName);
                            }

//                            padId = "";
                            Log.e("Done ", "" + extraToppingPriceCalculation);
                            if (isItemClicked) {
                                // TODO: 3/17/2017 Add HM to Arraylist
                                HashMap<String, Integer> mExtraToppings = new HashMap<>();
                                String extraToppingsParents = mainPosition + "" + extraToppingsParentPosition;
                                mExtraToppings.put(extraToppingsParents, pos);
                                extraToppingsToRemove.add(mExtraToppings);

                                storeArray(itemCheckedDefault, extraToppingsParents + "" + pos);
                            }

                            calculatePrice();

                            //   Log.e("Dialog ", "" + mainPosition + "" + pos + "_size");
                            StringBuilder result = new StringBuilder();
                            for (int eName : itemsSelected) {
                                //  itemChecked[eName] = true;
                                result.append(items[eName]);
                                result.append(", ");
                            }

                            extraToppingList.setText(result.length() > 0 ? result.substring(0, result.length() - 1) : "");

                        }
                    })
                    .setNegativeButton(getString(dk.eatmore.demo.R.string.Cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            dialog = builder.create();
            dialog.show();
        }
    };

    ListView.OnItemClickListener onlyExtraToppingClick = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(final AdapterView<?> adapterView, final View view, final int pos, long l) {

            // extraToppingCartAddingModelsArray.clear();
            final ArrayList<Integer> itemsSelected = new ArrayList();
            final int mainPosition = adapterView.getId();
            final TextView extraToppingList;

            extraToppingList = (TextView) view.findViewById(dk.eatmore.demo.R.id.extraToppingSubList);

            Dialog dialog;
            if (extraToppingsParentPositionMap.get(mainPosition) == null)
                extraToppingsParentPositionMap.put(mainPosition, 0);
            Log.e(TAG, extraToppingsParentPositionMap.toString());

            int size = categories.get(pos).children.size();
            final String[] items = new String[size];
            final boolean[] itemChecked = new boolean[size];
            itemCheckedDefault = new boolean[size];

            for (int i = 0; i < size; i++) {
                itemChecked[i] = false;
                ExtraToppeningCategory extraToppeningCategory =
                        categories.get(pos).children.get(i);
                items[i] = extraToppeningCategory.name + " " + extraToppeningCategory.price + " kr";

                // extraToppeningCategory.getTsg_id();
            }

            final int extraToppingsParentPosition = extraToppingsParentPositionMap.get(mainPosition);
            if (loadArray(mainPosition + "" + extraToppingsParentPosition + "" + pos).length == 0) {
                itemCheckedDefault = itemChecked;
            } else
                itemCheckedDefault = loadArray(mainPosition + "" + extraToppingsParentPosition + "" + pos);

//            if (loadArray(mainPosition + "" + pos).length == 0) {
//                itemCheckedDefault = itemChecked;
//            } else
//                itemCheckedDefault = loadArray(mainPosition + "" + pos);


            for (int dItem = 0; dItem < itemCheckedDefault.length; dItem++) {

                if (itemCheckedDefault[dItem])
                    itemsSelected.add(dItem);

            }
            String extraToppingDialogTitle = categories.get(pos).name;


            //    final ArrayList<Boolean> itemboolena = new ArrayList();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), dk.eatmore.demo.R.style.MultiChoiceDialogTheme);
            View titleview = LayoutInflater.from(getActivity()).inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
            // View titleview = getLayoutInflater().inflate(R.layout.ustom_alert_title, null);
            TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
//             title.setText("Choose Extratopping");
            title.setText(extraToppingDialogTitle);
            builder.setCustomTitle(titleview);
//            builder.setTitle(extraToppingDialogTitle);
            builder.setMultiChoiceItems(items, itemCheckedDefault,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedItemId,
                                            boolean isSelected) {
                            selctedDialogId = selectedItemId;
                            ExtraToppeningCategory extraToppeningCategory =
                                    categories.get(pos).
                                            children.get(selectedItemId);

                            //price calculation
                            ExtraToppingPriceCalculation extraToppingPriceObject = new ExtraToppingPriceCalculation();
                            extraToppingPriceObject.setPrice(extraToppeningCategory.price);
                            extraToppingPriceObject.setSelected(isSelected);
                            extraToppingPriceCalculation.put("" + adapterView.getId() + pos + selctedDialogId,
                                    extraToppingPriceObject);

                            //json formation for add to cart
                            ExtraToppingCartAddingModel extraToppingCartAddingModel = new ExtraToppingCartAddingModel();
                            extraToppingCartAddingModel.setTsgd_id(extraToppeningCategory.tsgd_id);
                            extraToppingCartAddingModel.setTsg_id(extraToppeningCategory.tsg_id);
                            extraToppingCartAddingModel.setId(extraToppeningCategory.id);
                            extraToppingCartAddingModel.setTm_id(extraToppeningCategory.tm_id);
                            extraToppingCartAddingModel.setPrice(extraToppeningCategory.price);
                            extraToppingCartAddingModel.setName(extraToppeningCategory.name);
                            extraToppingCartAddingModel.setSelected(isSelected);

                            extraToppingSelectedName.put("" + adapterView.getId() + pos + selctedDialogId,
                                    extraToppingCartAddingModel);


                            if (isSelected) {
                                itemCheckedDefault[selectedItemId] = true;
                                isItemClicked = true;
                                itemsSelected.add(selectedItemId);

                            } else if (itemsSelected.contains(selectedItemId)) {
                                //     Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();

                                itemChecked[selectedItemId] = false;
                                isItemClicked = true;
                                itemsSelected.remove(Integer.valueOf(selctedDialogId));
                            }
                        }
                    })
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Log.e("Done ", "" + extraToppingPriceCalculation);
                            if (isItemClicked) {
                                HashMap<String, Integer> mExtraToppings = new HashMap<>();
                                String extraToppingsParents = mainPosition + "" + extraToppingsParentPosition;
                                mExtraToppings.put(extraToppingsParents, pos);
                                extraToppingsToRemove.add(mExtraToppings);

                                storeArray(itemCheckedDefault, mainPosition + "" + extraToppingsParentPosition + "" + pos);
                            }

                            calculatePrice();

                            //   Log.e(TAG, "" + mainPosition + "" + pos + "_size");
                            StringBuilder result = new StringBuilder();
                            for (int eName : itemsSelected) {
                                //  itemChecked[eName] = true;
                                result.append(items[eName]);
                                result.append(",");
                            }

                            extraToppingList.setText(result.length() > 0 ? result.substring(0, result.length() - 1) : "");
                        }
                    })
                    .setNegativeButton(getString(dk.eatmore.demo.R.string.Cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            dialog = builder.create();
            dialog.show();
        }
    };

    public boolean storeArray(boolean[] array, String arrayName) {
        SharedPreferences prefs = getActivity().getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);

        for (int i = 0; i < array.length; i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);

        return editor.commit();
    }

    public boolean[] loadArray(String arrayName) {
        SharedPreferences prefs = getActivity().getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        boolean array[] = new boolean[size];
        for (int i = 0; i < size; i++)
            array[i] = prefs.getBoolean(arrayName + "_" + i, false);

        return array;
    }


    // int dialogPos = 0;
    int viewId = 0;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private void setDataToDialaog(final View myView, final String[] productAttributeList, String[] productAttributePrice,
                                  String dialogTitle,
                                  final ArrayList<ProductAttributeValue> arrayAttribute) {
        final AlertDialog.Builder chooseDrinkdialog = new AlertDialog.Builder(
                getActivity(), dk.eatmore.demo.R.style.CheckBoxTintTheme);

        View titleview = getActivity().getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
        TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
        title.setText(dialogTitle);
        chooseDrinkdialog.setCustomTitle(titleview);
        viewId = myView.getId();

        prefs = getActivity().getSharedPreferences("preferencename", 0);

        chooseDrinkdialog.setSingleChoiceItems(productAttributeList, prefs.getInt("DilogPos", 0),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        setDatatoView(myView, productAttributeList[item]);

                        editor = prefs.edit();
                        editor.putInt("DilogPos" + item, item);
                        editor.commit();

                        Log.e(TAG, extraToppingsParentPositionMap.toString());
                        extraToppingsParentPositionMap.put(viewId, item);
                        Log.e(TAG, extraToppingsParentPositionMap.toString());

                        int extraToppingsToRemoveSize = extraToppingsToRemove.size();
                        for (int i = 0; i < extraToppingsToRemoveSize; i++) {
                            HashMap<String, Integer> mExtraToppings = extraToppingsToRemove.get(i);
                            String extraToppingsParents = viewId + "" + item;
                            if (mExtraToppings.get(extraToppingsParents) != null) {
                                int extraToppingPos = mExtraToppings.get(extraToppingsParents);

                                editor.remove(extraToppingsParents + "" + extraToppingPos + "_size");
                            }
                        }
                        editor.apply();

                        AttributeCartAddingModel attributeCartAddingModel = new AttributeCartAddingModel();
                        attributeCartAddingModel.setTm_id(productAttributeLists_array.get(viewId).getProductAttributeValues().get(item).getTmId());
                        attributeCartAddingModel.setPad_id(productAttributeLists_array.get(viewId).getProductAttributeValues().get(item).getPadId());
                        attributeCartAddingModel.setPam_id(productAttributeLists_array.get(viewId).getProductAttributeValues().get(item).getPamId());
                        attributeCartAddingModel.setA_value(productAttributeLists_array.get(viewId).getProductAttributeValues().get(item).getAttrubuteName());
                        attributeCartAddingModel.setA_price(productAttributeLists_array.get(viewId).getProductAttributeValues().get(item).getAttributePrice());
//
//                        attributeListHashMap.put(productAttributeLists_array.get(viewId).getProductAttributeValues().get(item).getPamId(),
//                                attributeCartAddingModel);

                        attributeListHashMap.put(viewId, attributeCartAddingModel);
                        padId = attributeCartAddingModel.getPad_id();

                        attributePriceCalculation.put(viewId,
                                productAttributeLists_array.get(viewId).getProductAttributeValues().get(item).getAttributePrice());

                        calculatePrice();

                        extraToppingHashMap.put("" + viewId,
                                productAttributeLists_array.get(viewId).getProductAttributeValues().get(item).getTmId());

                        try {
                            JSONObject attributeJson = new JSONObject();

                            JSONArray array = new JSONArray();

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("tm_id", productAttributeLists_array.get(viewId).
                                    getProductAttributeValues().get(item).getTmId());

                            array.put(jsonObject);

                            attributeJson.put("tm_ids", array);
                            attributeJson.put("r_token", ApiCall.R_TOKEN);
                            attributeJson.put("r_key", ApiCall.R_KEY);

//                            RecyclerView rview= (RecyclerView) recyclerView_extraTopping.findViewById(viewId);
                            LinearLayout dynaParent = (LinearLayout) myView.getParent();
                            LinearLayout recViewContainer = (LinearLayout) dynaParent.getChildAt(2);

                            TextView text = (TextView) recViewContainer.getChildAt(1);
                            ListView rview = (ListView) recViewContainer.getChildAt(2);

                            getExpand(rview, text, attributeJson, viewId, arrayAttribute);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                });

        chooseDrinkdialog.show();
    }

    private void calculatePrice() {

        // Log.e(TAG,"extraTopping "+extraToppingStringListHashMap.toString());
        Double extraToppingPrice = 0.0;
        String extraToppingPriceKey = "0.0";

        DefaultProductPrice = 0.0;

        for (int key : attributePriceCalculation.keySet()) {
            String price = attributePriceCalculation.get(key);

            //    Log.e(TAG, "DefaultProductPrice" + price);

            DefaultProductPrice += Double.parseDouble(price);
            //   Log.e(TAG, "DefaultProductPrice" + DefaultProductPrice);
        }

        for (String key : extraToppingPriceCalculation.keySet()) {

            ExtraToppingPriceCalculation extraTopping = extraToppingPriceCalculation.get(key);

            if (extraTopping.isSelected()) {
                extraToppingPriceKey = extraTopping.getPrice();
                extraToppingPrice = extraToppingPrice + Double.parseDouble(extraToppingPriceKey);
                Log.e("if  ", "final " + extraToppingPrice);
            }

        }

        DefaultProductPrice = extraToppingPrice + DefaultProductPrice +
                Double.parseDouble(productTotalPrice);
        addToCartProductPrice = "" + formatValueToMoney(DefaultProductPrice);

//        productTotalTextView.setText(convertCurrencyToDkk((100 * DefaultProductPrice * mCounter)) + " kr");
//        productTotalTextView.setText(formatValueToMoney((DefaultProductPrice * mCounter)) + " kr");
        productTotalTextView.setText(convertCurrencyToDanish((DefaultProductPrice * mCounter)) + " kr");
    }

    private void includeLayoutDynamically(ArrayList<ProductAttributeList> productAttributeLists_array) {

        LinearLayout rl = (LinearLayout) rootView.findViewById(dk.eatmore.demo.R.id.ll_dynamic_layout_container);
        // inflate content layout and add it to the relative layout as second child
        // add as second child, therefore pass index 1 (0,1,...)
        LayoutInflater layoutInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < productAttributeLists_array.size(); i++) {

            View dynamicView = layoutInflater.inflate(dk.eatmore.demo.R.layout.dynamic_content, null, false);
            rl.addView(dynamicView, i + 2);

            LinearLayout attributeBorderLayout =
                    (LinearLayout) dynamicView.findViewById(dk.eatmore.demo.R.id.attributeBorderLayout);

            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(4, Color.parseColor("#019E0F"));
            drawable.setCornerRadius(2);
            //  drawable.setColor(Color.BLUE);
            attributeBorderLayout.setBackgroundDrawable(drawable);

            cooseSizeText = (TextView) dynamicView.findViewById(dk.eatmore.demo.R.id.cooseSizeText);
            //  cooseSizeText.setText(valueSize[0]);
            cooseSizeText.setText(productAttributeLists_array.get(i).
                    getProductAttributeValues().get(0).getAttrubuteName()
                    + " " +
                    productAttributeLists_array.get(i).
                            getProductAttributeValues().get(0).getAttributePrice() + " kr");
            //  getActivity(),productAttributeLists_array.get(i).getProductAttributeValues().get(i).getAttrubuteName();
            cooseSizeText.setId(i);

            atributeTitle = (TextView) dynamicView.findViewById(dk.eatmore.demo.R.id.atributeTitle);
            atributeTitle.setVisibility(View.VISIBLE);
            atributeTitle.setText(productAttributeLists_array.get(i).getTitle());


            sizell = (LinearLayout) dynamicView.findViewById(dk.eatmore.demo.R.id.chosseSize);
            sizell.setId(i);
            sizell.setOnClickListener(dialogAttributeClick);

//            View button_prodt_bar = dynamicView.findViewById(R.id.button_prodt_bar);
//            View list_prodt_bar = dynamicView.findViewById(R.id.list_prodt_bar);
            extratopping_title_indi = (TextView) dynamicView.findViewById(dk.eatmore.demo.R.id.extratopping_title_indi);
            // extratopping_title_indi.setVisibility(View.VISIBLE);
            recyclerView = (RecyclerView) dynamicView.findViewById(dk.eatmore.demo.R.id.my_recycler_view);
            recyclerView.setId(i);
            recyclerView.setClickable(true);


            recyclerView_extraTopping = (ListView) dynamicView.findViewById(dk.eatmore.demo.R.id.recycler_view_extraTopping);
            recyclerView_extraTopping.setId(i);
            recyclerView_extraTopping.setOnItemClickListener(extratoppingItemClick);

//            extraToppingRecyclerView = (RecyclerView) dynamicView.findViewById(R.id.rv_extra_toppings);
//            extraToppingRecyclerView.setHasFixedSize(true);
//            RecyclerView.LayoutManager extraToppingLayoutManager = new LinearLayoutManager(getActivity());
//            extraToppingRecyclerView.setLayoutManager(extraToppingLayoutManager);
//            extraToppingRecyclerView.addOnItemTouchListener(extratoppingItemTouch);

            if (productAttributeLists_array.get(i).getDisplay_type().equals("1")) {
                sizell.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                //  button_prodt_bar.setVisibility(View.VISIBLE);
                //  list_prodt_bar.setVisibility(View.GONE);
            } else {
                sizell.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                // button_prodt_bar.setVisibility(View.GONE);
                // list_prodt_bar.setVisibility(View.VISIBLE);
            }

            //    recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(layoutManager);


            // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            attributeAdapter = new AttributeAdapter(getActivity(),
                    productAttributeLists_array.get(i).getProductAttributeValues(), attributeInterface, recyclerView_extraTopping,
                    extratopping_title_indi, i);

            recyclerView.setAdapter(attributeAdapter);

            setDefaultExtraTopping(recyclerView_extraTopping, productAttributeLists_array.get(i).getCategories());

        }

    }

    private void includeExtraToppingOnly(ProductAttributeList attribute_list) {
        LinearLayout rl = (LinearLayout) rootView.findViewById(dk.eatmore.demo.R.id.ll_dynamic_layout_container);
        // inflate content layout and add it to the relative layout as second child
        // add as second child, therefore pass index 1 (0,1,...)
        LayoutInflater layoutInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dynamicView = layoutInflater.inflate(dk.eatmore.demo.R.layout.dynamic_content, null, false);
        rl.addView(dynamicView, 2);

        extratopping_title_indi = (TextView) dynamicView.findViewById(dk.eatmore.demo.R.id.extratopping_title_indi);

        //if(attribute_list.getCategories().size()>0)
        // extratopping_title_indi.setVisibility(View.VISIBLE);
        recyclerView_extraTopping = (ListView) dynamicView.findViewById(dk.eatmore.demo.R.id.recycler_view_extraTopping);
        recyclerView_extraTopping.setOnItemClickListener(onlyExtraToppingClick);

        setOnlyExtraTopping(recyclerView_extraTopping, attribute_list.getCategories());
    }

    //attribute  grid click
    AttributeInterface attributeInterface = new AttributeInterface() {
        @Override
        public void attributeClick(int position, ListView expandableListView, String tm_id,
                                   AttributeCartAddingModel attributeCartAddingModel,
                                   TextView ectratopping_ind, int parentPos, ArrayList<ProductAttributeValue> productAttributeLists) {

            // Toast.makeText(getActivity(), "  "+parentPos, Toast.LENGTH_SHORT).show();

            attributeListHashMap.put(parentPos, attributeCartAddingModel);

            attributePriceCalculation.put(parentPos, attributeCartAddingModel.getA_price());

            padId = attributeCartAddingModel.getPad_id();
            Log.e(TAG, "padId: " + padId);

            calculatePrice();

            Log.e(TAG, extraToppingsParentPositionMap.toString());
            extraToppingsParentPositionMap.put(parentPos, position);
            Log.e(TAG, extraToppingsParentPositionMap.toString());

            SharedPreferences prefs = getActivity().getSharedPreferences("preferencename", 0);
            SharedPreferences.Editor editor = prefs.edit();

            int extraToppingsToRemoveSize = extraToppingsToRemove.size();
            for (int i = 0; i < extraToppingsToRemoveSize; i++) {
                HashMap<String, Integer> mExtraToppings = extraToppingsToRemove.get(i);
                String extraToppingsParents = parentPos + "" + position;
                if (mExtraToppings.get(extraToppingsParents) != null) {
                    int extraToppingPos = mExtraToppings.get(extraToppingsParents);

                    editor.remove(extraToppingsParents + "" + extraToppingPos + "_size");
                }
            }
            editor.apply();

//            SharedPreferences prefs = getActivity().getSharedPreferences("preferencename", 0);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.remove(parentPos + "" + position + "_size");
//            //  editor.clear();
//            editor.apply();

            try {
                JSONObject attributeJson = new JSONObject();
                JSONArray array = new JSONArray();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tm_id", tm_id);
                array.put(jsonObject);

                attributeJson.put("tm_ids", array);
                attributeJson.put("r_token", ApiCall.R_TOKEN);
                attributeJson.put("r_key", ApiCall.R_KEY);

                Log.e(TAG, "attributeJson" + attributeJson);

                // Extra Toppings Expand
                getExpand(expandableListView, ectratopping_ind, attributeJson, parentPos, productAttributeLists);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void setDatatoView(View view, String text) {
        LinearLayout ll = (LinearLayout) view;
        TextView childTextView = (TextView) ll.getChildAt(0);
        childTextView.setText(text);
    }

    Double DefaultProductPrice;

    private void getProductDesc() {
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();

        DefaultProductPrice = 0.0;
        ingredientPojos = new ArrayList<>();
        ingridentsListAll = new HashMap<>();
        String tag_string_req = "storePayment";

        NetworkUtils.getProductDescription(prodcut_id, tag_string_req, ProductDescriptionFragment.this);
    }

    @Override
    public void getProductDescriptionCallbackSuccess(String success) {
        mDialog.dismiss();
        //           Log.e(TAG, "PRODUCT_DETAIL_BY_ID Response: " + response.toString());
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
                JSONObject jsonObject = jObj.getJSONObject("data");

//                ProductDetails mProductDetails = new Gson().fromJson(String.valueOf(jsonObject), ProductDetails.class);
//                Log.e(TAG, "" + mProductDetails);

                // JSONObject jsonObject new JSONObject();
                productName = jsonObject.getString("p_name");
                // JSONArray jsonArray = jObj.getJSONArray("data");
                //  JSONObject jsonObject = jsonArray.getJSONObject(0);
                productIdName.setText(jsonObject.getString("product_no") + " " + jsonObject.getString("p_name"));
                productTotalPrice = jsonObject.getString("p_price");

                if (jsonObject.isNull("p_price"))
                    productTotalPrice = "0";
                //      productTotalTextView.setText("Price: " + productTotalPrice);

                isAttrubueAvailable = jsonObject.getString("is_attributes");

                JSONArray jsonIngredentsarray = jsonObject.getJSONArray("product_ingredients");
                int arraySize = jsonIngredentsarray.length();

                mVals = new String[arraySize];
                for (int item = 0; item < arraySize; item++) {
                    ingredientsText.setVisibility(View.VISIBLE);

                    JSONObject jsoningredents = jsonIngredentsarray.getJSONObject(item);
                    ingredients = jsoningredents.getString("i_id");
                    ingredientsName = jsoningredents.getString("i_name");

                    IngredientPojo ingredientPojo = new IngredientPojo();
                    ingredientPojo.setIngredientId(ingredients);
                    ingredientPojo.setIngredientName(ingredientsName);

                    ingredentsMap.put(ingredients, ingredientsName);
                    ingredientPojos.add(ingredientPojo);
                    mVals[item] = ingredientsName;
                }

                mFlowLayout.setAdapter(mAdapter = new TagAdapter<String>(mVals) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(dk.eatmore.demo.R.layout.ingredients_selct_layout,
                                mFlowLayout, false);
                        tv.setText(s);
                        return tv;
                    }
                });

                for (int j = 0; j < mVals.length; j++) {
                    mAdapter.setSelectedList(j);
                }

                adapter = new IngredientsAdapterView(ingredientPojos, getActivity());

                //listView.setAdapter(adapter);
                adapter.SetOnItemClickListener(new IngredientsAdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        CheckBox mycheckBox = (CheckBox) view.findViewById(dk.eatmore.demo.R.id.checkBox);

                        if (mycheckBox.isChecked()) {
                            //  Toast.makeText(getActivity(), "un- selected"+ingredientPojos.get(position).getIngredientId(), Toast.LENGTH_SHORT).show();
                            mycheckBox.setChecked(false);
                            ingredentsMap.remove(ingredientPojos.get(position).getIngredientId());
                        } else {
                            //   Toast.makeText(getActivity(), "selected", Toast.LENGTH_SHORT).show();
                            mycheckBox.setChecked(true);
                            ingredentsMap.put(ingredientPojos.get(position).getIngredientId(), ingredientPojos.get(position).getIngredientName());
                        }
                    }
                });


                if (isAttrubueAvailable.equals("1")) {

                    productTotalPrice = "0";

                    ArrayList<ProductAttributeValue> productAttributeValues1 = null;
                    JSONArray product_attribute_list = jsonObject.getJSONArray("product_attribute_list");

                    int size = product_attribute_list.length();

                    for (int item = 0; item < size; item++) {
                        JSONObject jsonproduct_attribute_list = product_attribute_list.getJSONObject(item);
                        ProductAttributeList attribute_list = new ProductAttributeList();
                        attribute_list.setTitle(jsonproduct_attribute_list.getString("a_name"));
                        attribute_list.setDisplay_type(jsonproduct_attribute_list.getString("display_type"));
                        productAttributeValues1 = new ArrayList<>();


                        //getting product attribute name and price
                        JSONArray attrubutitemArray = jsonproduct_attribute_list.
                                getJSONArray("product_attribute_value");

                        int attrubutitemArraySize = attrubutitemArray.length();
                        for (int attrubutitem = 0; attrubutitem < attrubutitemArraySize; attrubutitem++) {
                            JSONObject jsonAttributeValue = attrubutitemArray.getJSONObject(attrubutitem);

                            ProductAttributeValue productAttributeValue = new ProductAttributeValue();
                            productAttributeValue.setPadId(jsonAttributeValue.getString("pad_id"));
                            productAttributeValue.setPamId(jsonAttributeValue.getString("pam_id"));
                            productAttributeValue.setTmId(jsonAttributeValue.getString("tm_id"));
                            productAttributeValue.setAttrubuteName(jsonAttributeValue.getString("a_value"));
                            productAttributeValue.setAttributePrice(jsonAttributeValue.getString("a_price"));
                            productAttributeValues1.add(productAttributeValue);
                        }
                        attribute_list.setProductAttributeValues(productAttributeValues1);


                        //    if (!defaultJsonData.isNull("tm_id")) {

                        //default attribute selection for cart check out
                        AttributeCartAddingModel defaultattribute = new AttributeCartAddingModel();

                        if (!jsonproduct_attribute_list.isNull("default_attribute_value")) {
                            JSONObject defaultJsonDataSelection = jsonproduct_attribute_list.
                                    getJSONObject("default_attribute_value");

//                                if (defaultJsonDataSelection != null) {
                            defaultattribute.setTm_id(defaultJsonDataSelection.getString("tm_id"));
                            defaultattribute.setPad_id(defaultJsonDataSelection.getString("pad_id"));
                            defaultattribute.setPam_id(defaultJsonDataSelection.getString("pam_id"));
                            defaultattribute.setA_value(defaultJsonDataSelection.getString("a_value"));
                            String aPeice = defaultJsonDataSelection.getString("a_price");

                            defaultattribute.setA_price(aPeice);

                            attributeListHashMap.put(item, defaultattribute);

                            //for price only
                            attributePriceCalculation.put(item, aPeice);

                            JSONObject defaultJsonData = jsonproduct_attribute_list.
                                    getJSONObject("default_attribute_value");

                            if (!defaultJsonData.isNull("tm_id")) {
                                JSONObject data = defaultJsonData.getJSONObject("extra_topping_group_deatils");
                                attribute_list.setCategories(ExtraToppeningCategory.getCategoriesWithJson(data));
                            }

                            Log.e(TAG, "attribue Pos  " + item);
                            productAttributeLists_array.add(attribute_list);
                        }

                    }

                    includeLayoutDynamically(productAttributeLists_array);
                } else {
                    ProductAttributeList attribute_list = new ProductAttributeList();

//                            attribute_list.setCategories(ExtraToppeningCategory.getCategories(jsonObject.
//                                    getJSONArray("extra_topping_group_deatils")));
                    String dataString = checkNull(jsonObject, "extra_topping_group_deatils");
                    if (dataString != null && !dataString.equalsIgnoreCase("")) {
                        JSONObject data = new JSONObject(dataString);
//                            JSONObject data = jsonObject.getJSONObject("extra_topping_group_deatils");

                        attribute_list.setCategories(ExtraToppeningCategory.getCategoriesWithJson(data));
                        includeExtraToppingOnly(attribute_list);
                    }
                }

                for (int key : attributePriceCalculation.keySet()) {
                    String price = attributePriceCalculation.get(key);
                    DefaultProductPrice += Double.parseDouble(price);

                }
                DefaultProductPrice = DefaultProductPrice + Double.parseDouble(productTotalPrice);
                addToCartProductPrice = formatValueToMoney(DefaultProductPrice);
//                        productTotalTextView.setText(convertCurrencyToDkk(DefaultProductPrice) + " kr");
//                        productTotalTextView.setText(formatValueToMoney(DefaultProductPrice) + " kr");
                productTotalTextView.setText(convertCurrencyToDanish(DefaultProductPrice) + " kr");
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSONException" + e.toString());
            // JSON error
            // hideDialog();
            e.printStackTrace();
            //   Toast.makeText(NewCheckoutActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void getProductDescriptionCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        Log.e(TAG, "storePayment Error: " + volleyError.getMessage());
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableProductDesc();
        }
    }

    public void showDialogInternetUnavailableProductDesc() {
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
                getProductDesc();
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

    public void setOnlyExtraTopping(ListView defaultExtraTopping,
                                    ArrayList<ExtraToppeningCategory> mcategories) {

        this.categories = mcategories;
        Log.e(TAG, "ExtraTopping" + categories);
        attributeExtratoppingAdapter = new ExtraToppingSubGroup(categories, getActivity());

        defaultExtraTopping.setAdapter(attributeExtratoppingAdapter);
        attributeExtratoppingAdapter.notifyDataSetChanged();
        ExpandableListUtility.setListViewHeightBasedOnChildren(defaultExtraTopping);
    }

    public void setDefaultExtraTopping(ListView defaultExtraTopping,
                                       ArrayList<ExtraToppeningCategory> mcategories) {

        this.categories = mcategories;
        Log.e(TAG, "ExtraTopping" + categories);
        attributeExtratoppingAdapter = new ExtraToppingSubGroup(categories, getActivity());

        defaultExtraTopping.setAdapter(attributeExtratoppingAdapter);
        attributeExtratoppingAdapter.notifyDataSetChanged();
        ExpandableListUtility.setListViewHeightBasedOnChildren(defaultExtraTopping);
    }

    private void getExpand(final ListView expndnd, final TextView ectratopping_ind,
                           final JSONObject jsonObject, final int parentPos,
                           final ArrayList<ProductAttributeValue> arrayAttribute) {
        mDialog.show();
        categories = new ArrayList<>();
        // Toast.makeText(getActivity(), ""+parentPos, Toast.LENGTH_SHORT).show();

        String tag_string_req = "EXTRA_TOPPING_DETAILS";


        NetworkUtils.getProductExtraToppings(jsonObject, expndnd, ectratopping_ind, parentPos,
                arrayAttribute, tag_string_req, ProductDescriptionFragment.this);
        //   Log.e(TAG, "JSONObject" + jsonObject.toString());
        //   Log.e(TAG,"addToCartApi"+jsonData);
//        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST,
//                ApiCall.EXTRATOPPING_DETAILS, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jObj) {
//                mDialog.dismiss();
//                //     categories.clear();
//                //    Log.e(TAG, "EXTRA_TOPPING_DETAILS Response: " + jObj.toString());
//                try {
//                    // JSONObject jObj = new JSONObject(response);
//                    boolean status = jObj.getBoolean("status");
//                    // Check for error node in json
//                    if (status) {
//
//                        ectratopping_ind.setVisibility(View.GONE);
//                        JSONArray jsonArray = jObj.getJSONArray("data");
//
//                        categories = ExtraToppeningCategory.getCategories(jsonArray, padId);
//                    } else {
//                        ectratopping_ind.setVisibility(View.GONE);
//                        //  categories.clear();
//                    }
//
//                    Log.e(TAG, "categories param " + categories);
//
//
//                    ProductAttributeList attribute_list = new ProductAttributeList();
//                    attribute_list.setProductAttributeValues(arrayAttribute);
//                    attribute_list.setCategories(categories);
//
//                    productAttributeLists_array.set(parentPos, attribute_list);
//                    Log.e(TAG, "ExtraTopping" + categories);
//                    attributeExtratoppingAdapter = new ExtraToppingSubGroup(categories, getActivity());
//
//                    expndnd.setAdapter(attributeExtratoppingAdapter);
//
//                    ExpandableListUtility.setListViewHeightBasedOnChildren(expndnd);
//                    attributeExtratoppingAdapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        mDialog.dismiss();
//                        Log.e(TAG, "EXTRA_TOPPING_DETAILS Error: " + error.getMessage());
//                    }
//                }
//        );
//        // Adding request to volley request queue
//        ApplicationClass.getInstance().addToRequestQueue(jsonReq, tag_string_req);
    }

    @Override
    public void getProductExtraToppingsCallbackSuccess(JSONObject success, ListView expndnd,
                                                       TextView ectratopping_ind, int parentPos,
                                                       ArrayList<ProductAttributeValue> arrayAttribute) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        //     categories.clear();
        //    Log.e(TAG, "EXTRA_TOPPING_DETAILS Response: " + success.toString());
        try {
            // JSONObject success = new JSONObject(response);
            boolean status = success.getBoolean("status");
            // Check for error node in json
            if (status) {

                ectratopping_ind.setVisibility(View.GONE);
                JSONArray jsonArray = success.getJSONArray("data");

                categories = ExtraToppeningCategory.getCategories(jsonArray, padId);
            } else {
                ectratopping_ind.setVisibility(View.GONE);
                //  categories.clear();
            }

            Log.e(TAG, "categories param " + categories);

            ProductAttributeList attribute_list = new ProductAttributeList();
            attribute_list.setProductAttributeValues(arrayAttribute);
            attribute_list.setCategories(categories);

            productAttributeLists_array.set(parentPos, attribute_list);
            Log.e(TAG, "ExtraTopping" + categories);
            attributeExtratoppingAdapter = new ExtraToppingSubGroup(categories, getActivity());

            expndnd.setAdapter(attributeExtratoppingAdapter);

            ExpandableListUtility.setListViewHeightBasedOnChildren(expndnd);
            attributeExtratoppingAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getProductExtraToppingsCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        Log.e(TAG, "EXTRA_TOPPING_DETAILS Error: " + volleyError.getMessage());
    }

    public void addToCartApi(final JSONObject jsonData) {
        mDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mDialog.show();

        String reqTag = "AddToCart";
        NetworkUtils.callAddToCart(jsonData, reqTag, ProductDescriptionFragment.this);
    }

    @Override
    public void addToCartCallbackSuccess(JSONObject success) {
        boolean isUserDeleted = false;
        try {
            if (mDialog != null && mDialog.isShowing())
                mDialog.dismiss();
            Log.e(TAG, "response" + success);
            if (ApplicationClass.getInstance().getPrefManager()
                    .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN))
                if (success.has("is_user_deleted"))
                    isUserDeleted = success.getBoolean("is_user_deleted");

            if (!isUserDeleted) {
                if (success.getBoolean("status") &&
                        success.getBoolean("is_restaurant_closed")) {
                    String msg = success.getString("msg");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View view = inflater.inflate(dk.eatmore.demo.R.layout.dilaog_info_layout, null);

                    TextView textViewTitle = (TextView) view.findViewById(dk.eatmore.demo.R.id.textviewinfoTitle);
                    Button butonOk = (Button) view.findViewById(dk.eatmore.demo.R.id.buttonAaddtocartOk);
                    TextView dialogText = (TextView) view.findViewById(dk.eatmore.demo.R.id.addedtocartText);
                    textViewTitle.setText("Restaurant is closed");
                    builder.setView(view);
                    dialogText.setTypeface(null, Typeface.BOLD);
                    dialogText.setText(msg);

                    final AlertDialog forgotDialog = builder.create();
                    forgotDialog.show();


                    butonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forgotDialog.dismiss();
                        }
                    });

                } else if (success.getBoolean("status")) {
                    prodQuantaty = success.getString("cartcnt");

                    Toast.makeText(getActivity(), productName + " " + getString(dk.eatmore.demo.R.string.added_to_cart), Toast.LENGTH_SHORT).show();

                    try {
                        getActivity().getSupportFragmentManager().popBackStack();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    Intent intent = new Intent("CartCount");
                    // You can also include some extra data.
                    intent.putExtra("count", prodQuantaty);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    LayoutInflater inflater = getActivity().getLayoutInflater();
//                    View view = inflater.inflate(R.layout.dilaog_info_layout, null);
//
//                    Button butonOk = (Button) view.findViewById(R.id.buttonAaddtocartOk);
//                    TextView dialogText = (TextView) view.findViewById(R.id.addedtocartText);
//
//                    dialogText.setText(productName + " " + getString(R.string.added_to_cart));
//                    builder.setView(view);
//                    final AlertDialog forgotDialog = builder.create();
//                    forgotDialog.show();
//
//                    butonOk.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            forgotDialog.dismiss();
//
//                            try {
//                                getActivity().getSupportFragmentManager().popBackStack();
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//
//                            Intent intent = new Intent("CartCount");
//                            // You can also include some extra data.
//                            intent.putExtra("count", prodQuantaty);
//                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//                        }
//                    });


//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                        alertDialog.setCancelable(false);
//                        View titleview = getActivity().getLayoutInflater().inflate(R.layout.ustom_alert_title, null);
//                        TextView title = (TextView) titleview.findViewById(R.id.dialogtitle);
//                        title.setText("Your product " + productName + " added to cart");
//                        alertDialog.setCustomTitle(titleview);
//                        //   .setMessage("Are you sure you want to delete this entry?")
//                        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                try
//
//                                {
//                                    getActivity().getSupportFragmentManager().popBackStack();
//                                } catch (Exception ex) {
//
//                                }
//                                Intent intent = new Intent("CartCount");
//                                // You can also include some extra data.
//                                intent.putExtra("count", prodQuantaty);
//                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//                            }
//                        })
//                                .show();
                }
            } else {
                // Deleted Account
                ApplicationClass.getInstance().getPrefManager().clearSharedPreference();

                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("deletedUser", "1");
                getActivity().startActivity(i);
                getActivity().finish();

//                        Intent intent = new Intent("DeletedAccount");
//                        // You can also include some extra data.
//                        intent.putExtra("deletedUser", "1");
//                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception" + e.toString());
        }
    }

    @Override
    public void addToCartCallbackError(VolleyError volleyError) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        //error
    }
}