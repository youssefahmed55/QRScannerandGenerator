package com.example.qrscannerandgenerator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.Holder> {
    private ArrayList<String> list;

    OnItemClick onItemClick;
    public interface OnItemClick {
        void onClick2(int postion , View view);
    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    public void setList(ArrayList<String> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textView1.setText(String.valueOf(position + 1));
        holder.textView2.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list == null)
            return 0;
        else
            return list.size();
    }



    public class Holder extends RecyclerView.ViewHolder{
        TextView textView1 , textView2;
        public Holder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.text1);
            textView2 = itemView.findViewById(R.id.text2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // you can trust the adapter position
                        // do whatever you intend to do with this position
                        if (onItemClick != null)
                            onItemClick.onClick2(getAdapterPosition(),itemView);
                    }

                }
            });

        }
    }
}
