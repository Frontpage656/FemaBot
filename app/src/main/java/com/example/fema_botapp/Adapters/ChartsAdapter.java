package com.example.fema_botapp.Adapters;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fema_botapp.PojoClass.ChartModeClass;
import com.example.fema_botapp.R;

import java.util.ArrayList;


public class ChartsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ChartModeClass> chartModeClasses;
    Context context;

    public ChartsAdapter(ArrayList<ChartModeClass> chartModeClasses, Context context) {
        this.chartModeClasses = chartModeClasses;
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_holder,parent,false);
                return new userHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_holder,parent,false);
                return new botHolder(view);
            default:
                return null;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //shit is here the whole chatting functionality ...
        ChartModeClass modeClass = chartModeClasses.get(position);
        switch (chartModeClasses.get(position).getSender()){
            case "user":
                ((userHolder)holder).user_text.setText(modeClass.getMessage());
                ((userHolder)holder).time.setText(modeClass.getTime());
                break;
            case "bot":
                ((botHolder)holder).bot_text.setText(modeClass.getMessage());
               ((botHolder)holder).time.setText(modeClass.getTime());
                break;
        }


    }

    @Override
    public int getItemViewType(int position) {
        switch (chartModeClasses.get(position).getSender()){
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return chartModeClasses.size();
    }

    // Two holders are created here * Do not extends at the top

    // (1) user holder
    public static class userHolder extends RecyclerView.ViewHolder {
        TextView user_text,time;
        @RequiresApi(api = Build.VERSION_CODES.N)
        public userHolder(@NonNull View itemView) {
            super(itemView);
            user_text  = itemView.findViewById(R.id.user_text);
            time = itemView.findViewById(R.id.time);
        }

    }

    // (2) bot holder
    public static class botHolder extends RecyclerView.ViewHolder {
        TextView bot_text,time;
        public botHolder(@NonNull View itemView) {
            super(itemView);

            bot_text = itemView.findViewById(R.id.bot_text);
            time = itemView.findViewById(R.id.time);

        }
    }
}
