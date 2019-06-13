package com.example.signupin;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "BoardActivity";
    private FirebaseAuth firebaseAuth;


    private ArrayList<ItemData>itemData;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public String getEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Button board_post = (Button) findViewById(R.id.board_post);
        Button board_profile = (Button) findViewById(R.id.board_profile);



        GlobalData.Myemail= getEmail();
        final DatabaseReference nRef = FirebaseDatabase.getInstance().getReference("/USER"+getEmail().replace('.',',')+"/Nickname");
        nRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                     Nickname n = dataSnapshot.getValue(Nickname.class);
                     GlobalData.MyName = Nickname.nickname;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        itemData=new ArrayList<>();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/게시판/물품목록");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ItemData i =snapshot.getValue(ItemData.class);
                        itemData.add(0,i);
                    }
                    adapter=new RecyclerViewAdapter(itemData);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ItemData idate = itemData.get(position);
                Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);

                intent.putExtra("nickname",idate.getNickname());
                intent.putExtra("title", idate.getTitle());
                intent.putExtra("content", idate.getContent());
                intent.putExtra("price", idate.getPrice());
                intent.putExtra("time", idate.getTime());

                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));



        board_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoardActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });


        board_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });








        /*
        Button board_modify = (Button) findViewById(R.id.board_modify);
        Button board_delete = (Button) findViewById(R.id.board_delete);
        */


        /*
        board_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoardActivity.this, ModifyActivity.class);
                startActivity(intent);
            }
        });

        board_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoardActivity.this, DeleteActivity.class);
                startActivity(intent);
            }
        });
        */
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements  RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private BoardActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final BoardActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
               @Override
               public boolean onSingleTapUp(MotionEvent e) {
                   return true;
               }

               @Override
                public void onLongPress(MotionEvent e) {
                   View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                   if (child != null && clickListener != null) {
                       clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                   }
               }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onClick(View view) {

    }
}
