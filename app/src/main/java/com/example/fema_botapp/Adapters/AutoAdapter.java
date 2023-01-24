package com.example.fema_botapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fema_botapp.PojoClass.RecommendedModeClass;
import com.example.fema_botapp.PojoClass.Texts;
import com.example.fema_botapp.R;

import java.util.List;

public class AutoAdapter extends RecyclerView.Adapter<AutoAdapter.ViewHolder> {
   Context context;
   List<RecommendedModeClass> textsList;

   //custom callbacks
   AutoAdapterCallback callback;

    public AutoAdapter(Context context, List<RecommendedModeClass> textsList) {
        this.context = context;
        this.textsList = textsList;
    }

    //custom callbacks constructor
    public void setCallback(AutoAdapterCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public AutoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_list,parent,false);

        //call view holder from here with callbacks..
        ViewHolder viewHolder = new ViewHolder(view, new ViewHolder.Callback() {
            @Override
            public void onItemClicked(int position) {
                // forward callback to adapter callback
                if (callback != null){
                    // get actual item from its position
                    RecommendedModeClass texts = getItemByPosition(position);
                    // send to adapter callback
                    callback.onItemClick(texts);

                }
            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.input_text.setText(textsList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return textsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView input_text;
        Callback callback;

        public ViewHolder(@NonNull View itemView, Callback callback) {
            super(itemView);
            this.callback = callback;

            input_text = itemView.findViewById(R.id.input_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   callback.onItemClicked(getAdapterPosition());
                }
            });

        }
        public interface Callback{
            void onItemClicked(int position);
        }
    }







    public interface AutoAdapterCallback {
        void onItemClick(RecommendedModeClass texts);
    }

    private RecommendedModeClass getItemByPosition(int position) {
        return this.textsList.get(position);
    }
}
