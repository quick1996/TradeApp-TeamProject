package com.example.signupin;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ItemData>itemData;


    public RecyclerViewAdapter(List<ItemData> itemData) {
        this.itemData = itemData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemData ld=itemData.get(position);
        holder.texttitle.setText(ld.getTitle());
        holder.textcontent.setText(ld.getContent());
        holder.textprice.setText(ld.getPrice());
        holder.texttime.setText(ld.getTime());
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView texttitle,textcontent,textprice,texttime;
        public ViewHolder(View itemView) {
            super(itemView);
            texttitle=(TextView)itemView.findViewById(R.id.list_title);
            textcontent=(TextView)itemView.findViewById(R.id.list_content);
            textprice=(TextView)itemView.findViewById(R.id.list_price);
            texttime=(TextView)itemView.findViewById(R.id.list_time);
        }
    }

}