package com.example.fema_botapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.fema_botapp.Adapters.FragmentAdapter;
import com.example.fema_botapp.Fragments.IntroChat_fragment;
import com.example.fema_botapp.Fragments.Loading;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Login_signup extends AppCompatActivity {

    TabLayout tab;
    ViewPager2 pageView;
    FrameLayout frame,frame2;
    LinearLayout fragments_car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        tab = findViewById(R.id.tab);
        pageView = findViewById(R.id.pageView);
        frame = findViewById(R.id.loading_frame);
        frame2 = findViewById(R.id.frame2);
        fragments_car = findViewById(R.id.fragments_car);

        frame.setVisibility(View.GONE);
        fragments_car.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                frame2.setVisibility(View.GONE);
                frame.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Animation here...
                        fragments_car.setVisibility(View.VISIBLE);
                    }
                },700);
            }
        },2000);



        FragmentManager fragmentManager2 = getSupportFragmentManager();
        fragmentManager2.beginTransaction()
                .replace(R.id.frame2,new Loading())
                .commit();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.loading_frame,new IntroChat_fragment())
                .commit();

        pageView.setAdapter(new FragmentAdapter(this));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tab, pageView, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("LOGIN");
                        break;
                    case 1:
                        tab.setText("SIGNUP");
                }
            }
        });
        tabLayoutMediator.attach();

    }
}