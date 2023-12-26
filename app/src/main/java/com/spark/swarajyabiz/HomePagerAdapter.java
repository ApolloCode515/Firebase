package com.spark.swarajyabiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.spark.swarajyabiz.ui.FragmentHome;

public class HomePagerAdapter extends FragmentStateAdapter {

    public HomePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 : return new FragmentHome();
            case 1 : return new FragmentHome();
            case 2 : return new FragmentHome();
            case 3 : return new FragmentHome();
            default: return new FragmentHome();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}
