package com.example.quangtv.xmppdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quangtv.xmppdemo.entity.HistoryMessage;
import com.example.quangtv.xmppdemo.R;
import com.example.quangtv.xmppdemo.component.RoundedLetterView;

import java.util.List;
import java.util.Random;

/**
 * Created by QuangTV on 11/5/15.
 */
public class ListHistoryAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<HistoryMessage> items;
    private Context mContext;
    public ListHistoryAdapter(Context context, List<HistoryMessage> items) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public HistoryMessage getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;

        if(convertView==null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.list_history, null);

            viewHolder.mRoundedLetterView = (RoundedLetterView) convertView.findViewById(R.id.rlv_name_view);
            viewHolder.txtAccount = (TextView) convertView.findViewById(R.id.txtAccount);
            viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HistoryMessage info = getItem(position);
        if (info != null) {
            //viewHolder.txtViewTitle.setText("Độ hư hại: " + info.getDamageLevel() + "%");
            viewHolder.mRoundedLetterView.setTitleText(getFirstCharacter(info.getName()));
            viewHolder.txtAccount.setText(info.getName());
            viewHolder.txtMessage.setText(info.getMessage());
            viewHolder.mRoundedLetterView.setBackgroundColor(randomColor());

        }

        return convertView;
    }

    public static class ViewHolder {
        private RoundedLetterView mRoundedLetterView;
        private TextView txtAccount;
        private TextView txtMessage;
    }

    public String getFirstCharacter(String name) {
        String[] result = name.split("(?<=[\\S])[\\S]*\\s*");

        String res = "";
        for (int i = 0; i < result.length; i++) {
            res += result[i];
        }
        return res.toUpperCase();
    }

    public int randomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }
}
