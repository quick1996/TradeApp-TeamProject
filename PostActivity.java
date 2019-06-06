package com.example.signupin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button post_apply = (Button) findViewById(R.id.post_apply);
        final EditText add_title = (EditText) findViewById(R.id.add_title);
        final EditText add_content = (EditText) findViewById(R.id.add_content);
        final EditText add_price = (EditText) findViewById(R.id.add_price);

        //온클릭리스너
        post_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edittext에 저장된 텍스트 String에 저장
                String get_title = add_title.getText().toString();
                String get_content = add_content.getText().toString();
                String get_price = add_price.getText().toString();
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String get_time = mFormat.format(System.currentTimeMillis());

                //hashmap 만들기
                HashMap result = new HashMap<>();
                result.put("title", get_title);
                result.put("content", get_content);
                result.put("price", get_price);
                result.put("time", get_time);

                //firebase 정의
                mDatabase = FirebaseDatabase.getInstance().getReference();
                //firebase에 저장
                mDatabase.child("post").push().setValue(result);

                Intent intent = new Intent(PostActivity.this, BoardActivity.class);
                startActivity(intent);

            }
        });
    }
}
