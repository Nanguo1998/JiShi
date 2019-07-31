package com.icebreaker.timelapse.apppart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.icebreaker.timelapse.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小侯同学 on 2018/3/22.
 */

public class AppAdapter extends BaseAdapter{

    private List<AppInfo> appInfos = new ArrayList<AppInfo>();
    private Context context;

    public void initAppAdapter(List<AppInfo> appInfos,Context context){
        this.appInfos = appInfos;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(appInfos!=null&&appInfos.size()>0){
            return appInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(appInfos!=null&&appInfos.size()>0){
            return appInfos.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        if(appInfos!=null&&appInfos.size()>0){
            return i;
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AppInfo appInfo = appInfos.get(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_app_info,null);
        ImageView img_icon = (ImageView)view.findViewById(R.id.app_icon);
        TextView txt_appName = (TextView)view.findViewById(R.id.app_name);
        TextView txt_appTime = (TextView)view.findViewById(R.id.app_foreground_time);
        TextView txt_appCount = (TextView)view.findViewById(R.id.app_launch_count);
        TextView txt_appType = (TextView)view.findViewById(R.id.app_type);
        ImageView img_top = (ImageView)view.findViewById(R.id.app_top);
        img_icon.setImageDrawable(appInfo.getIcon());
        txt_appName.setText(appInfo.getAppName()+"\t");
        txt_appTime.setText("\t"+formatSecond(appInfo.getForegroundTime()));
        txt_appCount.setText("\t开启次数："+appInfo.getLaunchCount());

        String appType = appInfo.getType();
        txt_appType.setText(appType);
        if (appType.equals("学习")){
            txt_appType.setBackgroundResource(R.color.study_color);
        }else if(appType.equals("工作")){
            txt_appType.setBackgroundResource(R.color.work_color);
        }else if(appType.equals("娱乐")){
            txt_appType.setBackgroundResource(R.color.amuse_color);
        }else{
            txt_appType.setBackgroundResource(R.color.other_color);
        }
        if(i == 0){
            img_top.setImageResource(R.drawable.top1);
        }
        if(i == 1){
            img_top.setImageResource(R.drawable.top2);
        }
        if(i == 2){
            img_top.setImageResource(R.drawable.top3);
        }
        return view;
    }

    private String formatSecond(long second){
        String result;
        long hour = second/(60*60);
        second = second - hour*60*60;
        long minute = second/60;
        if(minute==0&&hour==0){
            result = "小于1分钟";
        }else if(hour==0){
            result = minute + "分钟";
        }else{
            if(minute==0){
                result = hour + "小时";
            }else{
                result = hour + "小时" + minute + "分钟";
            }
        }
        return result;
    }
}
