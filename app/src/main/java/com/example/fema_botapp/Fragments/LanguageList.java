package com.example.fema_botapp.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fema_botapp.Charts;
import com.example.fema_botapp.R;

public class LanguageList extends Fragment implements View.OnClickListener {

    TextView selected_language_button,english_button,swahili_button,text_message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_language_list, container, false);

        selected_language_button = view.findViewById(R.id.selected_language_button);
        english_button = view.findViewById(R.id.english_button);
        swahili_button = view.findViewById(R.id.swahili_button);
        text_message = view.findViewById(R.id.text_message);

        selected_language_button.setVisibility(View.GONE);

        english_button.setOnClickListener(this);
        swahili_button.setOnClickListener(this);


        return  view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.english_button:
                callEnglishButton();
                break;
            case R.id.swahili_button:
                callSwahiliButton();
                break;
        }
    }

    private void callSwahiliButton() {
        selected_language_button.setText("swahili");
        selected_language_button.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(),Charts.class);
                intent.putExtra("lang",selected_language_button.getText().toString());
                startActivity(intent);
                getActivity().finish();
            }
        },800);

    }

    private void callEnglishButton() {
        selected_language_button.setText("english");

        selected_language_button.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(),Charts.class);
                intent.putExtra("lang",selected_language_button.getText().toString());
                startActivity(intent);
                getActivity().finish();
            }
        },800);
    }
}