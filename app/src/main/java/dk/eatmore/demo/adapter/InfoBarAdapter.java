package dk.eatmore.demo.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.activity.LoginActivity;
import dk.eatmore.demo.fragments.AboutUsFragment;
import dk.eatmore.demo.fragments.ChangePassswordFragment;
import dk.eatmore.demo.fragments.ContactUsFragment;
import dk.eatmore.demo.fragments.ControlSupportFragment;
import dk.eatmore.demo.fragments.EditProfileFragment;
import dk.eatmore.demo.fragments.GiftCardsFragment;
import dk.eatmore.demo.fragments.HowItWorksFragment;
import dk.eatmore.demo.fragments.InfoBarFragment;
import dk.eatmore.demo.fragments.LoginFragment;
import dk.eatmore.demo.fragments.MyCartCheckOut;
import dk.eatmore.demo.fragments.NewMyOrdersFragment;
import dk.eatmore.demo.fragments.OpeningHoursAlertClass;
import dk.eatmore.demo.fragments.PaymentMethodFragment;
import dk.eatmore.demo.fragments.TermsServicesFragment;
import dk.eatmore.demo.interfaces.ClearCartCallback;
import dk.eatmore.demo.model.InfoBarPojo;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.SharedPrefClass;
import dk.eatmore.demo.netutils.ProgressDialaogView;
import dk.eatmore.demo.network.NetworkUtils;
import com.leo.simplearcloader.SimpleArcDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfoBarAdapter extends RecyclerView.Adapter<InfoBarAdapter.View_Holder> implements ClearCartCallback {

    private static final String TAG = InfoBarAdapter.class.getSimpleName();
    List<InfoBarPojo> list = Collections.emptyList();
    Context context;
    Intent intent;
    Fragment fragment = null;
    private int mContainerId;
    private FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    private SharedPrefClass mSharedPrefClass = null;
    private SimpleArcDialog pDialog;

    public InfoBarAdapter(List<InfoBarPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(dk.eatmore.demo.R.layout.activity_info_bar_adapter, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {

        //  InfoBarPojo infoBarPojo = list.get(position);

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(list.get(position).title);
        holder.imageViewLogo.setImageResource(list.get(position).imageIcon);
        holder.imageViewNext.setImageResource(list.get(position).imageNext);

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

//    // Insert a new item to the RecyclerView on a predefined position
//    public void insert(int position, InfoBarPojo data) {
//        list.add(position, data);
//        notifyItemInserted(position);
//    }
//
//    // Remove a RecyclerView item containing a specified Data object
//    public void remove(InfoBarPojo data) {
//        int position = list.indexOf(data);
//        list.remove(position);
//        notifyItemRemoved(position);
//    }


    public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView title;
        ImageView imageViewLogo, imageViewNext;
        ArrayList<InfoBarPojo> infoBarPojos = new ArrayList<InfoBarPojo>();
        Context cntxt;

        public View_Holder(View itemView) {
            super(itemView);
            this.infoBarPojos = infoBarPojos;
            this.cntxt = cntxt;
            itemView.setOnClickListener(this);
            cv = (CardView) itemView.findViewById(dk.eatmore.demo.R.id.infocardView);
            title = (TextView) itemView.findViewById(dk.eatmore.demo.R.id.infotitle);
            imageViewLogo = (ImageView) itemView.findViewById(dk.eatmore.demo.R.id.menuicon);
            imageViewNext = (ImageView) itemView.findViewById(dk.eatmore.demo.R.id.infoMenuImage);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            mSharedPrefClass = new SharedPrefClass(context);

            if (ApplicationClass.getInstance().getPrefManager().getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN)) {
                if (mSharedPrefClass.isFacebookLogin()) {
                    switch (position) {
                        case 0:
                            fragment = new EditProfileFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            // fragmentManager= ((Activity)context).getFragmentManager();
                            FragmentTransaction fragmentTransactionLogin = fragmentManager.beginTransaction();
                            fragmentTransactionLogin.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionLogin.addToBackStack(InfoBarFragment.class.getName());
                            fragmentTransactionLogin.commit();
                            // set the toolbar title
                            // ((FragmentActivity)getActivity()).getSupportActionBar().setTitle(title);
                            break;
                        case 1:
//                            fragment = new MyOrdersFragment();
                            fragment = new NewMyOrdersFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction myorder = fragmentManager.beginTransaction();
                            myorder.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            myorder.addToBackStack(InfoBarAdapter.class.getName());
                            myorder.commit();
                            break;

                        case 2:
                            //                            fragment = new MyGiftCardsFragment();
                            fragment = new GiftCardsFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction giftCard = fragmentManager.beginTransaction();
                            giftCard.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            giftCard.addToBackStack(InfoBarAdapter.class.getName());
                            giftCard.commit();
                            break;

                        case 3:
                            String phone = ApplicationClass.getInstance().getPrefManager()
                                    .getStringPreferences(MyPreferenceManager.restaurant_num);
                            if (phone != null) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                context.startActivity(intent);
                            } else
                                Toast.makeText(context, dk.eatmore.demo.R.string.try_again, Toast.LENGTH_SHORT).show();

                            break;

                        case 4:
                            fragment = new OpeningHoursAlertClass();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransactionOpeningHours = fragmentManager.beginTransaction();
                            fragmentTransactionOpeningHours.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionOpeningHours.addToBackStack(InfoBarAdapter.class.getName());
                            fragmentTransactionOpeningHours.commit();
                            break;

                        case 5:
                            String map = "http://maps.google.co.in/maps?q=" + ApplicationClass.getInstance().getPrefManager()
                                    .getStringPreferences(MyPreferenceManager.restaurant_ADDRESS);

                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                            context.startActivity(i);

//                    Direction     fragment1 = new Direction();
//                    // title="Explore The MindSets";
//                    fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//                    FragmentTransaction fragmentTransactionOpeningHours1 = fragmentManager.beginTransaction();
//                    fragmentTransactionOpeningHours1.replace(R.id.fragment_container, fragment1);
//                    fragmentTransactionOpeningHours1.addToBackStack(InfoBarAdapter.class.getName());
//                    fragmentTransactionOpeningHours1.commit();
                            break;

                        case 6:
                            fragment = new ContactUsFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransactionContactUs = fragmentManager.beginTransaction();
                            fragmentTransactionContactUs.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionContactUs.addToBackStack(InfoBarAdapter.class.getName());
                            fragmentTransactionContactUs.commit();
                            break;

                        case 7:
                            fragment = new AboutUsFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransactionAboutUs = fragmentManager.beginTransaction();
                            fragmentTransactionAboutUs.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionAboutUs.addToBackStack(InfoBarAdapter.class.getName());
                            fragmentTransactionAboutUs.commit();
                            break;

                        case 8:
                            fragment = new HowItWorksFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransactionHowItWorks = fragmentManager.beginTransaction();
                            fragmentTransactionHowItWorks.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionHowItWorks.addToBackStack(InfoBarAdapter.class.getName());
                            fragmentTransactionHowItWorks.commit();
                            break;
                        case 9:
                            fragment = new PaymentMethodFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction paymentMethod = fragmentManager.beginTransaction();
                            paymentMethod.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            paymentMethod.addToBackStack(InfoBarAdapter.class.getName());
                            paymentMethod.commit();
                            break;
                        case 10:
                            //Kontrolrapport
                            fragment = new ControlSupportFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction controlSupportTransaction = fragmentManager.beginTransaction();
                            controlSupportTransaction.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            controlSupportTransaction.addToBackStack(InfoBarAdapter.class.getName());
                            controlSupportTransaction.commit();
                            break;
                        case 11:
                            fragment = new TermsServicesFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransactionTerms_Services = fragmentManager.beginTransaction();
                            fragmentTransactionTerms_Services.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionTerms_Services.addToBackStack(InfoBarAdapter.class.getName());
                            fragmentTransactionTerms_Services.commit();
                            break;

                        case 12:
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                            View titleview = LayoutInflater.from(context).inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
                            TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
                            title.setText(dk.eatmore.demo.R.string.logout);
                            alertDialog.setCustomTitle(titleview);
                            //   .setMessage("Are you sure you want to delete this entry?")
                            alertDialog
                                    .setPositiveButton(dk.eatmore.demo.R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            clearCart();
//                                    ApplicationClass.getInstance().getPrefManager().clearSharedPreference();
//
//                                    Intent i = new Intent(context, LoginActivity.class);
//                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    context.startActivity(i);
//                                    ((Activity) context).finish();

                                        }
                                    })
                                    .setNegativeButton(dk.eatmore.demo.R.string.dialog_no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })

                                    .show();
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:

                            fragment = new EditProfileFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            // fragmentManager= ((Activity)context).getFragmentManager();
                            FragmentTransaction fragmentTransactionLogin = fragmentManager.beginTransaction();
                            fragmentTransactionLogin.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionLogin.addToBackStack(InfoBarFragment.class.getName());
                            fragmentTransactionLogin.commit();
                            // set the toolbar title
                            // ((FragmentActivity)getActivity()).getSupportActionBar().setTitle(title);
                            break;
                        case 1:

//                            fragment = new MyOrdersFragment();
                            fragment = new NewMyOrdersFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction myorder = fragmentManager.beginTransaction();
                            myorder.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            myorder.addToBackStack(InfoBarAdapter.class.getName());
                            myorder.commit();
                            break;
                        case 2:
//                            fragment = new MyGiftCardsFragment();
                            fragment = new GiftCardsFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction giftCard = fragmentManager.beginTransaction();
                            giftCard.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            giftCard.addToBackStack(InfoBarAdapter.class.getName());
                            giftCard.commit();
                            break;
                        case 3:

                            String phone = ApplicationClass.getInstance().getPrefManager()
                                    .getStringPreferences(MyPreferenceManager.restaurant_num);
                            if (phone != null) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                context.startActivity(intent);
                            } else
                                Toast.makeText(context, dk.eatmore.demo.R.string.try_again, Toast.LENGTH_SHORT).show();

                            break;

                        case 4:
                            fragment = new OpeningHoursAlertClass();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransactionOpeningHours = fragmentManager.beginTransaction();
                            fragmentTransactionOpeningHours.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionOpeningHours.addToBackStack(InfoBarAdapter.class.getName());
                            fragmentTransactionOpeningHours.commit();
                            break;

                        case 5:
                            String map = "http://maps.google.co.in/maps?q=" + ApplicationClass.getInstance().getPrefManager()
                                    .getStringPreferences(MyPreferenceManager.restaurant_ADDRESS);

                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                            context.startActivity(i);

//                    Direction     fragment1 = new Direction();
//                    // title="Explore The MindSets";
//                    fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//                    FragmentTransaction fragmentTransactionOpeningHours1 = fragmentManager.beginTransaction();
//                    fragmentTransactionOpeningHours1.replace(R.id.fragment_container, fragment1);
//                    fragmentTransactionOpeningHours1.addToBackStack(InfoBarAdapter.class.getName());
//                    fragmentTransactionOpeningHours1.commit();
                            break;

                        case 6:
                            fragment = new ContactUsFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransactionContactUs = fragmentManager.beginTransaction();
                            fragmentTransactionContactUs.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionContactUs.addToBackStack(InfoBarAdapter.class.getName());
                            fragmentTransactionContactUs.commit();
                            break;

                        case 7:
                            fragment = new AboutUsFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransactionAboutUs = fragmentManager.beginTransaction();
                            fragmentTransactionAboutUs.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionAboutUs.addToBackStack(InfoBarAdapter.class.getName());
                            fragmentTransactionAboutUs.commit();
                            break;

                        case 8:
                            fragment = new HowItWorksFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransactionHowItWorks = fragmentManager.beginTransaction();
                            fragmentTransactionHowItWorks.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionHowItWorks.addToBackStack(InfoBarAdapter.class.getName());
                            fragmentTransactionHowItWorks.commit();
                            break;
                        case 9:
                            fragment = new PaymentMethodFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction paymentMethod = fragmentManager.beginTransaction();
                            paymentMethod.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            paymentMethod.addToBackStack(InfoBarAdapter.class.getName());
                            paymentMethod.commit();
                            break;

                        case 10:
                            //Kontrolrapport
                            fragment = new ControlSupportFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction controlSupportTransaction = fragmentManager.beginTransaction();
                            controlSupportTransaction.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            controlSupportTransaction.addToBackStack(InfoBarAdapter.class.getName());
                            controlSupportTransaction.commit();
                            break;

                        case 11:
                            fragment = new TermsServicesFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransactionTerms_Services = fragmentManager.beginTransaction();
                            fragmentTransactionTerms_Services.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            fragmentTransactionTerms_Services.addToBackStack(InfoBarAdapter.class.getName());
                            fragmentTransactionTerms_Services.commit();
                            break;

                        case 12:
                            fragment = new ChangePassswordFragment();
                            // title="Explore The MindSets";
                            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction changePasswordTransaction = fragmentManager.beginTransaction();
                            changePasswordTransaction.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                            changePasswordTransaction.addToBackStack(InfoBarAdapter.class.getName());
                            changePasswordTransaction.commit();
                            break;

                        case 13:
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                            View titleview = LayoutInflater.from(context).inflate(dk.eatmore.demo.R.layout.custom_alert_title, null);
                            TextView title = (TextView) titleview.findViewById(dk.eatmore.demo.R.id.dialogtitle);
                            title.setText(dk.eatmore.demo.R.string.logout);
                            alertDialog.setCustomTitle(titleview);
                            //   .setMessage("Are you sure you want to delete this entry?")
                            alertDialog.setPositiveButton(dk.eatmore.demo.R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    clearCart();
                                }
                            })
                                    .setNegativeButton(dk.eatmore.demo.R.string.dialog_no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })

                                    .show();
                            break;
                    }
                }
            } else
                switch (position) {
                    case 0:
                        fragment = new LoginFragment();
//                        fragment = new LoginFragmentMain();
                        // title="Explore The MindSets";

                        Bundle bundle = new Bundle();
                        bundle.putBoolean(MyCartCheckOut.IS_FROM_CART, false);
                        fragment.setArguments(bundle);
                        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                        // fragmentManager= ((Activity)context).getFragmentManager();
                        FragmentTransaction fragmentTransactionLogin = fragmentManager.beginTransaction();
                        fragmentTransactionLogin.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                        fragmentTransactionLogin.addToBackStack(InfoBarAdapter.class.getName());
                        fragmentTransactionLogin.commit();
                        // set the toolbar title
                        // ((FragmentActivity)getActivity()).getSupportActionBar().setTitle(title);
                        break;

                    case 1:
                        String phone = ApplicationClass.getInstance().getPrefManager()
                                .getStringPreferences(MyPreferenceManager.restaurant_num);
                        if (phone != null) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                            context.startActivity(intent);
                        } else
                            Toast.makeText(context, dk.eatmore.demo.R.string.try_again, Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        fragment = new OpeningHoursAlertClass();
                        // title="Explore The MindSets";
                        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransactionOpeningHours = fragmentManager.beginTransaction();
                        fragmentTransactionOpeningHours.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                        fragmentTransactionOpeningHours.addToBackStack(InfoBarAdapter.class.getName());
                        fragmentTransactionOpeningHours.commit();
                        break;

                    case 3:
                        String map = "http://maps.google.co.in/maps?q=" + ApplicationClass.getInstance().getPrefManager()
                                .getStringPreferences(MyPreferenceManager.restaurant_ADDRESS);

                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                        context.startActivity(i);

                        break;

                    case 4:
                        fragment = new ContactUsFragment();
                        // title="Explore The MindSets";
                        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransactionContactUs = fragmentManager.beginTransaction();
                        fragmentTransactionContactUs.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                        fragmentTransactionContactUs.addToBackStack(InfoBarAdapter.class.getName());
                        fragmentTransactionContactUs.commit();
                        break;

                    case 5:
                        fragment = new AboutUsFragment();
                        // title="Explore The MindSets";
                        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransactionAboutUs = fragmentManager.beginTransaction();
                        fragmentTransactionAboutUs.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                        fragmentTransactionAboutUs.addToBackStack(InfoBarAdapter.class.getName());
                        fragmentTransactionAboutUs.commit();
                        break;

                    case 6:
                        fragment = new HowItWorksFragment();
                        // title="Explore The MindSets";
                        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransactionHowItWorks = fragmentManager.beginTransaction();
                        fragmentTransactionHowItWorks.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                        fragmentTransactionHowItWorks.addToBackStack(InfoBarAdapter.class.getName());
                        fragmentTransactionHowItWorks.commit();
                        break;
                    case 7:
                        fragment = new PaymentMethodFragment();
                        // title="Explore The MindSets";
                        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction paymentMethod = fragmentManager.beginTransaction();
                        paymentMethod.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                        paymentMethod.addToBackStack(InfoBarAdapter.class.getName());
                        paymentMethod.commit();
                        break;

                    case 8:
                        //Kontrolrapport
                        fragment = new ControlSupportFragment();
                        // title="Explore The MindSets";
                        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction controlSupportTransaction = fragmentManager.beginTransaction();
                        controlSupportTransaction.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                        controlSupportTransaction.addToBackStack(InfoBarAdapter.class.getName());
                        controlSupportTransaction.commit();
                        break;
                    case 9:
                        fragment = new TermsServicesFragment();
                        // title="Explore The MindSets";
                        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransactionTerms_Services = fragmentManager.beginTransaction();
                        fragmentTransactionTerms_Services.replace(dk.eatmore.demo.R.id.fragment_container, fragment);
                        fragmentTransactionTerms_Services.addToBackStack(InfoBarAdapter.class.getName());
                        fragmentTransactionTerms_Services.commit();
                        break;
                }
        }
    }

    private void clearCart() {
        pDialog = new ProgressDialaogView().getProgressDialog(context);
        pDialog.show();
        String tag_string_req = "fcmtoken";

        NetworkUtils.callClearCart(tag_string_req, InfoBarAdapter.this);

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ApiCall.CLEAR_ALL_CART, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (pDialog != null && pDialog.isShowing())
//                    pDialog.dismiss();
//                Log.e("respo ", "clecr " + response);
//                ApplicationClass.getInstance().getPrefManager().clearSharedPreference();
//
//                Intent i = new Intent(context, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                context.startActivity(i);
//                ((Activity) context).finish();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (pDialog != null && pDialog.isShowing())
//                    pDialog.dismiss();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("r_token", ApiCall.R_TOKEN);
//                params.put("r_key", ApiCall.R_KEY);
//                if (ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.KEY_USER_ID) != null)
//                    params.put("customer_id", ApplicationClass.getInstance().getPrefManager().
//                            getStringPreferences(MyPreferenceManager.KEY_USER_ID));
//
//                if (ApplicationClass.getInstance().getPrefManager().getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID) != null)
//                    params.put("ip", ApplicationClass.getInstance().getPrefManager().
//                            getStringPreferences(MyPreferenceManager.KEY_DEVICE_ID));
//
//                return params;
//            }
//        };
//        // Adding request to request queue
//        ApplicationClass.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void clearCartCallbackSuccess(String success) {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
        Log.e(TAG, "clecr " + success);
        ApplicationClass.getInstance().getPrefManager().clearSharedPreference();

        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
        ((Activity) context).finish();
    }

    @Override
    public void clearCartCallbackError(VolleyError volleyError) {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }
}