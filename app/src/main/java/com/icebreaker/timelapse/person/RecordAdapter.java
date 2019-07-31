package com.icebreaker.timelapse.person;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.icebreaker.timelapse.R;

import org.w3c.dom.Text;

/**
 * 展示所有对战记录的适配器
 * @author Marhong
 * @time 2018/5/25 19:32
 */
public class RecordAdapter extends BaseAdapter {
    private ArrayList<Record> records;
    private Context context;
    private String userName;
    public RecordAdapter(ArrayList<Record> records, Context context,String userName) {
        this.records = records;
        this.context = context;
        this.userName = userName;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout record_item = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.record_item,null);
        ImageView imageResult = (ImageView)record_item.findViewById(R.id.imageResult);
        TextView textResult = (TextView)record_item.findViewById(R.id.textResult);
        TextView textTime = (TextView)record_item.findViewById(R.id.textTime);
        TextView unfinishedNum = (TextView)record_item.findViewById(R.id.unfinishedNum);
        Record record = records.get(position);

        if(record.getInitiator().equals(userName)){
            if(record.getIniUnfinishedNum() == 0){
                unfinishedNum.setText("全部达标");
                unfinishedNum.setTextColor(Color.rgb(0,255,127));
            }else{
                unfinishedNum.setText(record.getIniUnfinishedNum()+"项未达标");
                unfinishedNum.setTextColor(Color.RED);
            }
            // 用户为该次挑战的发起者
            if(record.getInitiatorResult() == 1){
                // 该次挑战胜利
                imageResult.setImageResource(R.drawable.victory);
                textResult.setText("胜利");
                textResult.setTextColor(Color.rgb(0,255,127));
            }else{
                // 该次挑战失败
                imageResult.setImageResource(R.drawable.defeat);
                textResult.setText("失败");
                textResult.setTextColor(Color.RED);
            }
        }else{
            // 用户为该次挑战的接受者
            if(record.getRecUnfinishedNum() == 0){
                unfinishedNum.setText("全部达标");
                unfinishedNum.setTextColor(Color.rgb(0,255,127));
            }else{
                unfinishedNum.setText(record.getRecUnfinishedNum()+"项未达标");
                unfinishedNum.setTextColor(Color.RED);
            }
            if(record.getReceiverResult() == 1){
                // 该次挑战胜利
                imageResult.setImageResource(R.drawable.victory);
                textResult.setText("胜利");
                textResult.setTextColor(Color.rgb(0,255,127));
            }else{
                // 该次挑战失败
                imageResult.setImageResource(R.drawable.defeat);
                textResult.setText("失败");
                textResult.setTextColor(Color.RED);
            }

        }
        textTime.setText(record.getTime());
        return record_item;
    }


}
