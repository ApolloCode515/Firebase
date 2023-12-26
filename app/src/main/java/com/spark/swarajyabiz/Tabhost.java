package com.spark.swarajyabiz;

import android.os.Bundle;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

public class Tabhost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabhost);

        // initiating the tabhost
        TabHost tabhost = findViewById(R.id.tabhost);
        // setting up the tab host
        tabhost.setup();

        // Code for adding Tab 1 to the tabhost
        TabHost.TabSpec spec = tabhost.newTabSpec("Personal");
        spec.setContent(R.id.tab1);

        // setting the name of the tab 1 as "Tab One"
        spec.setIndicator("Tab One");

        // adding the tab to tabhost
        tabhost.addTab(spec);

        // Code for adding Tab 2 to the tabhost
        spec = tabhost.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);

        // setting the name of the tab 1 as "Tab Two"
        spec.setIndicator("Tab Two");
        tabhost.addTab(spec);

        // Code for adding Tab 3 to the tabhost
        spec = tabhost.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Tab Three");
        tabhost.addTab(spec);
    }
}
