package com.JHJ_Studio.ttakmanna;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private String[] roomName, selectTxt;
    RecyclerHolder holder;

    public RecyclerAdapter(String[] roomName, String[] selectTxt){
        this.roomName = roomName;
        this.selectTxt = selectTxt;
    }

    @NonNull
    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        holder = new RecyclerHolder(holderView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerHolder holder, int position) {
        holder.rnTxt.setText(this.roomName[position]);
        holder.selTxt.setText(this.selectTxt[position]);
    }

    @Override
    public int getItemCount() {
        return roomName.length;
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder{

        public TextView rnTxt;
        public TextView selTxt;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            this.rnTxt = itemView.findViewById(R.id.rnTxt);
            this.selTxt = itemView.findViewById(R.id.nowTxt);
        }
    }
}
