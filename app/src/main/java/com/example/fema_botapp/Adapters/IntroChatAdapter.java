package com.example.fema_botapp.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fema_botapp.Fragments.IntroChat_fragment;
import com.example.fema_botapp.PojoClass.IntroChatPojo;
import com.example.fema_botapp.R;

import java.util.List;

public class IntroChatAdapter extends RecyclerView.Adapter<IntroChatAdapter.ViewHolderClass> {
    IntroChat_fragment context;
    List<IntroChatPojo> list;

    public IntroChatAdapter(IntroChat_fragment context, List<IntroChatPojo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_box,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        holder.text_message.setText(list.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolderClass extends RecyclerView.ViewHolder {

        TextView text_message;
        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            text_message = itemView.findViewById(R.id.text_message);
        }
    }
}
