package com.example.fema_botapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import com.example.fema_botapp.Fragments.LanguageList;
import com.example.fema_botapp.Fragments.Loading;

public class languageSelection extends AppCompatActivity {

    FrameLayout select_language_text;
    FrameLayout select_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        select_language_text = findViewById(R.id.select_language_text);
        select_loading = findViewById(R.id.select_loading);

        select_language_text.setVisibility(View.GONE);
        select_loading.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               select_loading.setVisibility(View.GONE);
               select_language_text.setVisibility(View.VISIBLE);
            }
        },1000);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.select_loading,new Loading())
                .commit();

        FragmentManager fragmentManager1 = getSupportFragmentManager();
        fragmentManager1.beginTransaction()
                .replace(R.id.select_language_text,new LanguageList())
                .commit();

    }
}