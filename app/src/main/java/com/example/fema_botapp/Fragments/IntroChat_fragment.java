package com.example.fema_botapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fema_botapp.Adapters.IntroChatAdapter;
import com.example.fema_botapp.PojoClass.IntroChatPojo;
import com.example.fema_botapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntroChat_fragment extends Fragment {

    RecyclerView intro_chart_text;
    RecyclerView.LayoutManager linearlayout;
    IntroChatAdapter introChatAdapter;
    List<IntroChatPojo> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_chat_fragment, container, false);

        intro_chart_text = view.findViewById(R.id.intro_chart_text);
        intro_chart_text.setHasFixedSize(true);

        linearlayout = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        intro_chart_text.setLayoutManager(linearlayout);

        introChatAdapter = new IntroChatAdapter(IntroChat_fragment.this,list);
        intro_chart_text.setAdapter(introChatAdapter);

        IntroChatPojo introChatPojo = new IntroChatPojo("hello call me FEMA BOT");
        IntroChatPojo introChatPojo2 = new IntroChatPojo("let's know each other..?");

        list.addAll(Arrays.asList(introChatPojo,introChatPojo2));

        return view;
    }
}