package dk.eatmore.demo.fragments;

import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.application.ApplicationClass;
import dk.eatmore.demo.adapter.InfoBarAdapter;
import dk.eatmore.demo.model.InfoBarPojo;
import dk.eatmore.demo.myutils.MyPreferenceManager;
import dk.eatmore.demo.myutils.SharedPrefClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InfoBarFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private TextView toolbarTitle;

    private SharedPrefClass mSharedPrefClass = null;

    public static InfoBarFragment newInstance() {
        return new InfoBarFragment();
    }

    public InfoBarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstaceState) {
        view = inflater
                .inflate(dk.eatmore.demo.R.layout.activity_info_bar, parent, false);
        // Showing Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).showBottomNavigation();
        //view.setLayoutManager(new LinearLayoutManager(getActivity()));
//
        ImageView resImage = (ImageView) view.findViewById(dk.eatmore.demo.R.id.tool_logo);
        ImageView imgback = (ImageView) view.findViewById(dk.eatmore.demo.R.id.imgback);
        imgback.setVisibility(View.INVISIBLE);
        String resImg = ApplicationClass.getInstance().getPrefManager().
                getStringPreferences(MyPreferenceManager.RESTAURANT_IMAGE);

        Picasso.with(getActivity())
                .load(resImg)
                .into(resImage);

        TextView resNameTextView = (TextView) view.findViewById(dk.eatmore.demo.R.id.tootlbar_title);
        String resName = ApplicationClass.getInstance().getPrefManager().
                getStringPreferences(MyPreferenceManager.RESTAURANT_NAME);
        resNameTextView.setText(resName);

        mRecyclerView = (RecyclerView) view.findViewById(dk.eatmore.demo.R.id.recyclerview12);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<InfoBarPojo> data = fill_with_data();

        InfoBarAdapter adapter = new InfoBarAdapter(data, getActivity());
        mRecyclerView.setAdapter(adapter);
        setRetainInstance(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public List<InfoBarPojo> fill_with_data() {
        List<InfoBarPojo> data = new ArrayList<>();

        mSharedPrefClass = new SharedPrefClass(getActivity());
        if (ApplicationClass.getInstance().getPrefManager().getBooleanPreferences(MyPreferenceManager.KEY_IS_LOGGEDIN)) {
            if (mSharedPrefClass.isFacebookLogin()) {
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.my_account),
                        dk.eatmore.demo.R.drawable.my_accpont_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.my_orders),
                        dk.eatmore.demo.R.drawable.my_order_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.giftcards),
                        dk.eatmore.demo.R.drawable.gift_card_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.call),
                        dk.eatmore.demo.R.drawable.call_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.Opening_Hours),
                        dk.eatmore.demo.R.drawable.opening_hour_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.directions),
                        dk.eatmore.demo.R.drawable.direction_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.contact_us),
                        dk.eatmore.demo.R.drawable.ic_contactus_myaccount, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.about_us),
                        dk.eatmore.demo.R.drawable.ic_about_us, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.How_it_works),
                        dk.eatmore.demo.R.drawable.how_it_works_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.payment_method),
                        dk.eatmore.demo.R.drawable.payment_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.control_support),
                        dk.eatmore.demo.R.drawable.kontrol_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.terms_of_service),
                        dk.eatmore.demo.R.drawable.ic_terms_of_service, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.log_out),
                        dk.eatmore.demo.R.drawable.logout_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
            } else {
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.my_account),
                        dk.eatmore.demo.R.drawable.my_accpont_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.my_orders),
                        dk.eatmore.demo.R.drawable.my_order_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.giftcards),
                        dk.eatmore.demo.R.drawable.gift_card_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.call),
                        dk.eatmore.demo.R.drawable.call_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.Opening_Hours),
                        dk.eatmore.demo.R.drawable.opening_hour_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.directions),
                        dk.eatmore.demo.R.drawable.direction_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.contact_us),
                        dk.eatmore.demo.R.drawable.ic_contactus_myaccount, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.about_us),
                        dk.eatmore.demo.R.drawable.ic_about_us, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.How_it_works),
                        dk.eatmore.demo.R.drawable.how_it_works_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.payment_method),
                        dk.eatmore.demo.R.drawable.payment_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.control_support),
                        dk.eatmore.demo.R.drawable.kontrol_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.terms_of_service),
                        dk.eatmore.demo.R.drawable.ic_terms_of_service, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.change_password),
                        dk.eatmore.demo.R.drawable.change_password, dk.eatmore.demo.R.drawable.ic_next_arrow));
                data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.log_out),
                        dk.eatmore.demo.R.drawable.logout_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
            }
        } else {
            data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.please_login),
                    dk.eatmore.demo.R.drawable.my_accpont_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
            data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.call),
                    dk.eatmore.demo.R.drawable.call_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
            data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.Opening_Hours),
                    dk.eatmore.demo.R.drawable.opening_hour_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
            data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.directions),
                    dk.eatmore.demo.R.drawable.direction_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
            data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.contact_us),
                    dk.eatmore.demo.R.drawable.ic_contactus_myaccount, dk.eatmore.demo.R.drawable.ic_next_arrow));
            data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.about_us),
                    dk.eatmore.demo.R.drawable.ic_about_us, dk.eatmore.demo.R.drawable.ic_next_arrow));
            data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.How_it_works),
                    dk.eatmore.demo.R.drawable.how_it_works_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
            data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.payment_method),
                    dk.eatmore.demo.R.drawable.payment_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
            data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.control_support),
                    dk.eatmore.demo.R.drawable.kontrol_new, dk.eatmore.demo.R.drawable.ic_next_arrow));
            data.add(new InfoBarPojo(getActivity().getResources().getString(dk.eatmore.demo.R.string.terms_of_service),
                    dk.eatmore.demo.R.drawable.ic_terms_of_service, dk.eatmore.demo.R.drawable.ic_next_arrow));
        }

        return data;
    }

}