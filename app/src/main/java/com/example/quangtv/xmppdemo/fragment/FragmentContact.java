package com.example.quangtv.xmppdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.quangtv.xmppdemo.R;
import com.example.quangtv.xmppdemo.activity.ChatActivity;
import com.example.quangtv.xmppdemo.adapter.ListContactAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuangTV on 11/9/15.
 */
public class FragmentContact extends Fragment {

    ImageButton btnAdd;
    ImageButton btnSearch;
    List<String> sampleData = new ArrayList<>();
    ListView lstView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        initView(rootView);
        return rootView;
    }

    public void initView(View rootView) {
        btnAdd = (ImageButton) rootView.findViewById(R.id.btnAdd);
        btnSearch = (ImageButton) rootView.findViewById(R.id.btnSearch);
        lstView = (ListView) rootView.findViewById(R.id.lv_sample_list);
        setData();
        setListAdapter();

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startChat(sampleData.get(i));
            }
        });


    }

    public void startChat(String username) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("username", username);
        startActivityForResult(intent, 1);

    }

    public void setData() {
        sampleData.add("Quang Tran@fysh.in");
        sampleData.add("Quang Luu@fysh.in");
        sampleData.add("Tin Nguyen@fysh.in");
        sampleData.add("Brice@fysh.in");
        sampleData.add("ThanhTT/M@fysh.in");
    }

    public void setListAdapter() {
        ListContactAdapter adapter = new ListContactAdapter(getActivity(), sampleData);
        lstView.setAdapter(adapter);
    }
}
