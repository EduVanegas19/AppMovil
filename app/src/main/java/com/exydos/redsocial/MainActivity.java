package com.exydos.redsocial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Switch;

import com.exydos.redsocial.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        addTabs();
    }

    private void init(){


        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

    }

    private void addTabs(){

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.house_solid));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.magnifying_glass_solid));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.baseline_add_24));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.heart_regular));

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.house_solid);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.house_solid);
                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(R.drawable.magnifying_glass_solid);
                        break;

                    case 2:
                        tabLayout.getTabAt(0).setIcon(R.drawable.baseline_add_24);
                        break;

                    case 3:
                        tabLayout.getTabAt(0).setIcon(R.drawable.heart_solid);
                        break;

                    case 4:
                        tabLayout.getTabAt(0).setIcon(android.R.drawable.ic_menu_help);
                        break;


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.house_solid);
                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(R.drawable.magnifying_glass_solid);
                        break;

                    case 2:
                        tabLayout.getTabAt(0).setIcon(R.drawable.baseline_add_24);
                        break;

                    case 3:
                        tabLayout.getTabAt(0).setIcon(R.drawable.heart_regular);
                        break;

                    case 4:
                        tabLayout.getTabAt(0).setIcon(R.drawable.heart_solid);
                        break;


                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.house_solid);
                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(R.drawable.magnifying_glass_solid);
                        break;

                    case 2:
                        tabLayout.getTabAt(0).setIcon(R.drawable.baseline_add_24);
                        break;

                    case 3:
                        tabLayout.getTabAt(0).setIcon(R.drawable.heart_solid);
                        break;

                    case 4:
                        tabLayout.getTabAt(0).setIcon(android.R.drawable.ic_menu_help);
                        break;


                }
            }
        });
    }
}