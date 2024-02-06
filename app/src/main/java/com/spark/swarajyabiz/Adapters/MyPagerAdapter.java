package com.spark.swarajyabiz.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.spark.swarajyabiz.MyFragments.MembersFragment;
import com.spark.swarajyabiz.MyFragments.PostsFragment;

// MyPagerAdapter.java
public class MyPagerAdapter extends FragmentStateAdapter {
    private String commonCommId;
    public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity,String commonCommId) {
        super(fragmentActivity);
        this.commonCommId = commonCommId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return PostsFragment.newInstance(commonCommId);
            case 1:
                return MembersFragment.newInstance(commonCommId);
            default:
                return PostsFragment.newInstance(commonCommId);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}
