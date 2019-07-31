package com.icebreaker.timelapse.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.internet.HttpGetData;
import com.icebreaker.timelapse.person.PersonActivity;
import com.icebreaker.timelapse.person.Record;
import com.icebreaker.timelapse.person.RecordAdapter;
import com.icebreaker.timelapse.person.TextProgressBar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 展示今日目标信息界面
 * @author Marhong
 * @time 2018/5/25 19:32
 */
public class TodayFragment extends BaseFragment implements View.OnClickListener{
    private String userName;
    private static final int GET_USER_GOAL = 1;
    private TextView totalTime,spareTime,socialTime,studyTime,newsTime,otherTime,unfinished;
    private TextProgressBar totalTimePro,spareTimePro,socialTimePro,studyTimePro,newsTimePro,otherTimePro;
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_USER_GOAL:
                    showTodayGoal(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.today_page, container, false);
        initViews(view);
        getTodayGoal();
        return view;
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        totalTime = (TextView)view.findViewById(R.id.totalTime);
        spareTime = (TextView)view.findViewById(R.id.spareTime);
        socialTime = (TextView)view.findViewById(R.id.socialTime);
        studyTime = (TextView)view.findViewById(R.id.studyTime);
        newsTime = (TextView)view.findViewById(R.id.newsTime);
        otherTime = (TextView)view.findViewById(R.id.otherTime);
        unfinished = (TextView)view.findViewById(R.id.unfinishedItems);
        totalTimePro = (TextProgressBar)view.findViewById(R.id.totalTimePro);
        spareTimePro = (TextProgressBar)view.findViewById(R.id.spareTimePro);
        socialTimePro = (TextProgressBar)view.findViewById(R.id.socialTimePro);
        studyTimePro = (TextProgressBar)view.findViewById(R.id.studyTimePro);
        newsTimePro = (TextProgressBar)view.findViewById(R.id.newsTimePro);
        otherTimePro = (TextProgressBar)view.findViewById(R.id.otherTimePro);

    }
    /**
     * 获取当前用户今天的每日目标及完成情况
     * @author Marhong
     * @time 2018/5/28 9:54
     */
    private void getTodayGoal(){
        SharedPreferences user = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userName = user.getString("userName",null);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String time = String.valueOf(year)+String.valueOf(month)+String.valueOf(day);
        final ArrayList<NameValuePair> arrayValues = new ArrayList<NameValuePair>();
        arrayValues.add(new BasicNameValuePair("userName", userName));
        arrayValues.add(new BasicNameValuePair("time",time));
        new Thread(new Runnable() { // 开启线程上传文件
            @Override
            public void run() {
                String loginResult = "";
                loginResult = HttpGetData.GetData("http://192.168.1.112:8080/Timelapse/getSomedayGoalServlet", arrayValues);
                Message msg = new Message();
                msg.obj = loginResult;
                msg.what = GET_USER_GOAL;
                uiHandler.sendMessage(msg);
            }
        }).start();
    }
    /**
     * 展示今日每日目标
     * @author Marhong
     * @time 2018/5/28 10:04
     */
    private void showTodayGoal(String goalString){
        Log.e("每日目标数据",goalString);

        try{
            // 解析json数据
            JSONObject goal = new JSONObject(goalString);
            String id = goal.getString("id");

            int planTotalTime = goal.getInt("planTotalTime");
            int planSpareTime = goal.getInt("planSpareTime");
            int planSocialTime = goal.getInt("planSocialTime");
            int planStudyTime = goal.getInt("planStudyTime");
            int planNewsTime = goal.getInt("planNewsTime");
            int planOtherTime = goal.getInt("planOtherTime");
            int actualTotalTime = goal.getInt("actualTotalTime");
            int actualSpareTime = goal.getInt("actualSpareTime");
            int actualSocialTime = goal.getInt("actualSocialTime");
            int actualStudyTime = goal.getInt("actualStudyTime");
            int actualNewsTime = goal.getInt("actualNewsTime");
            int actualOtherTime = goal.getInt("actualOtherTime");
            int unfinishedItems = goal.getInt("unfinishedItems");

            // 展示计划使用时间
            totalTime.setText(convertSecondToHour(planTotalTime));
            socialTime.setText(convertSecondToHour(planSocialTime));
            spareTime.setText(convertSecondToHour(planSpareTime));
            studyTime.setText(convertSecondToHour(planStudyTime));
            newsTime.setText(convertSecondToHour(planNewsTime));
            otherTime.setText(convertSecondToHour(planOtherTime));
            unfinished.setText(unfinishedItems+"项");
            // 展示实际使用时间
            Resources resources = getContext().getResources();
            Drawable warnDrawable = resources.getDrawable(R.drawable.warnprogressbar);
            if(actualTotalTime > planTotalTime){
                totalTimePro.setProgressDrawable(warnDrawable);

            }
            if(actualSpareTime > planSpareTime){
                spareTimePro.setProgressDrawable(warnDrawable);
            }
            if(actualSocialTime > planSocialTime){
                socialTimePro.setProgressDrawable(warnDrawable);
            }
            if(actualStudyTime < planStudyTime){
                studyTimePro.setProgressDrawable(warnDrawable);


            }
            if(actualNewsTime > planNewsTime){
                newsTimePro.setProgressDrawable(warnDrawable);
            }
            if (actualOtherTime > planOtherTime){
                otherTimePro.setProgressDrawable(warnDrawable);
            }
            totalTimePro.setMax(planTotalTime);
            spareTimePro.setMax(planSpareTime);
            socialTimePro.setMax(planSocialTime);
            studyTimePro.setMax(planStudyTime);
            newsTimePro.setMax(planNewsTime);
            otherTimePro.setMax(planOtherTime);

            totalTimePro.setProgress(actualTotalTime);
            spareTimePro.setProgress(actualSpareTime);
            socialTimePro.setProgress(actualSocialTime);
            studyTimePro.setProgress(actualStudyTime);
            newsTimePro.setProgress(actualNewsTime);
            otherTimePro.setProgress(actualOtherTime);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        PersonActivity.curFragmentTag = getString(R.string.str_today);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

        }
    }
    /**
     * 将时间从以秒为单位转换为以小时、分钟、秒为单位
     * @author Marhong
     * @time 2018/5/28 10:32
     */
    private String convertSecondToHour(int time)
    {
        String totalTime="";
        if(time == 0){
            totalTime = "0小时";
        }else{
            double realTime = time;
            double hour = realTime/3600;

            if(hour > 0)
            {
                totalTime += hour+"小时";
            }

        }

        return totalTime;
    }
}
