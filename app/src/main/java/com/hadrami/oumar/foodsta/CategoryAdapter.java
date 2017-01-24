package com.hadrami.oumar.foodsta;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by oumar on 26/12/2016.
 */
public class CategoryAdapter extends FragmentPagerAdapter {



    public CategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1) {
            return new FavoriteFragment();
        } else {
            return new AccountFragment();
        }
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Home";
        } else if (position == 1) {
            return "Favorites";
        } else {
            return "Account";
        }
    }
}
