package dk.eatmore.demo.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dk.eatmore.demo.R;
import dk.eatmore.demo.activity.MainActivity;
import dk.eatmore.demo.adapter.ProductListAdapter;
import dk.eatmore.demo.model.MenuItemPojo;
import dk.eatmore.demo.model.menuPojo;

import java.util.ArrayList;
import java.util.List;

public class MenuerFragment extends Fragment {
    private View view;
    private ArrayList<MenuItemPojo> menuItem;
    private RecyclerView mRecyclerView;
    private RelativeLayout errorLayout;

    public MenuerFragment() {

    }

    public void upDateData(menuPojo data) {
        this.menuItem = data.getMenuItem();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstaceState) {
        view = inflater.inflate(R.layout.fragment_speeddial_layout, parent, false);
        // Showing Bottom Bar as it is Login screen
        ((MainActivity) getActivity()).showBottomNavigation();
        bindViews();

        return view;
    }

    private void bindViews() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_layout);
        errorLayout.setVisibility(View.GONE);
        TextView txt_error_message = (TextView) view.findViewById(R.id.txt_error_message);
        txt_error_message.setText(R.string.no_product_in_category);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        setDataToView();
    }

    private void setDataToView() {
        if (getItems() != null && getItems().size() <= 0)
            errorLayout.setVisibility(View.VISIBLE);
        else
            errorLayout.setVisibility(View.GONE);

//        PizzaFragmentAdapter mAdapter = new PizzaFragmentAdapter(getActivity(), getItems());
        ProductListAdapter mAdapter = new ProductListAdapter(getActivity(), getItems());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private List<MenuItemPojo> getItems() {
        return menuItem;
    }

}
