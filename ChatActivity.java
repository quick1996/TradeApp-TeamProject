package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {
    /*
    private ArrayAdapter<String> adapter;
    private ListView listView;
    */


    private EditText editText;
    private Button sendButton;
    private String MyName;
    private String OtherName;
    private String inTime;

    private ChatRecyclerAdapter adapter;

    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //listView = (ListView) findViewById(R.id.list_view);
        editText = (EditText) findViewById(R.id.message_edit);
        sendButton = (Button) findViewById(R.id.send_button);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Intent intent = getIntent();
        MyName = itemmodel.MyName;
        OtherName =intent.getStringExtra("user2");
        // 기본 Text를 담을 수 있는 simple_list_item_1을 사용해서 ArrayAdapter를 만들고 listview에 설정


        init();



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 유저 이름과 메세지로 chatData 만들기
                inTime   = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());

                ChatData chatData = new ChatData(MyName, editText.getText().toString(),inTime);
                ChatData chatData2 = new ChatData(OtherName, editText.getText().toString(),inTime);
                // 기본 database 하위 message라는 child에 chatData를 list로 만들기

                databaseReference.child(MyName).child(OtherName).push().setValue(chatData);
                databaseReference.child(OtherName).child(MyName).push().setValue(chatData);

                //  chatlist duplicate remove
                DuplicateRemove(MyName,OtherName);
                DuplicateRemove(OtherName,MyName);


                //  chatlist push data
                databaseReference.child(OtherName).child("ChatList").push().setValue(chatData);
                databaseReference.child(MyName).child("ChatList").push().setValue(chatData2);


                editText.setText("");


            }
        });


        databaseReference.child(MyName).child(OtherName).addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고

                Data data = new Data();

                data.setContent(chatData.getMessage());
                data.setTime(chatData.getTime());

                if(chatData.getUserName().equals(MyName)){
                    data.setType(1);
                }
                else{
                    data.setTitle(OtherName);
                    data.setResId(R.drawable.blank_profile);
                    data.setType(0);
                }
                adapter.addItem(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ChatRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void DuplicateRemove(String Name1 ,String Name2){

        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child(Name1).child("ChatList");
        Query usersQuery = usersRef.orderByChild("userName").equalTo(Name2); // maximum chatlist = 100

        usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int exist = -1;
                String p= "";
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    if(dataSnapshot.exists()){
                        if(exist == 0){
                            usersRef.child(p).setValue(null);
                        }
                        p = userSnapshot.getKey();
                        exist =0;

                    }
                    //System.out.println(userSnapshot.getKey()+": " +userSnapshot.getChildren() + " : " + userSnapshot.toString());
                    //ChatData data = dataSnapshot.getValue(ChatData.class);
                    //System.out.println(data.getMessage() + " " + data.getUserName());


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

}