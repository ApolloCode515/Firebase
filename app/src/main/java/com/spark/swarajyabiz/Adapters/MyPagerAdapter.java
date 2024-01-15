package com.spark.swarajyabiz.Adapters;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.spark.swarajyabiz.MyFragments.MembersFragment;
import com.spark.swarajyabiz.MyFragments.PostsFragment;
import com.spark.swarajyabiz.ui.Fragment1;
import com.spark.swarajyabiz.ui.Fragment2;

// MyPagerAdapter.java
public class MyPagerAdapter extends FragmentStateAdapter {
    public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PostsFragment();
            case 1:
               // return new SecondFragment();
                return new MembersFragment();
            default:
              //  return new FirstFragment();
                return new PostsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}
