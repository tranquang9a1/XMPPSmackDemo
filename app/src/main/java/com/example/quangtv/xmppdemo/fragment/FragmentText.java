package com.example.quangtv.xmppdemo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.quangtv.xmppdemo.R;
import com.example.quangtv.xmppdemo.activity.ChatActivity;
import com.example.quangtv.xmppdemo.activity.NewConversationActivity;
import com.example.quangtv.xmppdemo.adapter.ListHistoryAdapter;
import com.example.quangtv.xmppdemo.entity.Connection;
import com.example.quangtv.xmppdemo.entity.HistoryMessage;

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
 * Created by neokree on 16/12/14.
 */
public class FragmentText extends Fragment{

    ListView lstMessage;
    List<HistoryMessage> sampleData = new ArrayList<>();
    Map<String, String> data = new HashMap<>();
    private ImageButton btnSearch;
    private ImageButton btnStart;
    private String username;
    private String password;
    private XMPPConnection connection;
    private Handler mHandler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        initView(rootView);
        return rootView;
    }

    public void initView(View rootView) {
        btnSearch = (ImageButton) rootView.findViewById(R.id.btnSearch);
        btnStart = (ImageButton) rootView.findViewById(R.id.btnStart);
        lstMessage = (ListView) rootView.findViewById(R.id.lv_sample_list);
        setData();
        connection = Connection.getConnection();
        setListAdapter();
        setConnection(connection);
        username = getActivity().getIntent().getExtras().getString("username");
        password = getActivity().getIntent().getExtras().getString("password");
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewConversationActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        lstMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("username", sampleData.get(i).getName());
                intent.putExtra("message", sampleData.get(i).getMessage());
                startActivityForResult(intent, 1);
            }
        });

    }


    public void setData() {
        Long time = System.currentTimeMillis();
        sampleData.add(new HistoryMessage("Quang Tran@fysh.in", "Welcome to Officience!", time));
        sampleData.add(new HistoryMessage("Quang Luu@fysh.in", "Welcome to Officience Viet Nam!", time));
        sampleData.add(new HistoryMessage("Tin Nguyen@fysh.in", "Welcome to Officience!", time));
        sampleData.add(new HistoryMessage("Brice Officience@fysh.in", "Welcome to Officience!", time));

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
                        addData(fromName, message.getBody());
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
        ListHistoryAdapter adapter = new ListHistoryAdapter(getActivity(), sampleData);
        lstMessage.setAdapter(adapter);
    }

    public void addData(String name, String data) {
        boolean duplicate = false;
        for (HistoryMessage message : sampleData) {
            if (message.getName().equalsIgnoreCase(name)) {
                duplicate = true;
                message.setMessage(data);
                message.setTime(System.currentTimeMillis());
                break;
            }
        }

        if (!duplicate) {
            sampleData.add(new HistoryMessage(name, data, System.currentTimeMillis()));
        }
        sortList();

    }



    public void sortList() {
        Collections.sort(sampleData);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            addData(data.getExtras().getString("to"), data.getExtras().getString("message"));
            mHandler.post(new Runnable() {
                public void run() {
                    setListAdapter();
                }
            });
        }

    }


}
