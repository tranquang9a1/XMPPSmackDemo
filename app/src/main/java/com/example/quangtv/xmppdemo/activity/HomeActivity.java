package com.example.quangtv.xmppdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quangtv.xmppdemo.R;
import com.example.quangtv.xmppdemo.adapter.ListHistoryAdapter;
import com.example.quangtv.xmppdemo.entity.Connection;
import com.example.quangtv.xmppdemo.entity.HistoryMessage;
import com.example.quangtv.xmppdemo.fragment.FragmentText;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jxmpp.util.XmppStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by QuangTV on 11/13/15.
 */
public class HomeActivity extends ActionBarActivity {

    ListView lstMessage;
    List<HistoryMessage> sampleData = new ArrayList<>();
    private ImageButton btnStart;
    private TextView txtUser;
    private String username;
    private ImageView btnCreate;
    private XMPPConnection connection;
    private Handler mHandler = new Handler();
    private LinearLayout emptyList;
    private LinearLayout layoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnStart = (ImageButton) findViewById(R.id.btnStart);

        lstMessage = (ListView) findViewById(R.id.lv_sample_list);
        txtUser = (TextView) findViewById(R.id.txtUser);
        emptyList = (LinearLayout) findViewById(R.id.firstTime);
        btnCreate = (ImageView) findViewById(R.id.btnCreate);
        layoutButton = (LinearLayout) findViewById(R.id.layoutButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        connection = Connection.getConnection();
        setListAdapter();
        setConnection(connection);
        username = getIntent().getExtras().getString("username");
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NewConversationActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        txtUser.setText(username);

        lstMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                intent.putExtra("username", sampleData.get(i).getName());
                intent.putExtra("message", sampleData.get(i).getMessage());
                intent.putExtra("isUser", sampleData.get(i).isUser());
                intent.putExtra("type", sampleData.get(i).getType());
                startActivityForResult(intent, 1);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NewConversationActivity.class);
                startActivityForResult(intent, 1);
            }
        });



    }

    public void setData() {
        Long time = System.currentTimeMillis();
        sampleData.add(new HistoryMessage("Quang Tran@fysh.in", "Welcome to Officience!", true, "text"));
        sampleData.add(new HistoryMessage("Quang Luu@fysh.in", "Welcome to Officience Viet Nam!", true, "text"));
        sampleData.add(new HistoryMessage("Tin Nguyen@fysh.in", "Welcome to Officience!", true, "text"));
        sampleData.add(new HistoryMessage("Brice Officience@fysh.in", "Welcome to Officience!", true, "text"));

    }




    public void setConnection
            (XMPPConnection
                     connection) {

        this.connection = connection;
        if (connection != null) {
            // Add a packet listener to get messages sent to us


            StanzaFilter filter1 = MessageTypeFilter.CHAT;


            //PacketFilter filter = (PacketFilter) MessageTypeFilter.CHAT;
            connection.addAsyncStanzaListener(new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    Message message = (Message) packet;
                    if (message.getBody() != null) {
                        String fromName = XmppStringUtils.parseBareJid(message.getFrom());
                        Log.i("XMPPClient", "Got text [" + message.getBody() + "] from [" + fromName + "]");
                        addData(fromName, message.getBody(), false, "text");
                        mHandler.post(new Runnable() {
                            public void run() {
                                setListAdapter();
                            }
                        });
                    }
                }
            }, filter1);
            //receiveFile();


        }
    }

    public void setListAdapter() {
        ListHistoryAdapter adapter = new ListHistoryAdapter(this, sampleData);
        if (sampleData.size() > 0) {
            emptyList.setVisibility(View.GONE);
            lstMessage.setVisibility(View.VISIBLE);
            layoutButton.setVisibility(View.VISIBLE);
        }
        lstMessage.setAdapter(adapter);
    }

    public void addData(String name, String data, boolean isUser, String type) {
        boolean duplicate = false;
        for (HistoryMessage message : sampleData) {
            if (message.getName().equalsIgnoreCase(name)) {
                duplicate = true;
                message.setMessage(data);
                message.setIsUser(isUser);
                message.setType(type);
                message.setTime(System.currentTimeMillis());
                break;
            }
        }

        if (!duplicate) {
            sampleData.add(new HistoryMessage(name, data, isUser, type));
        }
        sortList();

    }



    public void sortList() {
        Collections.sort(sampleData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || requestCode == 65537) {
            if(resultCode == RESULT_OK){
                String to = data.getStringExtra("to");
                String body = data.getStringExtra("message");
                boolean isUser = data.getBooleanExtra("isUser", true);
                String type = data.getStringExtra("type");


                addData(to, body, isUser, type);
                setListAdapter();
            }
        }
    }

}
