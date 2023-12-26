package com.spark.swarajyabiz;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.spark.swarajyabiz.ui.FragmentHome;
import com.spark.swarajyabiz.ui.FragmentShop;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentShop();
            case 1:
                return new FragmentHome();
            // Add more cases for additional tabs
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2; // Change this to the number of tabs you want
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // Return tab titles if needed
        switch (position) {
            case 0:
                return "Tab 1";
            case 1:
                return "Tab 2";
            // Add more titles for additional tabs
            default:
                return null;
        }
    }
}
