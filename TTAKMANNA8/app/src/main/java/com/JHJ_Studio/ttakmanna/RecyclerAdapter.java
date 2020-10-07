package com.JHJ_Studio.ttakmanna;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private static String[] roomName, selectTxt;
    RecyclerHolder holder;
    static Context context;
    static ArrayList<Room> rooms = new ArrayList<>();

    public RecyclerAdapter(String[] roomName, String[] selectTxt, ArrayList<Room> rooms){
        this.roomName = roomName;
        this.selectTxt = selectTxt;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        holder = new RecyclerHolder(holderView);
        context = parent.getContext();
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

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        //닫히지 않은 방 > 대기화면
                        if(rooms.get(pos).getClosed() == 0){
                            Intent intent = new Intent(context,ParticipationCheckActivity.class);
                            intent.putExtra("RoomName",roomName[pos]);
                            intent.putExtra("Room",rooms);
                            intent.putExtra("Pos",pos);
                            ((Activity)context).startActivity(intent);
                            ((Activity)context).overridePendingTransition(R.anim.enter,R.anim.exit);

                        //닫힌 방
                        }else if(rooms.get(pos).getClosed() == 1){
                            //선택 미완료 > 선택화면
                            if(rooms.get(pos).getSelected() == 0){
                                Intent intent = new Intent(context,FixScheduleActivity.class);
                                intent.putExtra("RoomName",roomName[pos]);
                                intent.putExtra("Room",rooms);
                                ((Activity)context).startActivity(intent);
                                ((Activity)context).overridePendingTransition(R.anim.enter,R.anim.exit);

                            //선택 완료 > 출력화면
                            }else if(rooms.get(pos).getSelected() == 1){
                                Intent intent = new Intent(context,PrintScheduleActivity.class);
                                intent.putExtra("RoomName",roomName[pos]);
                                intent.putExtra("Room",rooms);
                                ((Activity)context).startActivity(intent);
                                ((Activity)context).overridePendingTransition(R.anim.enter,R.anim.exit);
                            }
                        }

                    }
                }
            });
        }
    }
}
