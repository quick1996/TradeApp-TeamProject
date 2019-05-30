package com.example.myapplication;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    // adapter에 들어갈 list 입니다.

    private ArrayList<Data> Data = new ArrayList<Data>();
    //private ArrayList<MeData> MeData = new ArrayList<MeData>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.

            if(viewType == 0) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem, parent, false);
                return new ViewHolder0(view);   //1번 뷰타입
            }
            else if (viewType ==1 ){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem2, parent, false);
                return new ViewHolder2(view);   //2번 뷰타입
            }
            else{
                throw new RuntimeException("ERROR - oncreateViewholder");
            }

    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        //return position % 2 * 2;

        return Data.get(position).getType();
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        if(holder.getItemViewType()== 0){
            ((ViewHolder0) holder).onBind(Data.get(position));

        }
        else if(holder.getItemViewType() == 1){
            ((ViewHolder2) holder).onBind(Data.get(position));
        }
        else{
            Log.e("error :", "Error");
            System.out.println("position : " + position);
        }

    }


    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.

        return Data.size();
    }




    void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        Data.add(data);

    }








    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ViewHolder0 extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private TextView TimeView;
        private ImageView imageView;


        ViewHolder0(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.RoomID);
            textView2 = itemView.findViewById(R.id.TextView);
            TimeView = itemView.findViewById(R.id.message_time);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void onBind(Data data) {
            textView1.setText(data.getTitle());
            textView2.setText(data.getContent());
            TimeView.setText(data.getTime());
            imageView.setImageResource(data.getResId());
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            if(Build.VERSION.SDK_INT >= 21) {
                imageView.setClipToOutline(true);
            }
        }

    }

    class ViewHolder2 extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView TimeView;


        ViewHolder2(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.TextView2);
            TimeView = itemView.findViewById(R.id.message_time);

        }

        void onBind(Data data) {
            textView1.setText(data.getContent());
            TimeView.setText(data.getTime());

        }
    }


}
