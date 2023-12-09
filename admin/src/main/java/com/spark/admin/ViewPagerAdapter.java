package com.spark.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.spark.admin.ui.FragmentNonVerify;
import com.spark.admin.ui.FragmentPremium;
import com.spark.admin.ui.FragmentShop;
import com.spark.admin.ui.FragmentVerify;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 : return new FragmentShop();
            case 1 : return new FragmentVerify();
            case 2 : return new FragmentNonVerify();
            case 3 : return new FragmentPremium();
            default: return new FragmentShop();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}
