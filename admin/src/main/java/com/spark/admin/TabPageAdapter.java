package com.spark.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabPageAdapter extends FragmentStateAdapter {
    public TabPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
//        switch (position) {
//            case 0:
//                return new FragmentHome();
//            case 1:
//                return new FragmentShop();
//            case 2:
//                return new FragmentProfile();
//            default:
//                return null;
//        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }
}
