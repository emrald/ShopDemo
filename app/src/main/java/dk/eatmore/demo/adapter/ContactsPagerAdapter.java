package dk.eatmore.demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dk.eatmore.demo.fragments.MenuerFragment;
import dk.eatmore.demo.model.menuPojo;

import java.util.ArrayList;

public class ContactsPagerAdapter extends FragmentStatePagerAdapter {
    MenuerFragment mMenuerFragment;
    //  private List<Fragment> mFragments;
    // private List<String> mFragmentTitles;
    ArrayList<menuPojo> arraMenu;

    public ContactsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        //  mFragmentTitles = new ArrayList<>();
        // mFragments = new ArrayList<>();

        arraMenu = new ArrayList<>();
    }

    public void addData(ArrayList<menuPojo> arraMenu) {
        this.arraMenu = arraMenu;
        //  Log.e("addData", "addData " +arraMenu.size());
    }

    @Override
    public Fragment getItem(int position) {
        mMenuerFragment = new MenuerFragment();
        mMenuerFragment.upDateData(arraMenu.get(position));
        return mMenuerFragment;
    }

    @Override
    public int getCount() {
        return arraMenu.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arraMenu.get(position).getMenuName();
    }
}
