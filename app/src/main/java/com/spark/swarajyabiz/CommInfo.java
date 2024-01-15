package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.spark.swarajyabiz.Adapters.MyPagerAdapter;

public class CommInfo extends AppCompatActivity {
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_info);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout=findViewById(R.id.tabLayout);

        MyPagerAdapter adapter = new MyPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTabView = getLayoutInflater().inflate(R.layout.custom_tab_layout, null);
            TextView tabTextView = customTabView.findViewById(R.id.tabTextView);
            // Customize tab labels using a switch statement
            switch (position) {
                case 0:
                    tabTextView.setText("Posts");
                    tab.setCustomView(customTabView);
                    break;
                case 1:
                    tabTextView.setText("Members");
                    tab.setCustomView(customTabView);
                    break;
                // Add more cases as needed
            }
        }).attach();

    }
}