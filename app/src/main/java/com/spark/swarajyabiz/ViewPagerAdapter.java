package com.spark.swarajyabiz;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.spark.swarajyabiz.ui.FragmentHome;
import com.spark.swarajyabiz.ui.FragmentNonVerify;
import com.spark.swarajyabiz.ui.FragmentPremium;
import com.spark.swarajyabiz.ui.FragmentShop;
import com.spark.swarajyabiz.ui.FragmentVerify;
import com.spark.swarajyabiz.ui.HomeFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 : return new HomeFragment();
            case 1 : return new FragmentVerify();
            case 2 : return new FragmentNonVerify();
            case 3 : return new FragmentPremium();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}
