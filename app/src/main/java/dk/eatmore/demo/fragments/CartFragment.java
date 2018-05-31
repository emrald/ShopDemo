package dk.eatmore.demo.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.LoginActivity;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.adapter.InfoBarAdapter;
import dk.eatmore.demo.adapter.ShippingChargeAdapter;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.adapter.CartAdapter;
import dk.eatmore.demo.adapter.PaymentOptionAdapter;
import dk.eatmore.demo.interfaces.ApplyCodeGiftCardCallback;
import dk.eatmore.demo.interfaces.CancelOrderTransactionCallback;
import dk.eatmore.demo.interfaces.CheckoutCallback;
import dk.eatmore.demo.interfaces.CommunicatingWithRestaurantCallback;
import dk.eatmore.demo.interfaces.GetCityListCallback;
import dk.eatmore.demo.interfaces.OrderDetailsForCheckoutCallback;
import dk.eatmore.demo.interfaces.OrderTransactionCallback;
import dk.eatmore.demo.interfaces.PickUpDeliveryTimeCallback;
import dk.eatmore.demo.interfaces.ShippingCostCallback;
import dk.eatmore.demo.model.MyCartCheckOutList;
import dk.eatmore.demo.model.PaymentOption;
import dk.eatmore.demo.model.ShippingChargeObject;
import dk.eatmore.demo.myutils.ApiCall;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.Utility;
import dk.eatmore.demo.netutils.ApiCallBack;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import dk.eatmore.demo.network.NetworkUtils;
import dk.eatmore.demo.searchbardialog.CustomAlertAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static dk.eatmore.demo.fragments.EpayFragment.ePayPageBackPress;
import static dk.eatmore.demo.myutils.Utility.convertCurrencyToDanish;

public class CartFragment extends Fragment implements ApiCallBack, GetCityListCallback, CheckoutCallback, OrderDetailsForCheckoutCallback, PickUpDeliveryTimeCallback, ShippingCostCallback, ApplyCodeGiftCardCallback, OrderTransactionCallback, CommunicatingWithRestaurantCallback, CancelOrderTransactionCallback {

    private static final String TAG = CartFragment.class.getSimpleName();
    private View view = null;
    private View cartFooterView = null;
    private CheckBox cart_checkbox;

    private SimpleArcDialog mSimpleArcDialog;
    private ArcConfiguration configuration;
    // private CartAdapter mAdapter;
    Button mOrderConfirmbtn, couponsGiftValidation;
    ImageView imgback;
    private RecyclerView mRecyclerView;
    String cartItems;
    ArrayList<MyCartCheckOutList> cartListItem;
    private LinearLayout shipping_cart_address;
    //    private LinearLayout shiipingAddressNo;
    private LinearLayout changeShippingAddLayout;
    //    private LinearLayout shippingTitleLayout;
//    private LinearLayout ShhippingAddTitleNo;
    private RadioGroup radioDefaultShippingAdd;
    private RadioGroup radioGroup_shippingMethod;
    //    private TextView txtpaymentName;
//    private TextView txtpaymentStreetName;
//    private TextView txtpaymentPostalCode;
//    private TextView txtpaymentCityName;
//    private TextView txtpaymentTelephoneNo;
    private TextView shippingMethodText;
    private TextView paymentMethodText;
    //    private TextView finalAmountText;
    private TextView subTotalTextView;
    private TextView discountAmountText;
    private TextView shippingAmountTextView;
    private TextView totalAmountTextView;
    ArrayList<String> opIdArray;

    private LinearLayout discountCheckoutLinearLayout;

    String firstName, lastName, street, postal_code, city, telephone_no,
            discount_type = "", discount_id = "0", discount_amount = "0";
    //  String op_id;
    private String shippingTypeFlag = "0", pd_time, radioPickup_deliveryMSg = "Delivery Time",
            PaymentId, radioPickup_deliveryType = "Delivery", comment,
            radioPickUp;
    private String[] restpickuptime_array;
    private String firstValueForTime = "";

    ArrayList<String> stringArrayListPickup;
    ArrayList<String> stringArrayListDelivery;

    LinearLayout timeSelctionDialog, paymentDialog;
    int dialogPos = 0;

    ArrayList<PaymentOption> paymentOptions;
    PaymentOptionAdapter adapter;
    private EditText cartCommentInput;
    private EditText cartFirstName;
    private EditText cartLastName;
    private EditText cartStreetName;
    private EditText cartFloorDoorName;
    private EditText cartTelephone;
    private EditText inputGiftCoupons;
    private Button cartPostelCode;
    private Button cartCityName;
    private android.app.AlertDialog myalertDialog = null;
    CustomAlertAdapter arrayAdapter;
    private ArrayList<String> array_sort;
    int textlength = 0;
    String[] allPostalName_array;
    static String ORDER_NO, AMOUNT_TOTAL;
    //    private Button shippingCost;
//    private Button shippingCostAdd;

    String postalCodeNew;
    Double total_price = 0.0;
    Double subTotalDouble = 0.0;
    private Double discountAmountDouble = 0.0;
    Double shippingAddressCost = 0.0;
    String ipAdd = "";

    private boolean isDeliveryFlag = true;
    private LinearLayout shippingMethodLinearLayout;
    private LinearLayout deliveryLinearLayout;
    private LinearLayout pickupLinearLayout;
    private ImageView deliveryImageView;
    private ImageView pickupImageView;
    private LinearLayout shippingAmountLinearLayout;
    private boolean isShippingCostFetched = false;
    private boolean isPickupDeliveryTimeFetched = false;
    private Double orderTotalDouble = 0.0;

    //    private String termsUrlString = "http://itakeaway.dk/web-view/t-o-s";
    public static String shippingDistance = "";
    private TextView deliveryMessageTextView;
    private boolean wrongDeliveryAddress = true;
    private boolean wrongDeliveryAddressFromAPI = true;
    private String wrongDeliveryMsg = "";
    int prevCount = -1;
    private String deliveryMessage = "";
    private String pickUpMessage = "";
    private JSONArray cartProdIds = null;
    private String floorDoorString = "";

    private ImageView shippingChargeImageView;
    private LinearLayout shippingChargeLinearLayout;
    private ShippingChargeAdapter mShippingChargeAdapter;
    private Dialog shippingChargeDialog;
    private ListView shippingChargeListView;
    private ArrayList<ShippingChargeObject> mShippingChargeObjects;
    private String shippingUnit = "";

    private String firstNameOriginal = "";
    private String lastNameOriginal = "";
    private String streetNumberOriginal = "";
    private String floorDoorOriginal = "";
    private String postalCodeOriginal = "";
    private String cityOriginal = "";
    private String phoneNumberOriginal = "";
    private Double subTotalOriginal = 0.0;
    private Double discountOriginal = 0.0;
    private Double shippingOriginal = 0.0;
    private Double totalOriginal = 0.0;
    private boolean wrongDeliveryAddressFromAPIOriginal = true;

    private String couponCode = "";
    private boolean isDeliveryPresent = false;
    private boolean isPickupPresent = false;

    private String deliveryOrPickUpString = "Delivery";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(dk.eatmore.demo.R.layout.activity_cart, container, false);
        setupUI(view);
        if (EpayFragment.acceptPayment) {
            sendOrderTransaction();
        } else {
            if (ePayPageBackPress) {
                String reqTag = "CancelOrderTrabsaction";
                NetworkUtils.callCancelOrderTransaction(reqTag, CartFragment.this);
            }

            bindViews();
            bindFooterViews();
            initBroadcastReceiver();

            // Hiding Bottom Bar as it is Login screen
            ((MainActivity) getActivity()).hideBottomNavigation();

            opIdArray = new ArrayList<>();

            setTermOfService();

            cartStreetName.setSingleLine(true);
            cartStreetName.setLines(10);
            cartStreetName.setHorizontallyScrolling(false);
            cartCommentInput = (EditText) cartFooterView.findViewById(dk.eatmore.demo.R.id.cartComment);
            cartCommentInput.setSingleLine(true);
            cartCommentInput.setLines(10);
            cartCommentInput.setHorizontallyScrolling(false);

            if (discountAmountDouble <= 0) {
                discountCheckoutLinearLayout.setVisibility(View.GONE);
            } else {
                discountCheckoutLinearLayout.setVisibility(View.VISIBLE);
                discountAmountText.setText(convertCurrencyToDanish(discountAmountDouble) + " kr");
            }

            shipping_cart_address = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.shipping_cart_address);
//        ShhippingAddTitleNo = (LinearLayout) view.findViewById(R.id.ShhippingAddTitleNo);
//        shippingTitleLayout = (LinearLayout) view.findViewById(R.id.shippingTitleLayout);
//        shiipingAddressNo = (LinearLayout) view.findViewById(R.id.shiipingAddressNo);
            changeShippingAddLayout = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.shiipingAddLinelayout);
//
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(4, Color.parseColor("#019E0F"));
            drawable.setCornerRadius(2);
            //  drawable.setColor(Color.BLUE);
            changeShippingAddLayout.setBackgroundDrawable(drawable);

            radioDefaultShippingAdd = (RadioGroup) view.findViewById(dk.eatmore.demo.R.id.rg_change_shipping_address);
            radioGroup_shippingMethod = (RadioGroup) view.findViewById(dk.eatmore.demo.R.id.radioGroup_shippingMethod);

            shippingMethodLinearLayout = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.ll_shipping_method);
            deliveryLinearLayout = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.ll_delivery);
            pickupLinearLayout = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.ll_pick_up);
            deliveryImageView = (ImageView) view.findViewById(dk.eatmore.demo.R.id.iv_delivery);
            pickupImageView = (ImageView) view.findViewById(dk.eatmore.demo.R.id.iv_pick_up);

//        txtpaymentName = (TextView) view.findViewById(R.id.txtpaymentName);
//        txtpaymentStreetName = (TextView) view.findViewById(R.id.txtpaymentStreetName);
//        txtpaymentPostalCode = (TextView) view.findViewById(R.id.txtpaymentPostalCode);
//        txtpaymentCityName = (TextView) view.findViewById(R.id.txtpaymentCityName);
//        txtpaymentTelephoneNo = (TextView) view.findViewById(R.id.txtpaymentTelephoneNo);

            timeSelctionDialog = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.timeselctionDialog);
            paymentDialog = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.paymentDialog);

            shippingMethodText = (TextView) view.findViewById(dk.eatmore.demo.R.id.shippingMethodText);
            paymentMethodText = (TextView) view.findViewById(dk.eatmore.demo.R.id.paymentMethodText);

            paymentOptions = new ArrayList<>();
            adapter = new PaymentOptionAdapter(getActivity(), paymentOptions);
            mShippingChargeObjects = new ArrayList<>();
            mShippingChargeAdapter = new ShippingChargeAdapter(getActivity(), mShippingChargeObjects);

            stringArrayListDelivery = new ArrayList<String>();
            //   parseCartJson(cartItems);
//        shipping_cart_address.setVisibility(View.GONE);
//        shippingTitleLayout.setVisibility(View.GONE);
            cartStreetName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.e(TAG, "beforeTextChanged " + s + " " + start + " " + after + " " + count);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.e(TAG, "onTextChanged " + s + " " + start + " " + before + " " + count);
                    Log.e(TAG, "" + prevCount);
                    if (!ePayPageBackPress) {
                        if (prevCount != -1) {
                            cartPostelCode.setText(getResources().getText(dk.eatmore.demo.R.string.postal_code));
                            cartCityName.setText(getResources().getText(dk.eatmore.demo.R.string.city_name));
                            postal_code = "";
                            city = "";
                            floorDoorString = "";
                            cartFloorDoorName.setText(floorDoorString);
                            if (shippingTypeFlag.equalsIgnoreCase("1")) {
                                shippingAddressCost = 0.0;
                                totalAmountTextView.setText(convertCurrencyToDanish(total_price - discountAmountDouble + shippingAddressCost) + " kr");
                            }
                            shippingAmountLinearLayout.setVisibility(View.GONE);
                        }
                        prevCount = count;
                        Log.e(TAG, "" + prevCount);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.e(TAG, "afterTextChanged " + s.toString());
                }
            });

            cartPostelCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.app.AlertDialog.Builder myDialog = new android.app.AlertDialog.Builder(getActivity());

                    View titleview = getActivity().getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
                    TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
                    title.setText(dk.eatmore.demo.R.string.enter_postal_code);
                    myDialog.setCustomTitle(titleview);

                    final EditText editText = new EditText(getActivity());
                    editText.setTextColor(Color.BLACK);
                    editText.setHint(dk.eatmore.demo.R.string.enter_postal_code);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    int maxLength = 4;
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

                    final ListView listview = new ListView(getActivity());

                    editText.setCompoundDrawablesWithIntrinsicBounds(dk.eatmore.demo.R.drawable.ic_search_grey_500_24dp, 0, 0, 0);
                    array_sort = new ArrayList<String>(Arrays.asList(allPostalName_array));

                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.addView(editText);
                    layout.addView(listview);
                    myDialog.setView(layout);
                    arrayAdapter = new CustomAlertAdapter(getActivity(), array_sort);
                    listview.setAdapter(arrayAdapter);
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
                            myalertDialog.dismiss();
                            cartPostelCode.setError(null);
                            cartPostelCode.setText(array_sort.get(position));
                            postal_code = array_sort.get(position);
                            // Log.e("onclick","onclick "+array_sort.get(position));
                            isPickupDeliveryTimeFetched = true;
//                        getShippingtCost(postal_code);
                            getCityList(postal_code);
                        }
                    });
                    editText.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable s) {

                        }

                        public void beforeTextChanged(CharSequence s,
                                                      int start, int count, int after) {

                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            editText.setCompoundDrawablesWithIntrinsicBounds(dk.eatmore.demo.R.drawable.ic_search_grey_500_24dp, 0, 0, 0);
                            textlength = editText.getText().length();
                            array_sort.clear();
                            for (int i = 0; i < allPostalName_array.length; i++) {
                                if (textlength <= allPostalName_array[i].length()) {

                                    if (allPostalName_array[i].toLowerCase().contains(editText.getText().toString().toLowerCase().trim())) {
                                        array_sort.add(allPostalName_array[i]);
                                    }
                                }
                            }
                            listview.setAdapter(new CustomAlertAdapter(getActivity(), array_sort));
                        }
                    });
                    myDialog.setNegativeButton(getString(dk.eatmore.demo.R.string.Cancel), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    myalertDialog = myDialog.show();
                }
            });

            deliveryLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isDeliveryFlag) {
                        wrongDeliveryAddress = true;
                        wrongDeliveryAddressFromAPI = true;
                        isDeliveryFlag = true;
                        deliveryMessageTextView.setText(deliveryMessage);
                        deliveryImageView.setBackgroundColor(ResourcesCompat.getColor(getResources(), dk.eatmore.demo.R.color.accent_color, null));
                        pickupImageView.setBackgroundColor(Color.parseColor("#C8CCCF"));

                        radioPickup_deliveryMSg = "Delivery Time";
                        radioPickup_deliveryType = "Delivery";
                        shippingMethodText.setHint(dk.eatmore.demo.R.string.choose_delivery_time);
                        // false == delivery time
                        deliveryOrPickUpString = "Delivery";
                        getRestaurantPickUpDeliveryTime(deliveryOrPickUpString);
                        // TODO: 2/17/2017 getShipping cost for this postal code
//                    getShippingtCost(postal_code);
                        getNewShippingCost();

                        discountAmountDouble = 0.0;
                        discountCheckoutLinearLayout.setVisibility(View.GONE);

                        cartFirstName.setVisibility(View.VISIBLE);
                        cartLastName.setVisibility(View.VISIBLE);
                        cartStreetName.setVisibility(View.VISIBLE);
                        cartFloorDoorName.setVisibility(View.VISIBLE);
                        cartPostelCode.setVisibility(View.VISIBLE);
                        cartCityName.setVisibility(View.VISIBLE);
                        cartTelephone.setVisibility(View.VISIBLE);

                        shippingAmountLinearLayout.setVisibility(View.VISIBLE);
                        changeShippingAddLayout.setVisibility(View.VISIBLE);
                        inputGiftCoupons.setText("");


                        shippingChargeLinearLayout.setVisibility(View.VISIBLE);
//                    shiipingAddressNo.setVisibility(View.VISIBLE);
//                    ShhippingAddTitleNo.setVisibility(View.VISIBLE);
//                    shippingTitleLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

            pickupLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDeliveryFlag) {
                        wrongDeliveryAddress = false;
                        wrongDeliveryAddressFromAPI = false;
                        isDeliveryFlag = false;
                        deliveryMessageTextView.setText(pickUpMessage);
                        pickupImageView.setBackgroundColor(ResourcesCompat.getColor(getResources(), dk.eatmore.demo.R.color.accent_color, null));
                        deliveryImageView.setBackgroundColor(Color.parseColor("#C8CCCF"));
                        radioPickup_deliveryMSg = "Pick up Time";
                        radioPickup_deliveryType = "Pickup";
                        shippingMethodText.setHint(dk.eatmore.demo.R.string.choose_pickup_time);
                        // true == pickup Time
                        isShippingCostFetched = true;
                        deliveryOrPickUpString = "Pickup";
                        getRestaurantPickUpDeliveryTime(deliveryOrPickUpString);

                        discount_id = "0";
                        discountAmountDouble = 0.0;
                        shippingAddressCost = 0.0;
                        discountCheckoutLinearLayout.setVisibility(View.GONE);
                        shippingAmountLinearLayout.setVisibility(View.GONE);

//                    discountAmountText.setText(formatValueToMoney(0.0) + " kr");
//                    shippingAmountTextView.setText(formatValueToMoney(0.0) + " kr");
//                    totalAmountTextView.setText((total_price - discountAmountDouble + shippingAddressCost) + " kr");

                        discountAmountText.setText(convertCurrencyToDanish(0.0) + " kr");
                        shippingAmountTextView.setText(convertCurrencyToDanish(0.0) + " kr");
                        totalAmountTextView.setText(convertCurrencyToDanish(total_price - discountAmountDouble + shippingAddressCost) + " kr");

                        cartFirstName.setVisibility(View.VISIBLE);
                        cartLastName.setVisibility(View.VISIBLE);
                        cartStreetName.setVisibility(View.GONE);
                        cartFloorDoorName.setVisibility(View.GONE);
                        cartPostelCode.setVisibility(View.GONE);
                        cartCityName.setVisibility(View.GONE);
                        cartTelephone.setVisibility(View.VISIBLE);
                        inputGiftCoupons.setText("");

                        shippingChargeLinearLayout.setVisibility(View.GONE);
//                    changeShippingAddLayout.setVisibility(View.GONE);
//                    shiipingAddressNo.setVisibility(View.GONE);
//                    ShhippingAddTitleNo.setVisibility(View.GONE);
//                    shippingTitleLayout.setVisibility(View.GONE);
                    }
                }
            });

            radioGroup_shippingMethod.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case dk.eatmore.demo.R.id.radioButton_PickpUp:

                            radioPickup_deliveryMSg = "Pick up Time";
                            radioPickup_deliveryType = "Pickup";
                            shippingMethodText.setHint(dk.eatmore.demo.R.string.choose_pickup_time);
                            // true == pickup Time
                            deliveryOrPickUpString = "Pickup";
                            getRestaurantPickUpDeliveryTime(deliveryOrPickUpString);

                            changeShippingAddLayout.setVisibility(View.GONE);
//                        shiipingAddressNo.setVisibility(View.GONE);
//                        ShhippingAddTitleNo.setVisibility(View.GONE);
//                        shippingTitleLayout.setVisibility(View.GONE);
                            break;
                        case dk.eatmore.demo.R.id.radioButton_Delivery:
                            radioPickup_deliveryMSg = "Delivery Time";
                            radioPickup_deliveryType = "Delivery";
                            shippingMethodText.setHint(dk.eatmore.demo.R.string.choose_delivery_time);
                            // false == delivery time
                            deliveryOrPickUpString = "Delivery";
                            getRestaurantPickUpDeliveryTime(deliveryOrPickUpString);


                            changeShippingAddLayout.setVisibility(View.VISIBLE);
//                        shiipingAddressNo.setVisibility(View.VISIBLE);
//                        ShhippingAddTitleNo.setVisibility(View.VISIBLE);
//                        shippingTitleLayout.setVisibility(View.VISIBLE);
                            break;

                        default:
                            break;
                    }
                }
            });

            radioDefaultShippingAdd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {

                        case dk.eatmore.demo.R.id.radioYes:
                            // TODO: 2/17/2017 Shipping Cost will change if city changed
//                        finalAmountText.setText("" + total_price + " kr");
//                        shippingAddressCost = 0.0;

                            shippingTypeFlag = "1";

                            cartFirstName.setEnabled(true);
                            cartLastName.setEnabled(true);
                            cartStreetName.setEnabled(true);
                            cartFloorDoorName.setEnabled(true);
                            cartPostelCode.setEnabled(true);
                            cartCityName.setEnabled(true);
                            cartTelephone.setEnabled(true);

                            setBackgroundForShippingAddressFields();
//                        shiipingAddressNo.setVisibility(View.GONE);
//                        ShhippingAddTitleNo.setVisibility(View.GONE);
//                        shipping_cart_address.setVisibility(View.VISIBLE);
//                        shippingTitleLayout.setVisibility(View.VISIBLE);

                            break;
                        case dk.eatmore.demo.R.id.radioNo:
//                        finalAmountText.setText("" + total_price + " kr");
//                        shippingAddressCost = 0.0;

                            shippingTypeFlag = "0";
                            cartFirstName.setEnabled(false);
                            cartLastName.setEnabled(false);
                            cartStreetName.setEnabled(false);
                            cartFloorDoorName.setEnabled(false);
                            cartPostelCode.setEnabled(false);
                            cartCityName.setEnabled(false);
                            cartTelephone.setEnabled(false);

                            setBackgroundForShippingAddressFields();

//                        shipping_cart_address.setVisibility(View.GONE);
//                        shippingTitleLayout.setVisibility(View.GONE);
//                        shiipingAddressNo.setVisibility(View.VISIBLE);
//                        ShhippingAddTitleNo.setVisibility(View.VISIBLE);
                            break;

                        default:
                            break;
                    }
                }
            });

            couponsGiftValidation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateCoupon())
//                    revealCouponsGiftCard(promoCode);
                        applyCouponsGiftCard(couponCode);

                }
            });


            TextView title = (TextView) view.findViewById(dk.eatmore.demo.R.id.tootlbar_title);
            title.setTextColor(ResourcesCompat.getColor(getResources(), dk.eatmore.demo.R.color.accent_color, null));
            title.setTypeface(null, Typeface.BOLD);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            title.setText(getResources().getString(dk.eatmore.demo.R.string.checkout));
            imgback = (ImageView) view.findViewById(dk.eatmore.demo.R.id.imgback);
            imgback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mOnlinePaymentReceiver);
                    getActivity().getSupportFragmentManager().popBackStack();
                    // Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
                }
            });


            mOrderConfirmbtn = (Button) view.findViewById(dk.eatmore.demo.R.id.btnConfirm);
            mOrderConfirmbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOrderConfirmbtn.setEnabled(false);
                    mSimpleArcDialog = getProgressDialog(getActivity(), null);
                    mSimpleArcDialog.show();
                    mOrderConfirmbtn.setEnabled(true);

                    JSONObject finalCartJson = new JSONObject();
                    if (getValidation()) {

                        try {
                            finalCartJson.accumulate("r_token", ApiCall.R_TOKEN);
                            finalCartJson.accumulate("r_key", ApiCall.R_KEY);

                            finalCartJson.accumulate("customer_id", ApplicationClass.getInstance().getPrefManager()
                                    .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
                            finalCartJson.put("first_name", firstName);
                            finalCartJson.put("last_name", lastName);
                            if (shippingTypeFlag.equalsIgnoreCase("1")) {
                                if (floorDoorString != null && !floorDoorString.isEmpty())
                                    finalCartJson.put("address", street + " " + floorDoorString + ", " + postal_code + " " + city);
                                else
                                    finalCartJson.put("address", street + ", " + postal_code + " " + city);
                            } else if (shippingTypeFlag.equalsIgnoreCase("0")) {

                                if (floorDoorOriginal != null && !floorDoorOriginal.isEmpty())
                                    finalCartJson.put("address", streetNumberOriginal + " " + floorDoorOriginal + ", " + postalCodeOriginal + " " + cityOriginal);
                                else
                                    finalCartJson.put("address", streetNumberOriginal + ", " + postalCodeOriginal + " " + cityOriginal);
                            }

                            if (isDeliveryFlag) {
//                            finalCartJson.put("address", street + ", " + postal_code + ", " + city);
                                finalCartJson.put("distance", shippingDistance);
                            } else {
//                            finalCartJson.put("address", "");
                                finalCartJson.put("distance", "");
                            }
//                        finalCartJson.put("street", street);
//                        finalCartJson.put("postal_code", postal_code);
//                        finalCartJson.put("city", city);
                            finalCartJson.put("telephone_no", telephone_no);


                            finalCartJson.accumulate("ip", ApplicationClass.getInstance().getPrefManager()
                                    .getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));
                            finalCartJson.accumulate("comments", comment);
                            finalCartJson.accumulate("shipping", radioPickup_deliveryType);
                            finalCartJson.accumulate("expected_time", pd_time);
                            finalCartJson.accumulate("first_time", firstValueForTime);

                            //Coupon/Giftcard/
                            finalCartJson.accumulate("discount_id", discount_id);
                            finalCartJson.accumulate("discount_type", discount_type);
                            finalCartJson.accumulate("discount_amount", discount_amount);
                            finalCartJson.accumulate("shipping_costs", shippingAddressCost);

                            finalCartJson.accumulate("accept_tc", "1");
                            finalCartJson.accumulate("paymethod", PaymentId);

//                        orderTotalDouble = subTotalDouble - discountAmountDouble + shippingAddressCost;
                            finalCartJson.accumulate("order_total", subTotalDouble);

                            finalCartJson.accumulate("device_type", "Android");

//                        finalCartJson.accumulate("user_IP_add", ipAdd);

//                        String oderNO=ApplicationClass.getInstance().
//                                getPrefManager().getStringPreferences(MyPreferenceManager.ORDER_NUMBER);
//
//                        if(oderNO!=null)
//                            finalCartJson.accumulate("order_no", oderNO);
//                        else
//                            finalCartJson.accumulate("order_no","");
//                        finalCartJson.accumulate("order_no", MainActivity.ORDER_NO);
                            //if address_st==1 then user info  new  and 0 means default
                            //  finalCartJson.accumulate("address_st", shippingTypeFlag);


                            //  finalCartJson.accumulate("vat", "0");

                            total_price = total_price + shippingAddressCost;

                            //   finalCartJson.accumulate("total_to_pay", ""+total_price);
//                        finalCartJson.accumulate("system", "App");

                            //user array
//                        JSONArray userinfoArray = new JSONArray();
//                        JSONObject userInfoJson = new JSONObject();
                            //   if (shippingTypeFlag.equals("1")) {
//                        try {
//                            userInfoJson.put("first_name", firstName);
//                            userInfoJson.put("last_name", lastName);
//                            userInfoJson.put("street", street);
//                            userInfoJson.put("postal_code", postal_code);
//                            userInfoJson.put("city", city);
//                            userInfoJson.put("telephone_no", telephone_no);
//                            //    reqObj.put("ingredientsName", ingredentsMap.get(key));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        // }
//                        userinfoArray.put(userInfoJson);
//                        finalCartJson.put("useinfo", userinfoArray);

                            //cart product Id

                            //user array
                            JSONArray cartProdId = new JSONArray();
                            JSONObject cartProdJson = null;
                            try {
                                for (int i = 0; i < opIdArray.size(); i++) {
                                    cartProdJson = new JSONObject();
                                    cartProdJson.put("op_id", opIdArray.get(i));
                                    cartProdId.put(cartProdJson);
                                }
                                //    reqObj.put("ingredientsName", ingredentsMap.get(key));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            cartProdIds = cartProdId;
                            finalCartJson.put("cartproducts", cartProdId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "cart  " + finalCartJson);
                        add_to_cart(finalCartJson, getActivity());
                    } else {
                        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
                            mSimpleArcDialog.dismiss();
                    }

                }
            });

            paymentDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(
                            getActivity());

                    View titleview = getActivity().getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
                    TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
                    title.setText(dk.eatmore.demo.R.string.payment_method);
                    dialog.setCustomTitle(titleview);

                    dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Toast.makeText(getActivity(), "Item Selected: " + item, Toast.LENGTH_SHORT).show();
                            PaymentId = paymentOptions.get(item).getPaymentId();
                            paymentMethodText.setText(paymentOptions.get(item).getPayentName());
                        }
                    }).show();
                }
            });

            timeSelctionDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(
                            getActivity());

                    View titleview = getActivity().getLayoutInflater().inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
                    TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
                    title.setHint(getString(dk.eatmore.demo.R.string.choose_pickup_time));
                    if (isDeliveryFlag)
                        title.setText(getString(dk.eatmore.demo.R.string.choose_delivery_time));
                    else
                        title.setText(getString(dk.eatmore.demo.R.string.choose_pickup_time));
                    dialog.setCustomTitle(titleview);
                    dialog.setSingleChoiceItems(restpickuptime_array, dialogPos,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    dialog.dismiss();

                                    shippingMethodText.setText(restpickuptime_array[item]);
                                    if (item == 0)
                                        pd_time = firstValueForTime;
                                    else
                                        pd_time = restpickuptime_array[item];
                                    dialogPos = item;
                                }
                            });

                    dialog.show();
                }
            });

            shippingChargeLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shippingChargeDialog = new Dialog(getActivity());  // always give context of activity.
                    shippingChargeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    shippingChargeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    shippingChargeDialog.setContentView(dk.eatmore.demo.R.layout.dialog_shipping_charge);

                    ImageView closeButton = (ImageView) shippingChargeDialog.findViewById(dk.eatmore.demo.R.id.iv_close_shipping_charge);
                    TextView fromTitleShippingChargeListView = (TextView) shippingChargeDialog.findViewById(dk.eatmore.demo.R.id.tv_from_shipping_charge_title);
                    TextView toTitleShippingChargeListView = (TextView) shippingChargeDialog.findViewById(dk.eatmore.demo.R.id.tv_to_shipping_charge_title);
                    shippingChargeListView = (ListView) shippingChargeDialog.findViewById(dk.eatmore.demo.R.id.lv_shipping_charge);

                    String fromTitleShippingCharge = "";
                    String toTitleShippingCharge = "";
                    if (shippingUnit == null || shippingUnit.equalsIgnoreCase("null")) {
                        // Don't show dialog
                        if (shippingChargeDialog != null && shippingChargeDialog.isShowing())
                            shippingChargeDialog.dismiss();
                    } else {
                        if (shippingUnit.equalsIgnoreCase("km")) {
                            fromTitleShippingCharge = getString(dk.eatmore.demo.R.string.from_km);
                            toTitleShippingCharge = getString(dk.eatmore.demo.R.string.to_km);
                        } else if (shippingUnit.equalsIgnoreCase("Price")) {
                            fromTitleShippingCharge = getString(dk.eatmore.demo.R.string.from_price);
                            toTitleShippingCharge = getString(dk.eatmore.demo.R.string.to_price);
                        }

                        fromTitleShippingChargeListView.setText(fromTitleShippingCharge);
                        toTitleShippingChargeListView.setText(toTitleShippingCharge);
                        shippingChargeListView.setAdapter(mShippingChargeAdapter);

                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (shippingChargeDialog != null && shippingChargeDialog.isShowing())
                                    shippingChargeDialog.dismiss();
                            }
                        });

                        shippingChargeDialog.show();
                    }

                }
            });

            shippingChargeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shippingChargeDialog = new Dialog(getActivity());  // always give context of activity.
                    shippingChargeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    shippingChargeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    shippingChargeDialog.setContentView(dk.eatmore.demo.R.layout.dialog_shipping_charge);

                    ImageView closeButton = (ImageView) shippingChargeDialog.findViewById(dk.eatmore.demo.R.id.iv_close_shipping_charge);
                    TextView fromTitleShippingChargeListView = (TextView) shippingChargeDialog.findViewById(dk.eatmore.demo.R.id.tv_from_shipping_charge_title);
                    TextView toTitleShippingChargeListView = (TextView) shippingChargeDialog.findViewById(dk.eatmore.demo.R.id.tv_to_shipping_charge_title);
                    shippingChargeListView = (ListView) shippingChargeDialog.findViewById(dk.eatmore.demo.R.id.lv_shipping_charge);

                    String fromTitleShippingCharge = "";
                    String toTitleShippingCharge = "";
                    if (shippingUnit == null || shippingUnit.equalsIgnoreCase("null")) {
                        // Don't show dialog
                        if (shippingChargeDialog != null && shippingChargeDialog.isShowing())
                            shippingChargeDialog.dismiss();
                    } else {
                        if (shippingUnit.equalsIgnoreCase("km")) {
                            fromTitleShippingCharge = getString(dk.eatmore.demo.R.string.from_km);
                            toTitleShippingCharge = getString(dk.eatmore.demo.R.string.to_km);
                        } else if (shippingUnit.equalsIgnoreCase("Price")) {
                            fromTitleShippingCharge = getString(dk.eatmore.demo.R.string.from_price);
                            toTitleShippingCharge = getString(dk.eatmore.demo.R.string.to_price);
                        }

                        fromTitleShippingChargeListView.setText(fromTitleShippingCharge);
                        toTitleShippingChargeListView.setText(toTitleShippingCharge);
                        shippingChargeListView.setAdapter(mShippingChargeAdapter);

                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (shippingChargeDialog != null && shippingChargeDialog.isShowing())
                                    shippingChargeDialog.dismiss();
                            }
                        });

                        shippingChargeDialog.show();
                    }

                }
            });

//        shippingCost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getShippingtCost(postal_code);
//            }
//        });

//        shippingCostAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getPostalCodeValidation())
//                    getShippingtCost(postalCodeNew);
//            }
//        });
//        NetwordDetect();

            getUserDetails();
            //  getAllPostel();
        }
        return view;
    }

    private boolean validateCoupon() {
        couponCode = inputGiftCoupons.getText().toString().trim();
        if (couponCode.isEmpty()) {
//            inputGiftCoupons.setError("Please enter code");
            inputGiftCoupons.setError(getString(dk.eatmore.demo.R.string.invalid_coupon));
            return false;
        } else {
            firstName = cartFirstName.getText().toString().trim();
            lastName = cartLastName.getText().toString().trim();
            telephone_no = cartTelephone.getText().toString();

            if (!firstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
                cartFirstName.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
                cartFirstName.requestFocus();
            } else
                cartFirstName.setError(null);

            if (firstName.length() <= 2 || firstName.length() > 100) {
                cartFirstName.setError(getString(dk.eatmore.demo.R.string.first_name_limit));
                // inputLastName.requestFocus();
            } else
                cartFirstName.setError(null);

            if (!lastName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
                cartLastName.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
                // inputLastName.requestFocus();
            } else
                cartLastName.setError(null);

            if (lastName.length() <= 2 || lastName.length() > 100) {
                cartLastName.setError(getString(dk.eatmore.demo.R.string.last_name_limit));
                // inputLastName.requestFocus();
            } else
                cartLastName.setError(null);

            if (!firstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
                cartFirstName.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
                //    inputFirstName.requestFocus();
                return false;
            }

            if (!lastName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
                cartLastName.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
                //    inputLastName.requestFocus();
                return false;
            }

            if (isDeliveryFlag) {
                if (wrongDeliveryAddressFromAPI) {
//            Toast.makeText(getActivity(), wrongDeliveryMsg, Toast.LENGTH_SHORT).show();
                    callAlertDialog();
                    return false;
                }

                street = cartStreetName.getText().toString();
                floorDoorString = cartFloorDoorName.getText().toString().trim();

                if (street.isEmpty()) {
                    cartStreetName.setError(getString(dk.eatmore.demo.R.string.empty_street_number));
                    if (isDeliveryFlag) {
                        wrongDeliveryAddress = true;
                    }
                    // inputStreetName.requestFocus();
                } else {
                    wrongDeliveryAddress = false;
                    cartStreetName.setError(null);
                }
                if (wrongDeliveryAddress) {
//            Toast.makeText(getActivity(), wrongDeliveryMsg, Toast.LENGTH_SHORT).show();
                    callAlertDialog();
                    return false;
                }

//            if (floorDoorString.isEmpty()) {
//                cartFloorDoorName.setError("Address is not valid.");
//                if (isDeliveryFlag) {
//                    wrongDeliveryAddress = true;
//                }
//                // inputStreetName.requestFocus();
//            } else
//                cartFloorDoorName.setError(null);

                if (postal_code == null || postal_code.isEmpty()) {
                    cartPostelCode.setError(getString(dk.eatmore.demo.R.string.invalid_postal_code));
                    // btnPostelCode.requestFocus();
                } else
                    cartPostelCode.setError(null);

                if (street.isEmpty()) {
                    cartStreetName.setError(getString(dk.eatmore.demo.R.string.empty_street_number));
                    // inputStreetName.requestFocus();
                    return false;
                }

                if (postal_code == null || postal_code.isEmpty()) {
                    cartPostelCode.setError(getString(dk.eatmore.demo.R.string.invalid_postal_code));
                    //  btnPostelCode.requestFocus();
                    return false;
                }

                if (city == null || city.isEmpty()) {
                    cartCityName.setError(getString(dk.eatmore.demo.R.string.invalid_city_name));
                    //  btnCityName.requestFocus();
                    return false;
                } else
                    cartCityName.setError(null);

                if (telephone_no.isEmpty()) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
                    //  inputPhoneNo.requestFocus();
                    return false;
                }

                if (telephone_no.length() <= 7) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.invalid_phone_number));
                    //   inputPhoneNo.requestFocus();
                    return false;
                }

                if (telephone_no.length() > 15) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.invalid_phone_number));
                    //   inputPhoneNo.requestFocus();
                    return false;
                }
            } else {

                if (telephone_no.isEmpty()) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
                    //  inputPhoneNo.requestFocus();
                    return false;
                }

                if (telephone_no.length() <= 7) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.invalid_phone_number));
                    //   inputPhoneNo.requestFocus();
                    return false;
                }

                if (telephone_no.length() > 15) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.invalid_phone_number));
                    //   inputPhoneNo.requestFocus();
                    return false;
                }
            }
        }

        return true;
    }

    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Utility.hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void setBackgroundForShippingAddressFields() {
        if (shippingTypeFlag.equalsIgnoreCase("1")) {
            cartFirstName.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_bg);
            cartLastName.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_bg);
            cartStreetName.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_bg);
            cartFloorDoorName.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_bg);
            cartPostelCode.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_bg);
            cartCityName.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_bg);
            cartTelephone.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_bg);

            discountAmountDouble = 0.0;
        } else if (shippingTypeFlag.equalsIgnoreCase("0")) {
            cartFirstName.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_gray_bg);
            cartLastName.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_gray_bg);
            cartStreetName.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_gray_bg);
            cartFloorDoorName.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_gray_bg);
            cartPostelCode.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_gray_bg);
            cartCityName.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_gray_bg);
            cartTelephone.setBackgroundResource(dk.eatmore.demo.R.drawable.edit_text_gray_bg);

            cartFirstName.setText(firstNameOriginal);
            cartLastName.setText(lastNameOriginal);
            cartStreetName.setText(streetNumberOriginal);
            cartFloorDoorName.setText(floorDoorOriginal);
            cartPostelCode.setText(postalCodeOriginal);
            cartCityName.setText(cityOriginal);
            cartTelephone.setText(phoneNumberOriginal);

            subTotalTextView.setText(convertCurrencyToDanish(subTotalOriginal) + " kr");
            shippingAmountTextView.setText(convertCurrencyToDanish(shippingOriginal) + " kr");
            if (isDeliveryFlag) {
                shippingAmountLinearLayout.setVisibility(View.VISIBLE);
            }

            totalAmountTextView.setText(convertCurrencyToDanish(subTotalOriginal - discountOriginal + shippingOriginal) + " kr");

            postal_code = postalCodeOriginal;
            subTotalDouble = subTotalOriginal;
            discountAmountDouble = 0.0;
            shippingAddressCost = shippingOriginal;
            wrongDeliveryAddressFromAPI = wrongDeliveryAddressFromAPIOriginal;
        }
        discountCheckoutLinearLayout.setVisibility(View.GONE);
    }

    private void setTermOfService() {
        String input = "<font color=" + "#41579F" + ">" + "terms of service" + "</font>";
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
//                startActivity(new Intent(CartFragment.this, NextActivity.class));
                if (cart_checkbox.isChecked())
                    cart_checkbox.setChecked(false);
                else
                    cart_checkbox.setChecked(true);
                Fragment termsOfServiceFragment = new TermsServicesFragment();
                // title="Explore The MindSets";
                FragmentManager termsOfServiceFragmentManager = (getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransactionTerms_Services = termsOfServiceFragmentManager.beginTransaction();
                fragmentTransactionTerms_Services.add(dk.eatmore.demo.R.id.fragment_container, termsOfServiceFragment);
                fragmentTransactionTerms_Services.addToBackStack(InfoBarAdapter.class.getName());
                fragmentTransactionTerms_Services.commit();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
//                ds.setColor(getResources().getColor(R.color.orange));
                ds.setColor(ContextCompat.getColor(getActivity(), dk.eatmore.demo.R.color.dark_blue));
            }
        };

        String deviceLocale = Locale.getDefault().getLanguage();
        Log.e(TAG, deviceLocale);
        String termsString = "";
        SpannableString ss;
        if (deviceLocale.equalsIgnoreCase("en")) {
            termsString = "I have read and I agree to the terms of service";
            ss = new SpannableString(termsString);
            ss.setSpan(clickableSpan, 31, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            termsString = "Jeg har læst og accepteret handelsbetingelserne.";
            ss = new SpannableString(termsString);
            ss.setSpan(clickableSpan, 16, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        cart_checkbox.setText(ss);
        cart_checkbox.setMovementMethod(LinkMovementMethod.getInstance());

//        cart_checkbox.setText(Utility.fromHtml("I have read and I agree to the" + " " +
//                "<a href=" + termsUrlString + ">" + input + "</a>"));
//        cart_checkbox.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initBroadcastReceiver() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mOnlinePaymentReceiver,
                new IntentFilter("OnlinePayment"));
    }

    private void bindViews() {
        mRecyclerView = (RecyclerView) view.findViewById(dk.eatmore.demo.R.id.yourorderlist);
        cartFirstName = (EditText) view.findViewById(dk.eatmore.demo.R.id.cartFirstName);
        cartLastName = (EditText) view.findViewById(dk.eatmore.demo.R.id.cartLastName);
        cartStreetName = (EditText) view.findViewById(dk.eatmore.demo.R.id.cartStreetName);
        cartFloorDoorName = (EditText) view.findViewById(dk.eatmore.demo.R.id.et_floor_door_shipping);
        cartTelephone = (EditText) view.findViewById(dk.eatmore.demo.R.id.cartTelephone);

        cartPostelCode = (Button) view.findViewById(dk.eatmore.demo.R.id.cartPostelCode);
        cartCityName = (Button) view.findViewById(dk.eatmore.demo.R.id.cartCityName);
//        shippingCost = (Button) view.findViewById(R.id.shippingCost);
//        shippingCostAdd = (Button) view.findViewById(R.id.shippingCostAdd);
        couponsGiftValidation = (Button) view.findViewById(dk.eatmore.demo.R.id.couponsGiftValidation);
        inputGiftCoupons = (EditText) view.findViewById(dk.eatmore.demo.R.id.inputGiftCoupons);
        deliveryMessageTextView = (TextView) view.findViewById(dk.eatmore.demo.R.id.tv_delivery_message);
        shippingChargeLinearLayout = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.ll_shipping_charge);
        shippingChargeImageView = (ImageView) view.findViewById(dk.eatmore.demo.R.id.iv_shipping_charge);
    }

    private void bindFooterViews() {
        cartFooterView = view.findViewById(dk.eatmore.demo.R.id.orderTotalLayout);
        cart_checkbox = (CheckBox) cartFooterView.findViewById(dk.eatmore.demo.R.id.cart_checkbox);
        shippingAmountLinearLayout = (LinearLayout) cartFooterView.findViewById(dk.eatmore.demo.R.id.ll_shipping_amount);
//        finalAmountText = (TextView) cartFooterView.findViewById(R.id.finalAmount);
        subTotalTextView = (TextView) cartFooterView.findViewById(dk.eatmore.demo.R.id.tv_sub_total);
        totalAmountTextView = (TextView) cartFooterView.findViewById(dk.eatmore.demo.R.id.tv_total);
        discountAmountText = (TextView) cartFooterView.findViewById(dk.eatmore.demo.R.id.discountAmount);
        shippingAmountTextView = (TextView) cartFooterView.findViewById(dk.eatmore.demo.R.id.tv_shipping_amount);

        discountCheckoutLinearLayout = (LinearLayout) cartFooterView.findViewById(dk.eatmore.demo.R.id.ll_discount_checkout);
//        cartStreetName = (EditText) cartFooterView.findViewById(R.id.cartComment);
    }

    private boolean getPostalCodeValidation() {

        postalCodeNew = cartPostelCode.getText().toString().trim();

        if (postalCodeNew.length() <= 0) {
            cartPostelCode.setError(getString(dk.eatmore.demo.R.string.invalid_postal_code));
        } else
            cartPostelCode.setError(null);

        if (postalCodeNew.length() <= 0) {
            cartPostelCode.setError(getString(dk.eatmore.demo.R.string.invalid_postal_code));
            //  btnPostelCode.requestFocus();
            return false;
        }
        return true;
    }

    private void getCityList(final String postalId) {
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mSimpleArcDialog.show();
        String tag_string_req = "getCityList";

        NetworkUtils.callGetCityList(postalId, tag_string_req, CartFragment.this);
    }

    @Override
    public void getCityListCallbackSuccess(String success) {
        //   Log.e(TAG, "cHARITY Response: " + response);
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();

        JSONObject jObj;
        try {
            jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            if (status) {
                cartCityName.setText(jObj.getString("city_name"));
                city = jObj.getString("city_name");

                street = cartStreetName.getText().toString();
                if (street.isEmpty()) {
                    cartStreetName.setError(getString(dk.eatmore.demo.R.string.empty_street_number));
                    if (isDeliveryFlag) {
                        wrongDeliveryAddress = true;
                    }
                    // inputStreetName.requestFocus();
                } else {
                    wrongDeliveryAddress = false;
                    cartStreetName.setError(null);
                }
                if (isDeliveryPresent)
                    getNewShippingCost();
            } else {
                // Error occurred in registration. Get the error
                // message
                String errorMsg = jObj.getString("status");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getCityListCallbackError(VolleyError volleyError) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Toast.makeText(getActivity(),
                volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }

    private boolean getValidation() {
        if (shippingMethodText.getText().toString().trim().length() <= 0) {
            Toast.makeText(getActivity(), "Please select " + radioPickup_deliveryMSg, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (PaymentId == null) {
            Toast.makeText(getActivity(), "Please select payment method", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (total_price == null) {
            Toast.makeText(getActivity(), getString(dk.eatmore.demo.R.string.try_again), Toast.LENGTH_SHORT).show();
            return false;
        }

        comment = cartCommentInput.getText().toString().trim();
        firstName = cartFirstName.getText().toString().trim();
        lastName = cartLastName.getText().toString().trim();
        telephone_no = cartTelephone.getText().toString();

        if (shippingTypeFlag.equalsIgnoreCase("1")) {

            if (!firstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
                cartFirstName.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
                cartFirstName.requestFocus();
            } else
                cartFirstName.setError(null);

            if (firstName.length() <= 2 || firstName.length() > 100) {
                cartFirstName.setError(getString(dk.eatmore.demo.R.string.first_name_limit));
                // inputLastName.requestFocus();
            } else
                cartFirstName.setError(null);

            if (!lastName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
                cartLastName.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
                // inputLastName.requestFocus();
            } else
                cartLastName.setError(null);

            if (lastName.length() <= 2 || lastName.length() > 100) {
                cartLastName.setError(getString(dk.eatmore.demo.R.string.last_name_limit));
                // inputLastName.requestFocus();
            } else
                cartLastName.setError(null);

            if (!firstName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
                cartFirstName.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
                //    inputFirstName.requestFocus();
                return false;
            }

            if (!lastName.matches("^[A-Za-z_ÆØÅæøå ]+$")) {
                cartLastName.setError(getString(dk.eatmore.demo.R.string.only_alpha_numeric));
                //    inputLastName.requestFocus();
                return false;
            }

            if (isDeliveryFlag) {
                if (wrongDeliveryAddressFromAPI) {
//            Toast.makeText(getActivity(), wrongDeliveryMsg, Toast.LENGTH_SHORT).show();
                    callAlertDialog();
                    return false;
                }

                street = cartStreetName.getText().toString();
                if (street.isEmpty()) {
                    cartStreetName.setError(getString(dk.eatmore.demo.R.string.empty_street_number));
                    if (isDeliveryFlag) {
                        wrongDeliveryAddress = true;
                    }
                    // inputStreetName.requestFocus();
                } else {
                    wrongDeliveryAddress = false;
                    cartStreetName.setError(null);
                }

                if (wrongDeliveryAddress) {
//            Toast.makeText(getActivity(), wrongDeliveryMsg, Toast.LENGTH_SHORT).show();
                    callAlertDialog();
                    return false;
                }

                floorDoorString = cartFloorDoorName.getText().toString().trim();
//            if (floorDoorString.isEmpty()) {
//                cartFloorDoorName.setError(getString(R.string.empty_street_number));
//                if (isDeliveryFlag) {
//                    wrongDeliveryAddress = true;
//                }
//                // inputStreetName.requestFocus();
//            } else
//                cartFloorDoorName.setError(null);


                if (postal_code == null || postal_code.isEmpty()) {
                    cartPostelCode.setError(getString(dk.eatmore.demo.R.string.invalid_postal_code));
                    // btnPostelCode.requestFocus();
                } else
                    cartPostelCode.setError(null);

                if (street.isEmpty()) {
                    cartStreetName.setError(getString(dk.eatmore.demo.R.string.empty_street_number));
                    // inputStreetName.requestFocus();
                    return false;
                }

                if (postal_code == null || postal_code.isEmpty()) {
                    cartPostelCode.setError(getString(dk.eatmore.demo.R.string.invalid_postal_code));
                    //  btnPostelCode.requestFocus();
                    return false;
                }

                if (city == null || city.isEmpty()) {
                    cartCityName.setError(getString(dk.eatmore.demo.R.string.invalid_city_name));
                    //  btnCityName.requestFocus();
                    return false;
                } else
                    cartCityName.setError(null);

                if (telephone_no.isEmpty()) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
                    //  inputPhoneNo.requestFocus();
                    return false;
                }

                if (telephone_no.length() <= 7) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.invalid_phone_number));
                    //   inputPhoneNo.requestFocus();
                    return false;
                }

                if (telephone_no.length() > 15) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.invalid_phone_number));
                    //   inputPhoneNo.requestFocus();
                    return false;
                }
            } else {

                if (telephone_no.isEmpty()) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.cannot_be_blank_field));
                    //  inputPhoneNo.requestFocus();
                    return false;
                }

                if (telephone_no.length() <= 7) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.invalid_phone_number));
                    //   inputPhoneNo.requestFocus();
                    return false;
                }

                if (telephone_no.length() > 15) {
                    cartTelephone.setError(getString(dk.eatmore.demo.R.string.invalid_phone_number));
                    //   inputPhoneNo.requestFocus();
                    return false;
                }
            }
        }

        if (!cart_checkbox.isChecked()) {
            Toast.makeText(getActivity(), dk.eatmore.demo.R.string.term_of_service_not_accepted, Toast.LENGTH_SHORT).show();
            // inputStreetName.requestFocus();
            return false;
        }

        return true;
    }


    private void parseCartJson(JSONArray jsonArray) {

        cartListItem = new ArrayList<>();
        try {
            // JSONObject jsonObject=new JSONObject(response);

            // JSONArray jsonArray=jsonObject.getJSONArray("result");
            int size = jsonArray.length();

            for (int i = 0; i < size; i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("products");

                MyCartCheckOutList myCartCheckOutList = new MyCartCheckOutList();

                String pId = jsonObject1.getString("p_id");

                String op_id = jsonObject1.getString("op_id");
                myCartCheckOutList.setOp_id(op_id);

                opIdArray.add(op_id);
                myCartCheckOutList.setP_id(pId);
                myCartCheckOutList.setP_name(jsonObject1.getString("p_name"));
                myCartCheckOutList.setP_image(jsonObject1.getString("p_image"));
                myCartCheckOutList.setP_price(jsonObject1.getString("p_price"));
                myCartCheckOutList.setP_quantity(jsonObject1.getString("p_quantity"));
                cartListItem.add(myCartCheckOutList);
            }

            CartAdapter adapter = new CartAdapter(getActivity(), cartListItem);
            mRecyclerView.setAdapter(adapter);
            setRetainInstance(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            // mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserDetails() {
        opIdArray = new ArrayList<>();
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mSimpleArcDialog.show();
        String tag_string_req = "OrderDetails";

        NetworkUtils.callOrderDetailsForCheckout(tag_string_req, CartFragment.this);
    }

    @Override
    public void orderDetailsForCheckoutCallbackSuccess(String success) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e(TAG, "MY_ORDER_DETAILS" + success);
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
                deliveryMessage = jObj.getString("delivery_message");
                pickUpMessage = jObj.getString("pickup_message");
                deliveryMessageTextView.setText(deliveryMessage);

                if (jObj.has("delivery_present"))
                    isDeliveryPresent = jObj.getBoolean("delivery_present");
                if (jObj.has("pickup_present"))
                    isPickupPresent = jObj.getBoolean("pickup_present");

//                if (isDeliveryPresent && isPickupPresent) {
//                    //if Both present make them VISIBLE
//                    deliveryLinearLayout.setVisibility(View.VISIBLE);
//                    pickupLinearLayout.setVisibility(View.VISIBLE);
//                } else if (!isDeliveryPresent && !isPickupPresent) {
//                    //if Both NOT present make them GONE
//                    shippingMethodLinearLayout.setVisibility(View.GONE);
//
//                    isDeliveryFlag = false;
//                } else {
//                    if (isDeliveryPresent) {
//                        //if Delivery present make it VISIBLE and Pickup GONE
//                        deliveryLinearLayout.setVisibility(View.VISIBLE);
//                        pickupLinearLayout.setVisibility(View.GONE);
//                        deliveryImageView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.accent_color));
//
//                        deliveryLinearLayout.performClick();
//                    } else {
//                        //if Pickup present make it VISIBLE and Delivery GONE
//                        deliveryLinearLayout.setVisibility(View.GONE);
//                        pickupLinearLayout.setVisibility(View.VISIBLE);
//                        pickupImageView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.accent_color));
//
//                        pickupLinearLayout.performClick();
//                    }
//                }

                parseCartJson(jObj.getJSONArray("viewcart"));

                try {
                    total_price = Double.parseDouble(jObj.getString("order_total"));
                    subTotalOriginal = subTotalDouble = Double.parseDouble(jObj.getString("order_total"));
                } catch (NumberFormatException ex) {
                    total_price = 0.0;
                    subTotalDouble = 0.0;
                }

//                        finalAmountText.setText(total_price + " kr");
                subTotalTextView.setText(convertCurrencyToDanish(subTotalDouble) + " kr");

//                if (MainActivity.ORDER_NO.isEmpty()) {
                JSONObject jsonObject = jObj.getJSONObject("result").
                        getJSONObject("userprofile");

                firstNameOriginal = firstName = jsonObject.getString("first_name");
                lastNameOriginal = lastName = jsonObject.getString("last_name");
                streetNumberOriginal = street = jsonObject.getString("street");
                if (jsonObject.has("floor_door"))
                    floorDoorOriginal = floorDoorString = jsonObject.getString("floor_door");
                postalCodeOriginal = postal_code = jsonObject.getString("postal_code");
                cityOriginal = city = jsonObject.getString("city");
                phoneNumberOriginal = telephone_no = jsonObject.getString("telephone_no");

                cartFirstName.setText(firstName);
                cartLastName.setText(lastName);
                cartStreetName.setText(street);
                cartFloorDoorName.setText(floorDoorString);
                cartPostelCode.setText(postal_code);
                cartCityName.setText(city);
                cartTelephone.setText(telephone_no);

                if (shippingTypeFlag.equalsIgnoreCase("0")) {
                    cartFirstName.setEnabled(false);
                    cartLastName.setEnabled(false);
                    cartStreetName.setEnabled(false);
                    cartFloorDoorName.setEnabled(false);
                    cartPostelCode.setEnabled(false);
                    cartCityName.setEnabled(false);
                    cartTelephone.setEnabled(false);
                } else {
                    cartFirstName.setEnabled(true);
                    cartLastName.setEnabled(true);
                    cartStreetName.setEnabled(true);
                    cartFloorDoorName.setEnabled(true);
                    cartPostelCode.setEnabled(true);
                    cartCityName.setEnabled(true);
                    cartTelephone.setEnabled(true);
                }

                if (jObj.has("shipping_unit")) {
                    shippingUnit = jObj.getString("shipping_unit");
                    if (shippingUnit == null || shippingUnit.equalsIgnoreCase("null")) {
                        // Don't show Shipping Charge
                        shippingChargeLinearLayout.setVisibility(View.GONE);
                    } else {
                        shippingChargeLinearLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Don't show Shipping Charge
                    shippingChargeLinearLayout.setVisibility(View.GONE);
                }

                if (jObj.getJSONObject("result").has("shipping_charge_details")) {
                    JSONArray shippingChargeJsonArray = jObj.getJSONObject("result").getJSONArray("shipping_charge_details");
                    mShippingChargeObjects = new Gson().fromJson(String.valueOf(shippingChargeJsonArray), new TypeToken<ArrayList<ShippingChargeObject>>() {
                    }.getType());
                    mShippingChargeAdapter = new ShippingChargeAdapter(getActivity(), mShippingChargeObjects);
//                            mShippingChargeAdapter.notifyDataSetChanged();
                    Log.e(TAG, "" + mShippingChargeObjects);
                }
//                } else {
//                    JSONArray userInfoArray = jObj.getJSONArray("order");
//
//                    JSONObject jsonObjectUserInfo = (JSONObject) userInfoArray.get(0);
//
//                    paymentMethodText.setText(jsonObjectUserInfo.getString("pm_name"));
//                    cartCommentInput.setText(jsonObjectUserInfo.getString("comments"));
//                    firstName = jsonObjectUserInfo.getString("first_name");
//                    lastName = jsonObjectUserInfo.getString("last_name");
//                    String[] addArray = jsonObjectUserInfo.getString("address").trim().split("\\s*,\\s*");
//
//                    street = addArray[0];
//                    postal_code = addArray[1];
//                    city = addArray[2];
//                    telephone_no = jsonObjectUserInfo.getString("telephone_no");
//
////                            txtpaymentName.setText(firstName + " " + lastName);
////                            txtpaymentPostalCode.setText(postal_code);
////                            txtpaymentCityName.setText(city);
////                            txtpaymentTelephoneNo.setText(telephone_no);
////                            txtpaymentStreetName.setText(street);
//
//                    cartFirstName.setText(firstName);
//                    cartLastName.setText(lastName);
//                    cartStreetName.setText(street);
//                    cartFloorDoorName.setText(floorDoorString);
//                    cartPostelCode.setText(postal_code);
//                    cartCityName.setText(city);
//                    cartTelephone.setText(telephone_no);
//
//                    if (shippingTypeFlag.equalsIgnoreCase("0")) {
//                        cartFirstName.setEnabled(false);
//                        cartLastName.setEnabled(false);
//                        cartStreetName.setEnabled(false);
//                        cartFloorDoorName.setEnabled(false);
//                        cartPostelCode.setEnabled(false);
//                        cartCityName.setEnabled(false);
//                        cartTelephone.setEnabled(false);
//                    } else {
//                        cartFirstName.setEnabled(true);
//                        cartLastName.setEnabled(true);
//                        cartStreetName.setEnabled(true);
//                        cartFloorDoorName.setEnabled(true);
//                        cartPostelCode.setEnabled(true);
//                        cartCityName.setEnabled(true);
//                        cartTelephone.setEnabled(true);
//                    }
//                }


                if (isDeliveryPresent) {
                    if (isPickupPresent) {
                        //if Both present make them VISIBLE
                        deliveryLinearLayout.setVisibility(View.VISIBLE);
                        pickupLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        //if Delivery present make it VISIBLE and Pickup GONE
                        deliveryLinearLayout.setVisibility(View.VISIBLE);
                        pickupLinearLayout.setVisibility(View.GONE);
                        deliveryImageView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.accent_color));
                    }
                    isDeliveryFlag = false;
                    deliveryLinearLayout.performClick();
                } else {
                    if (isPickupPresent) {
                        deliveryLinearLayout.setVisibility(View.GONE);
                        pickupLinearLayout.setVisibility(View.VISIBLE);
                        pickupImageView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.accent_color));

                    } else {
                        //if Both NOT present make them GONE
                        shippingMethodLinearLayout.setVisibility(View.GONE);

                        isDeliveryFlag = false;
                    }
                    pickupLinearLayout.performClick();
                }

//                        getShippingtCost(postal_code);
//                if (isDeliveryPresent) {
////                    getRestaurantPickUpDeliveryTime("Delivery");
////                    getNewShippingCost();
//                    deliveryLinearLayout.performClick();
//                } else if (isPickupPresent) {
//                    pickupLinearLayout.performClick();
////                    getRestaurantPickUpDeliveryTime("Pickup");
//                }


                JSONArray paymentArray = jObj.getJSONObject("result").getJSONArray("restpaymentmethods");
                int paymentSize = paymentArray.length();
                for (int i = 0; i < paymentSize; i++) {

                    JSONObject jsonObject1 = paymentArray.getJSONObject(i);
                    PaymentId = paymentArray.getJSONObject(0).getString("pm_id");
                    paymentMethodText.setText(paymentArray.getJSONObject(0).getString("pm_name"));
                    PaymentOption paymentOption = new PaymentOption();
                    paymentOption.setPayentImage(jsonObject1.getString("logo"));
                    paymentOption.setPayentName(jsonObject1.getString("pm_name"));
                    paymentOption.setPaymentId(jsonObject1.getString("pm_id"));
                    paymentOptions.add(paymentOption);
                }


//                        JSONArray opidJsonArray=jObj.getJSONArray("viewcart");
//                        for (int i = 0; i <opidJsonArray.length() ; i++) {
//
//                            JSONObject opIdJson=opidJsonArray.getJSONObject(i);
//                            opIdArray.add(opIdJson.getString("op_id"));
//                        }


                //stringArrayListDelivery = new ArrayList<String>();

//                        JSONArray jsonArrayDelivery = jObj.getJSONObject("result").
//                                getJSONArray("restdeltime");
//                        int jsonArrayDeliverySize = jsonArrayDelivery.length();
//
//                        for (int i = 0; i < jsonArrayDeliverySize; i++) {
//                            JSONObject jsonObject1 = jsonArrayDelivery.getJSONObject(i);
//
//                            String pdTime = jsonArrayDelivery.getJSONObject(0).getString("dt");
//                            shippingMethodText.setText(pdTime);
//                            pd_time = pdTime;
//                            stringArrayListDelivery.add(jsonObject1.getString("dt"));
//                        }
//                        restpickuptime_array = stringArrayListDelivery.
//                                toArray(new String[stringArrayListDelivery.size()]);
//
//                        JSONArray jsonArray = jObj.getJSONObject("result").
//                                getJSONArray("restpickuptime");
//                        int size = jsonArray.length();
//                        stringArrayListPickup = new ArrayList<String>();
//                        for (int i = 0; i < size; i++) {
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                            stringArrayListPickup.add(jsonObject1.getString("dt"));
//                        }

                ArrayList<String> stringArrayList = new ArrayList<String>();
                JSONArray postalArray = jObj.getJSONObject("result").getJSONArray("postal_code_details");
                for (int i = 0; i < postalArray.length(); i++) {

                    JSONObject postalJson = postalArray.getJSONObject(i);

                    String postCode = postalJson.getString("postal_code");
                    stringArrayList.add(postCode);

                }
                allPostalName_array = stringArrayList.toArray(new String[stringArrayList.size()]);

                adapter.notifyDataSetChanged();
            }

        } catch (JSONException | ArrayIndexOutOfBoundsException e) {
            Log.e("JSONException", "JSONException " + e);
            e.printStackTrace();
        }

    }

    @Override
    public void orderDetailsForCheckoutCallbackError(VolleyError volleyError) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e(TAG, "storePayment Error: " + volleyError.getMessage());
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableOrderDetails();
        }
    }

    private void showDialogInternetUnavailableOrderDetails() {
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
                getUserDetails();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated");
        //    List<PaymentMethodPojo> data = fill_with_data();

        //  init();
        //   setCustomActionBar();//for actionbar
    }

    private void getRestaurantPickUpDeliveryTime(final String pick_Type) {
        stringArrayListDelivery = new ArrayList<>();

        restpickuptime_array = stringArrayListDelivery.
                toArray(new String[stringArrayListDelivery.size()]);

        if (mSimpleArcDialog == null || !mSimpleArcDialog.isShowing()) {
            mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
            mSimpleArcDialog.show();
        }

        String tag_string_req = "RESTAURANT_PD_TIME";

        NetworkUtils.callPickUpDeliveryTime(pick_Type, tag_string_req, CartFragment.this);
    }

    @Override
    public void pickUpDeliveryTimeCallbackSuccess(String success, String pick_Type) {
        isPickupDeliveryTimeFetched = true;
        if (isShippingCostFetched) {
            if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
                mSimpleArcDialog.dismiss();
            isShippingCostFetched = false;
            isPickupDeliveryTimeFetched = false;
        }
        Log.e(TAG, "RESTAURANT_PD_TIME Response: " + success);
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
                firstValueForTime = jObj.getString("first_time");
                JSONArray jsonArray = jObj.getJSONArray("times");
                int size = jsonArray.length();

                ArrayList<String> stringArrayList = new ArrayList<String>();
                for (int i = 0; i < size; i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    String pdTime = jsonArray.getJSONObject(0).getString("dt");
                    shippingMethodText.setText(pdTime);
                    pd_time = firstValueForTime;
                    stringArrayList.add(jsonObject1.getString("dt"));
                }

                if (pick_Type.equals("Pickup"))
                    restpickuptime_array = stringArrayList.toArray(new String[stringArrayList.size()]);
                else
                    restpickuptime_array = stringArrayList.toArray(new String[stringArrayList.size()]);
            }
        } catch (JSONException e) {
            // hideDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void pickUpDeliveryTimeCallbackError(VolleyError volleyError) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e(TAG, "storePayment Error: " + volleyError.getMessage());
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableDeliveryOrPickUpTime();
        }
    }

    private void showDialogInternetUnavailableDeliveryOrPickUpTime() {
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
                getRestaurantPickUpDeliveryTime(deliveryOrPickUpString);
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

    private void add_to_cart(final JSONObject jsonData, final Context context) {
        String tag_string_req = "ADD_TO_CART";

        NetworkUtils.callCheckout(jsonData, tag_string_req, CartFragment.this);
    }

    @Override
    public void checkoutCallbackSuccess(JSONObject success) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e(TAG, "ADD_TO_CART Response: " + success);
        boolean isUserDeleted = false;
        try {
            if (ApplicationClass.getInstance().getPrefManager()
                    .getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN))
                isUserDeleted = success.getBoolean("is_user_deleted");
            boolean status = success.getBoolean("status");
            if (!isUserDeleted) {
                if (status) {
                    MainActivity.ORDER_NO = success.getString("order_no");
                    String orderTotalString = success.getString("order_total");

                    String paymentMethod = success.getString("paymethod");
                    if (paymentMethod.equalsIgnoreCase("Cash Payment")
                            || Double.parseDouble(orderTotalString) <= 0) {
                        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
                        mSimpleArcDialog.show();

                        communicatingWithRestro(MainActivity.ORDER_NO);
//                                    newCommunicatingWithRestro(MainActivity.ORDER_NO);
                    } else if (paymentMethod.equalsIgnoreCase("Online Payment")) {
                        EpayFragment fragment = new EpayFragment();
                        Bundle bundle = new Bundle();
                        //   bundle.putString(ORDER_NO, success.getString("order_no"));
                        bundle.putString(AMOUNT_TOTAL, success.getString("order_total"));
                        fragment.setArguments(bundle);
//                                    getActivity().getSupportFragmentManager().popBackStack();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                        fragmentTransaction.addToBackStack(CartFragment.class.getName());
                        fragmentTransaction.commit();
                    }
                }
            } else {
                // Deleted Account
                ApplicationClass.getInstance().getPrefManager().clearSharedPreference();

                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("deletedUser", "1");
                getActivity().startActivity(i);
                getActivity().finish();

//                                Intent intent = new Intent("DeletedAccount");
//                                // You can also include some extra data.
//                                intent.putExtra("deletedUser", "1");
//                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void checkoutCallbackError(VolleyError volleyError) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        //   Log.e(TAG, "getAllCharity Error: " + error.getMessage());
//        Toast.makeText(getActivity(),
//                volleyError.getMessage(), Toast.LENGTH_LONG).show();

        String msg = getResources().getString(R.string.try_again);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    int count = 0;

    private void communicatingWithRestro(final String orderNo) {
        setBackgroundImageDialog();

        final String tag_string_req = "communicatingWithRestro";

        NetworkUtils.callCommunicatingWithRestaurant(orderNo, tag_string_req, CartFragment.this);
    }

    @Override
    public void communicatingWithRestaurantCallbackSuccess(String success, String requestTag) {
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            Log.e("Res accepted", "response " + success);

            if (status) {
                if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
                    mSimpleArcDialog.dismiss();

                // TODO: 3/19/2017 Commented as we have to show this below Accept or Reject Dialog
//                        if (communicationPageDialog != null && communicationPageDialog.isShowing())
//                            communicationPageDialog.dismiss();

                ApplicationClass.getInstance().cancelPendingRequests(requestTag);

//                        CashPaymentDialog cartDialogFragmentClass = new CashPaymentDialog();

                Bundle bundle = new Bundle();
                bundle.putString("order_status", jObj.getString("order_status"));
                if (jObj.has("time"))
                    bundle.putString("time", jObj.getString("time"));
                if (jObj.has("reject_reason"))
                    bundle.putString("reject_Reason", jObj.getString("reject_reason"));

                orderConfirmRejectDialog(bundle);

//                        cartDialogFragmentClass.setArguments(bundle);
//                        cartDialogFragmentClass.show(getActivity().getSupportFragmentManager(), "Cash");
            } else {
                String msg = jObj.getString("msg");
                msgTextView.setText(msg);
//                        configuration.setText(msg);
//                        mSimpleArcDialog.setConfiguration(configuration);
//                        TextView tv = mSimpleArcDialog.getLoadingTextView();
//                        tv.setText(msg);
//                        Resources r = getResources();
//                        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());
//                        configuration.setArcWidthInPixel((int) px);
//                        mSimpleArcDialog.setConfiguration(configuration);
                count = count + 1;
                Log.e("Called", "Called" + count);

                communicatingWithRestro(MainActivity.ORDER_NO);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
            Log.e(TAG, "communicatingWithRestro Error: " + ex);
        }
    }

    @Override
    public void communicatingWithRestaurantCallbackError(VolleyError volleyError) {
        mSimpleArcDialog.dismiss();
        Log.e(TAG, "communicatingWithRestro Error: " + volleyError.getMessage());
    }

    private void orderConfirmRejectDialog(Bundle bundle) {
        EpayFragment.acceptPayment = false;

        String orderStatus = "";
        String rejectReason = "";
        String expectedTime = "";

        if (bundle != null) {
            orderStatus = bundle.getString("order_status");
            rejectReason = bundle.getString("reject_Reason");
            expectedTime = bundle.getString("time");
        }

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(dk.eatmore.demo.R.layout.order_confirmation_activity, null);

        ImageView acceptRejectImageView = (ImageView) view.findViewById(dk.eatmore.demo.R.id.iv_accept_reject);
        TextView dialogOrderId = (TextView) view.findViewById(dk.eatmore.demo.R.id.dialogOrderId);
        TextView dialogExpectedTime = (TextView) view.findViewById(dk.eatmore.demo.R.id.dialogExpectedTime);
        TextView dialogHeaderText = (TextView) view.findViewById(dk.eatmore.demo.R.id.dialogHeaderText);
        LinearLayout dialogHeader = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.dialogHeader);
        TextView dialogConfirmText = (TextView) view.findViewById(dk.eatmore.demo.R.id.dialogConfirmText);
        LinearLayout orderNoLayout = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.orderNoLayout);
        LinearLayout expextedTimeLayout = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.expextedTimeLayout);
        TextView anyQuestionsCallUsTextView = (TextView) view.findViewById(dk.eatmore.demo.R.id.tv_any_questions_call_us);
        String resContactNumber = ApplicationClass.getInstance().getPrefManager().
                getStringPreferences(MyPreferenceManager.restaurant_num);
        anyQuestionsCallUsTextView.setText(resContactNumber);


        LinearLayout rejectReasonLayout = (LinearLayout) view.findViewById(dk.eatmore.demo.R.id.ll_reject_reason);
        TextView rejectReasonTextView = (TextView) view.findViewById(dk.eatmore.demo.R.id.tv_reject_reason);

        builder.setView(view);
        final android.support.v7.app.AlertDialog orderConfirmRejectDialog = builder.create();
        orderConfirmRejectDialog.setCancelable(false);

        dialogOrderId.setText(MainActivity.ORDER_NO);
        MainActivity.ORDER_NO = "";

        if (orderStatus.equalsIgnoreCase("Accepted")) {
            dialogExpectedTime.setText(expectedTime);
            acceptRejectImageView.setImageResource(dk.eatmore.demo.R.drawable.ic_check);
            rejectReasonLayout.setVisibility(View.GONE);
            expextedTimeLayout.setVisibility(View.VISIBLE);
        } else if (orderStatus.equals("Rejected")) {
            dialogHeaderText.setText(dk.eatmore.demo.R.string.order_rejection);
            rejectReasonTextView.setText(rejectReason);
            acceptRejectImageView.setImageResource(dk.eatmore.demo.R.drawable.ic_error);
            dialogHeader.setBackgroundColor(Color.parseColor("#CF2A28"));
            dialogConfirmText.setText(getResources().getString(dk.eatmore.demo.R.string.order_rejected));
            expextedTimeLayout.setVisibility(View.GONE);
            rejectReasonLayout.setVisibility(View.VISIBLE);
        }
        orderConfirmRejectDialog.show();

        Button mMainMenuBtn = (Button) view.findViewById(dk.eatmore.demo.R.id.btnBackToMain);

        final String finalOrderStatus = orderStatus;
        mMainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (communicationPageDialog != null && communicationPageDialog.isShowing())
                    communicationPageDialog.dismiss();

                if (orderConfirmRejectDialog.isShowing())
                    orderConfirmRejectDialog.dismiss();

                Intent intent = new Intent("CartCount");
                // You can also include some extra data.
                intent.putExtra("count", "007");

//                getActivity().getSupportFragmentManager().popBackStack();
//                if (finalOrderStatus.equalsIgnoreCase("Accepted")) {
                // Removing Checkout and Cart Fragments
//                    getActivity().getSupportFragmentManager().popBackStack();
//                } else if (finalOrderStatus.equals("Rejected")) {
                intent.putExtra("orderRejected", "1");
//                    getActivity().getSupportFragmentManager().popBackStack();
//                }

//                Fragment fragment = new MenuFragment();
//                FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
//                FragmentTransaction myorder = fragmentManager.beginTransaction();
//                myorder.replace(R.id.fragment_container, fragment);
//                myorder.addToBackStack(InfoBarAdapter.class.getName());
//                myorder.commit();

                // Need to check this below order I think it was put to redirect to Menu after order was completed but now we redirect to Order Details screen may be useful for order reject
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

                if (communicationPageDialog != null && communicationPageDialog.isShowing())
                    communicationPageDialog.dismiss();

                if (orderConfirmRejectDialog.isShowing())
                    orderConfirmRejectDialog.dismiss();
            }
        });

    }

    private Dialog communicationPageDialog;
    private TextView msgTextView;

    private void setBackgroundImageDialog() {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();

        if (communicationPageDialog == null) {
            communicationPageDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//        communicationPageDialog.getWindow().setBackgroundDrawableResource(R.drawable.ic_background);
            communicationPageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            communicationPageDialog.setCancelable(false);
            communicationPageDialog.setContentView(dk.eatmore.demo.R.layout.layout_communication_page);
            msgTextView = (TextView) communicationPageDialog.findViewById(dk.eatmore.demo.R.id.tv_communication_page);

            communicationPageDialog.show();
        }
    }

    @Override
    public void onEventCompleted(JSONObject success) {

    }

    @Override
    public void onEventFailure() {

    }

    private void getNewShippingCost() {
        if (mSimpleArcDialog == null || !mSimpleArcDialog.isShowing()) {
            mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
            mSimpleArcDialog.show();
        }
        shippingAddressCost = 0.0;
        String tag_string_req = "GET_SHIPPING_COST";

        Map<String, String> params = new HashMap<String, String>();
        params.put("r_token", ApiCall.R_TOKEN);
        params.put("r_key", ApiCall.R_KEY);
        params.put("order_total", "" + subTotalDouble);
        if (shippingTypeFlag.equalsIgnoreCase("1")) {
            params.put("address", street + ", " + postal_code + " " + city);
        } else if (shippingTypeFlag.equalsIgnoreCase("0")) {
            params.put("address", streetNumberOriginal + ", " + postalCodeOriginal + " " + cityOriginal);
        }
        params.put("shipping", "Delivery");
        JSONObject jsonParams = new JSONObject(params);

        NetworkUtils.callShippingCost(jsonParams, tag_string_req, CartFragment.this);
    }

    @Override
    public void shippingCostCallbackSuccess(JSONObject success) {
        isShippingCostFetched = true;
        if (isPickupDeliveryTimeFetched) {
            if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
                mSimpleArcDialog.dismiss();
            isShippingCostFetched = false;
            isPickupDeliveryTimeFetched = false;
        }
        Log.e(TAG, "GET_SHIPPING_COST Response: " + success);
        try {
            boolean status = success.getBoolean("status");
            // Check for error node in json
            if (status) {
                wrongDeliveryAddress = false;
                wrongDeliveryAddressFromAPI = false;
                shippingDistance = success.getString("distance");
                shippingAddressCost = Double.parseDouble
                        (success.getString("data"));

                if (shippingOriginal == 0) {
                    shippingOriginal = shippingAddressCost;
                    totalOriginal = subTotalDouble - discountAmountDouble + shippingAddressCost;
                    wrongDeliveryAddressFromAPIOriginal = false;
                }

                shippingAmountTextView.setText(convertCurrencyToDanish(shippingAddressCost) + " kr");
                shippingAmountLinearLayout.setVisibility(View.VISIBLE);

//                        finalAmountText.setText("" + (total_price + shippingAddressCost) + " kr");
//                        totalAmountTextView.setText((total_price - discountAmountDouble + shippingAddressCost) + " kr");
                totalAmountTextView.setText(convertCurrencyToDanish(subTotalDouble - discountAmountDouble + shippingAddressCost) + " kr");
            } else {
                String msg = success.getString("msg");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                wrongDeliveryAddress = true;
                wrongDeliveryAddressFromAPI = true;
                wrongDeliveryMsg = msg;
                callAlertDialog();

                shippingAddressCost = 0.0;
                shippingAmountLinearLayout.setVisibility(View.GONE);
//                                shippingAmountTextView.setText(shippingAddressCost + " kr");

                totalAmountTextView.setText(convertCurrencyToDanish(subTotalDouble - discountAmountDouble + shippingAddressCost) + " kr");
            }
        } catch (JSONException e) {
            // hideDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void shippingCostCallbackError(VolleyError volleyError) {

        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableShippingCost();
        }
    }

    private void showDialogInternetUnavailableShippingCost() {
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
                getNewShippingCost();
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

    private void applyCouponsGiftCard(final String promoId) {
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mSimpleArcDialog.show();
        String tag_string_req = "COUPONS_REVEAL";

        Map<String, String> params = new HashMap<String, String>();
        params.put("r_token", ApiCall.R_TOKEN);
        params.put("r_key", ApiCall.R_KEY);
        params.put("order_total", "" + subTotalDouble);
        params.put("code", promoId);
        if (isDeliveryFlag)
            params.put("shipping", "Delivery");
        else
            params.put("shipping", "Pickup");
//                if (floorDoorString != null && !floorDoorString.isEmpty())
//                    params.put("address", street + " " + floorDoorString + ", " + postal_code + " " + city);
//                else
        if (shippingTypeFlag.equalsIgnoreCase("1")) {
            params.put("address", street + ", " + postal_code + " " + city);
        } else if (shippingTypeFlag.equalsIgnoreCase("0")) {
            params.put("address", streetNumberOriginal + ", " + postalCodeOriginal + " " + cityOriginal);
        }
        params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
                .getStringPreferences(MyPreferenceManager.KEY_USER_ID));

        NetworkUtils.callApplyCodeGiftCard(params, tag_string_req, CartFragment.this);
    }

    @Override
    public void applyCodeGiftCardCallbackSuccess(String success) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e(TAG, "COUPONS_REVEAL Response: " + success);
        try {
            JSONObject jObj = new JSONObject(success);
            boolean status = jObj.getBoolean("status");
            // Check for error node in json
            if (status) {
//                                JSONObject datObj = jObj.getJSONObject("result");

                discount_amount = jObj.getString("discount_amount");
                discount_type = jObj.getString("discount_type");
                discount_id = jObj.getString("discount_id");
                String shippingCost = jObj.getString("shipping_charge");
                shippingAddressCost = Double.parseDouble(shippingCost);
                String order_total = jObj.getString("order_total");
                String msg = jObj.getString("msg");
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                discountAmountDouble = Double.parseDouble(discount_amount);

                inputGiftCoupons.setText("");

                if (discountAmountDouble > 0) {
                    discountCheckoutLinearLayout.setVisibility(View.VISIBLE);
//                                    discountAmountText.setText(formatValueToMoney(Double.parseDouble(discount_amount)) + " kr");
//                                    shippingAmountTextView.setText(formatValueToMoney(shippingAddressCost) + " kr");
//
//                                    totalAmountTextView.setText(formatValueToMoney(Double.parseDouble(order_total)) + " kr");

                    discountAmountText.setText(convertCurrencyToDanish(Double.parseDouble(discount_amount)) + " kr");
                    shippingAmountTextView.setText(convertCurrencyToDanish(shippingAddressCost) + " kr");

                    totalAmountTextView.setText(convertCurrencyToDanish(Double.parseDouble(order_total)) + " kr");
                } else {
                    discountCheckoutLinearLayout.setVisibility(View.GONE);
                }
//                                Toast.makeText(getActivity(), "Coupons/Gift card is applied", Toast.LENGTH_SHORT).show();
            } else {
                String msg = jObj.getString("msg");
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                discountCheckoutLinearLayout.setVisibility(View.GONE);
                discountAmountText.setText("0" + " kr");
            }
//                            Toast.makeText(getActivity(), "Coupons/Gift card is invalid", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            // hideDialog();
            e.printStackTrace();
        }

    }

    @Override
    public void applyCodeGiftCardCallbackError(VolleyError volleyError) {
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        Log.e(TAG, "COUPONS_REVEAL Error: " + volleyError.getMessage());
        if (volleyError instanceof NoConnectionError) {
            String msg = getResources().getString(R.string.internet_unavailable_message);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }else{
            String msg = getResources().getString(R.string.try_again);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendOrderTransaction() {
        mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
        mSimpleArcDialog.show();

        String tag_string_req = "OrderTransaction";

        Map<String, String> params = new HashMap<String, String>();
        params.put("r_token", ApiCall.R_TOKEN);
        params.put("r_key", ApiCall.R_KEY);
        params.put("order_no", MainActivity.ORDER_NO);
        params.put("cardno", EpayFragment.CARD_NO);
        params.put("txnid", EpayFragment.TAXN_ID);
        params.put("txnfee", EpayFragment.TAX_FEE);
        params.put("customer_id", ApplicationClass.getInstance().getPrefManager()
                .getStringPreferences(MyPreferenceManager.KEY_USER_ID));
//        params.put("cartproducts", cartProdIds);

        JSONObject requestParam = new JSONObject(params);
        try {
            requestParam.put("cartproducts", cartProdIds);
            NetworkUtils.callOrderTransaction(requestParam, tag_string_req, CartFragment.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void orderTransactionCallbackSuccess(JSONObject success) {
//                        mSimpleArcDialog.dismiss();
        Log.e(TAG, "ORDER_TRANSACTION Response: " + success);
        try {
            boolean status = success.getBoolean("status");
            // Check for error node in json
            if (status) {
                communicatingWithRestro(MainActivity.ORDER_NO);
//                                Intent intent = new Intent("CartCount");
//                                // You can also include some extra data.
//                                intent.putExtra("count", "0");
//                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//                                MainActivity.ORDER_NO = "";
            }
        } catch (JSONException e) {
            // hideDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void orderTransactionCallbackError(VolleyError volleyError) {
        Log.e("printOrder", "printOrder Error: " + volleyError.getMessage());
        if (mSimpleArcDialog != null && mSimpleArcDialog.isShowing())
            mSimpleArcDialog.dismiss();
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableOrderTransaction();
        }
    }

    private void showDialogInternetUnavailableOrderTransaction() {
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
                sendOrderTransaction();
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

    public SimpleArcDialog getProgressDialog(Context mContext, String msg) {
        mSimpleArcDialog = new SimpleArcDialog(mContext);
        mSimpleArcDialog.setCancelable(false);

        configuration = new ArcConfiguration(mContext);
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        if (msg != null)
            configuration.setText(msg);
        else
            configuration.setText(getString(dk.eatmore.demo.R.string.please_wait));
        configuration.setAnimationSpeedWithIndex(SimpleArcLoader.SPEED_FAST);
        int color[] = {Color.parseColor("#019E0F"), Color.parseColor("#019E0F")};
        configuration.setColors(color);
        mSimpleArcDialog.setConfiguration(configuration);

        return mSimpleArcDialog;
    }

    private BroadcastReceiver mOnlinePaymentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String onlinePayment = intent.getStringExtra("onlinePayment");

            if (onlinePayment.equals("1")) {
//                mSimpleArcDialog = new ProgressDialaogView().getProgressDialog(getActivity());
//                mSimpleArcDialog.show();
//                communicatingWithRestro(MainActivity.ORDER_NO);

                sendOrderTransaction();
            } else {
                // TODO: 2/22/2017 Payment Done But something is not correct
            }

        }
    };

    private void callAlertDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(dk.eatmore.demo.R.layout.dilaog_info_layout, null);

        TextView textViewTitle = (TextView) view.findViewById(dk.eatmore.demo.R.id.textviewinfoTitle);
        Button butonOk = (Button) view.findViewById(dk.eatmore.demo.R.id.buttonAaddtocartOk);
        TextView dialogText = (TextView) view.findViewById(dk.eatmore.demo.R.id.addedtocartText);
        textViewTitle.setText(dk.eatmore.demo.R.string.alert);
        builder.setView(view);
        dialogText.setTypeface(null, Typeface.BOLD);
        dialogText.setText(wrongDeliveryMsg);

        final android.support.v7.app.AlertDialog forgotDialog = builder.create();
        forgotDialog.show();


        butonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotDialog.dismiss();
            }
        });
    }

    @Override
    public void cancelOrderTransactionCallbackSuccess(String success) {
        Log.e(TAG, success);
    }

    @Override
    public void cancelOrderTransactionCallbackError(VolleyError volleyError) {
        if (volleyError instanceof NoConnectionError) {
            showDialogInternetUnavailableCancelOrder();
        }
    }

    private void showDialogInternetUnavailableCancelOrder() {
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
                String reqTag = "CancelOrderTrabsaction";
                NetworkUtils.callCancelOrderTransaction(reqTag, CartFragment.this);
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
}


