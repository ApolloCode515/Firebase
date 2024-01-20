package com.spark.swarajyabiz.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.spark.swarajyabiz.FragmentGlobalCommunity;
import com.spark.swarajyabiz.FragmentMyCommunity;
import com.spark.swarajyabiz.MyFragments.MembersFragment;
import com.spark.swarajyabiz.MyFragments.PostsFragment;

public class CommunityPagerAdapter extends FragmentStateAdapter {
    private String commonCommId;
    public CommunityPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return FragmentMyCommunity.newInstance();
            case 1:
                return FragmentGlobalCommunity.newInstance();
            default:
                return FragmentMyCommunity.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}