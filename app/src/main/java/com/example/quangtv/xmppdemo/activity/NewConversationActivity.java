package com.example.quangtv.xmppdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.quangtv.xmppdemo.adapter.ListContactAdapter;
import com.example.quangtv.xmppdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuangTV on 11/9/15.
 */
public class NewConversationActivity extends ActionBarActivity {

    ImageView btnCreate;
    EditText txtUsername;
    ListView lstView;
    List<String> sampleData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Choose User");
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnCreate = (ImageView) findViewById(R.id.btnCreate);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        lstView = (ListView) findViewById(R.id.lv_sample_list);

        setData();
        setListAdapter();

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startChat(sampleData.get(i));


            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                startChat(username);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void startChat(String username) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("username", username);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || requestCode == 65537) {
            if(resultCode == RESULT_OK){
                String to = data.getStringExtra("to");
                String body = data.getStringExtra("message");

                Intent intent = new Intent();
                intent.putExtra("message",body);
                intent.putExtra("to", to);
                intent.putExtra("isUser", data.getBooleanExtra("isUser", true));
                intent.putExtra("type", data.getStringExtra("type"));
                setResult(RESULT_OK, intent);

            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    public void setData() {
        sampleData.add("Quang Tran@fysh.in");
        sampleData.add("Quang Luu@fysh.in");
        sampleData.add("Tin Nguyen@fysh.in");
        sampleData.add("Brice@fysh.in");
        sampleData.add("ThanhTT/M@fysh.in");
        sampleData.add("Lionel Richie@fysh.in");
        sampleData.add("Justin@fysh.in");
        sampleData.add("Paul Pogba@fysh.in");
    }

    public void setListAdapter() {
        ListContactAdapter adapter = new ListContactAdapter(this, sampleData);
        lstView.setAdapter(adapter);
    }
}
