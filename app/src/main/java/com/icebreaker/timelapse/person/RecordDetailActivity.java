package com.icebreaker.timelapse.person;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.internet.HttpGetData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 展示对战双方的详细数据
 * @author Marhong
 * @time 2018/5/28 16:43
 */
public class RecordDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private String userName;
    private int initiatorResult,receiverResult;
    private static final int GET_USER_GOAL = 1;
    private RelativeLayout mBarView;
    private ImageView mBack;
    // 我方部分控件
    private TextView textMyResult, textMyUnfinishedItems, myTotalTime, mySpareTime, mySocialTime, myStudyTime, myNewsTime, myOtherTime;
    private TextProgressBar myTotalTimePro, mySpareTimePro, mySocialTimePro, myStudyTimePro, myNewsTimePro, myOtherTimePro;
    // 敌方部分控件
    private TextView textRivalResult, textRivalUnfinishedItems, rivalTotalTime, rivalSpareTime, rivalSocialTime, rivalStudyTime, rivalNewsTime, rivalOtherTime;
    private TextProgressBar rivalTotalTimePro, rivalSpareTimePro, rivalSocialTimePro, rivalStudyTimePro, rivalNewsTimePro, rivalOtherTimePro;
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_USER_GOAL:
                    showData(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        Bundle bundle = getIntent().getBundleExtra("recordData");
        initiatorResult = bundle.getInt("initiatorResult");
        receiverResult = bundle.getInt("receiverResult");
        initViews();
        getRecordData(bundle.getString("initiator"),bundle.getString("receiver"),bundle.getString("timestring"));
    }
    /**
     * 初始化所有控件
     * @author Marhong
     * @time 2018/5/28 18:09
     */
    private void initViews(){
        // 初始化ActionBar
        StatusBarCompat.setStatusBarColor(this, Color.WHITE);
        mBarView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.actionbar_record_detail,null);
        mBack = (ImageView)mBarView.findViewById(R.id.back);
        mBack.setOnClickListener(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);

        SharedPreferences user = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userName = user.getString("userName",null);
        // 我方部分控件
        textMyResult = (TextView)findViewById(R.id.textMyResult);
        textMyUnfinishedItems = (TextView)findViewById(R.id.textMyUnfinishedItems);
        myTotalTime = (TextView)findViewById(R.id.myTotalTime);
        mySpareTime = (TextView)findViewById(R.id.mySpareTime);
        mySocialTime = (TextView)findViewById(R.id.mySocialTime);
        myStudyTime = (TextView)findViewById(R.id.myStudyTime);
        myNewsTime = (TextView)findViewById(R.id.myNewsTime);
        myOtherTime = (TextView)findViewById(R.id.myOtherTime);
        myTotalTimePro = (TextProgressBar)findViewById(R.id.myTotalTimePro);
        mySpareTimePro = (TextProgressBar)findViewById(R.id.mySpareTimePro);
        mySocialTimePro = (TextProgressBar)findViewById(R.id.mySocialTimePro);
        myStudyTimePro = (TextProgressBar)findViewById(R.id.myStudyTimePro);
        myNewsTimePro = (TextProgressBar)findViewById(R.id.myNewsTimePro);
        myOtherTimePro = (TextProgressBar)findViewById(R.id.myOtherTimePro);

        // 敌方控件
        textRivalResult = (TextView)findViewById(R.id.textRivalResult);
        textRivalUnfinishedItems = (TextView)findViewById(R.id.textRivalUnfinishedItems);
        rivalTotalTime = (TextView)findViewById(R.id.rivalTotalTime);
        rivalSpareTime = (TextView)findViewById(R.id.rivalSpareTime);
        rivalSocialTime = (TextView)findViewById(R.id.rivalSocialTime);
        rivalStudyTime = (TextView)findViewById(R.id.rivalStudyTime);
        rivalNewsTime = (TextView)findViewById(R.id.rivalNewsTime);
        rivalOtherTime = (TextView)findViewById(R.id.rivalOtherTime);
        rivalTotalTimePro = (TextProgressBar)findViewById(R.id.rivalTotalTimePro);
        rivalSpareTimePro = (TextProgressBar)findViewById(R.id.rivalSpareTimePro);
        rivalSocialTimePro = (TextProgressBar)findViewById(R.id.rivalSocialTimePro);
        rivalStudyTimePro = (TextProgressBar)findViewById(R.id.rivalStudyTimePro);
        rivalNewsTimePro = (TextProgressBar)findViewById(R.id.rivalNewsTimePro);
        rivalOtherTimePro = (TextProgressBar)findViewById(R.id.rivalOtherTimePro);
    }
    /**
     * 获取对战双方的每日目标
     * @author Marhong
     * @time 2018/5/28 18:09
     */
    private void getRecordData(String initiator,String receiver,String timestring){

        final ArrayList<NameValuePair> initiatorArrayValues = new ArrayList<NameValuePair>();
        initiatorArrayValues.add(new BasicNameValuePair("userName", initiator));
        initiatorArrayValues.add(new BasicNameValuePair("time",timestring));
        final ArrayList<NameValuePair> receiverArrayValues = new ArrayList<NameValuePair>();
        receiverArrayValues.add(new BasicNameValuePair("userName", receiver));
        receiverArrayValues.add(new BasicNameValuePair("time",timestring));
        new Thread(new Runnable() { // 开启线程上传文件
            @Override
            public void run() {
                String initiatorGoalString = "";
                String receiverGoalString = "";
                initiatorGoalString = HttpGetData.GetData("http://192.168.1.112:8080/Timelapse/getSomedayGoalServlet", initiatorArrayValues);
                receiverGoalString = HttpGetData.GetData("http://192.168.1.112:8080/Timelapse/getSomedayGoalServlet", receiverArrayValues);
                Message msg = new Message();
                msg.obj = initiatorGoalString+"_"+receiverGoalString;
                msg.what = GET_USER_GOAL;
                uiHandler.sendMessage(msg);
            }
        }).start();
    }
    /**
     * 将双方数据展示出来
     * @author Marhong
     * @time 2018/5/28 18:10
     */
    private void showData(String goalString){
        String initiatorGoalString = goalString.split("_")[0];
        String receiverGoalString = goalString.split("_")[1];


        try{
            // 解析json数据
            JSONObject initiator = new JSONObject(initiatorGoalString);
            JSONObject receiver = new JSONObject(receiverGoalString);
            int myPlanTotalTime=0,myPlanSpareTime=0,myPlanSocialTime=0,myPlanStudyTime=0,myPlanNewsTime=0,myPlanOtherTime=0,myUnfinishedItems=0;
            int myActualTotalTime=0,myActualSpareTime=0,myActualSocialTime=0,myActualStudyTime=0,myActualNewsTime=0,myActualOtherTime=0;
            int rivalPlanTotalTime=0,rivalPlanSpareTime=0,rivalPlanSocialTime=0,rivalPlanStudyTime=0,rivalPlanNewsTime=0,rivalPlanOtherTime=0,rivalUnfinishedItems=0;
            int rivalActualTotalTime=0,rivalActualSpareTime=0,rivalActualSocialTime=0,rivalActualStudyTime=0,rivalActualNewsTime=0,rivalActualOtherTime=0;
            if(userName.equals(initiator.getString("userName"))){
                // 当前用户为该次对战的发起者
                String id = initiator.getString("id");
                if(initiatorResult == 1){
                    textMyResult.setText("我方胜");
                    textMyResult.setTextColor(Color.rgb(0,255,127));
                }else{
                    textMyResult.setText("我方败");
                    textMyResult.setTextColor(Color.RED);
                }
                if(receiverResult == 1){
                    textRivalResult.setText(receiver.getString("userName")+" 胜");
                    textRivalResult.setTextColor(Color.rgb(0,255,127));
                }else{
                    textRivalResult.setText(receiver.getString("userName")+" 败");
                    textRivalResult.setTextColor(Color.RED);
                }
                myPlanTotalTime = initiator.getInt("planTotalTime");
                myPlanSpareTime = initiator.getInt("planSpareTime");
                myPlanSocialTime = initiator.getInt("planSocialTime");
                myPlanStudyTime = initiator.getInt("planStudyTime");
                myPlanNewsTime = initiator.getInt("planNewsTime");
                myPlanOtherTime = initiator.getInt("planOtherTime");
                myActualTotalTime = initiator.getInt("actualTotalTime");
                myActualSpareTime = initiator.getInt("actualSpareTime");
                myActualSocialTime = initiator.getInt("actualSocialTime");
                myActualStudyTime = initiator.getInt("actualStudyTime");
                myActualNewsTime = initiator.getInt("actualNewsTime");
                myActualOtherTime = initiator.getInt("actualOtherTime");
                myUnfinishedItems = initiator.getInt("unfinishedItems");

                rivalPlanTotalTime = receiver.getInt("planTotalTime");
                rivalPlanSpareTime = receiver.getInt("planSpareTime");
                rivalPlanSocialTime = receiver.getInt("planSocialTime");
                rivalPlanStudyTime = receiver.getInt("planStudyTime");
                rivalPlanNewsTime = receiver.getInt("planNewsTime");
                rivalPlanOtherTime = receiver.getInt("planOtherTime");
                rivalActualTotalTime = receiver.getInt("actualTotalTime");
                rivalActualSpareTime = receiver.getInt("actualSpareTime");
                rivalActualSocialTime = receiver.getInt("actualSocialTime");
                rivalActualStudyTime = receiver.getInt("actualStudyTime");
                rivalActualNewsTime = receiver.getInt("actualNewsTime");
                rivalActualOtherTime = receiver.getInt("actualOtherTime");
                rivalUnfinishedItems = receiver.getInt("unfinishedItems");


            }else if(userName.equals(receiver.getString("userName"))){
                if(receiverResult == 1){
                    textMyResult.setText("我方胜");
                    textMyResult.setTextColor(Color.rgb(0,255,127));
                }else{
                    textMyResult.setText("我方败");
                    textMyResult.setTextColor(Color.RED);
                }
                if(initiatorResult == 1){
                    textRivalResult.setText(initiator.getString("userName")+" 胜");
                    textRivalResult.setTextColor(Color.rgb(0,255,127));
                }else{
                    textRivalResult.setText(initiator.getString("userName")+" 败");
                    textRivalResult.setTextColor(Color.RED);
                }
                myPlanTotalTime = receiver.getInt("planTotalTime");
                myPlanSpareTime = receiver.getInt("planSpareTime");
                myPlanSocialTime = receiver.getInt("planSocialTime");
                myPlanStudyTime = receiver.getInt("planStudyTime");
                myPlanNewsTime = receiver.getInt("planNewsTime");
                myPlanOtherTime = receiver.getInt("planOtherTime");
                myActualTotalTime = receiver.getInt("actualTotalTime");
                myActualSpareTime = receiver.getInt("actualSpareTime");
                myActualSocialTime = receiver.getInt("actualSocialTime");
                myActualStudyTime = receiver.getInt("actualStudyTime");
                myActualNewsTime = receiver.getInt("actualNewsTime");
                myActualOtherTime = receiver.getInt("actualOtherTime");
                myUnfinishedItems = receiver.getInt("unfinishedItems");

                rivalPlanTotalTime = initiator.getInt("planTotalTime");
                rivalPlanSpareTime = initiator.getInt("planSpareTime");
                rivalPlanSocialTime = initiator.getInt("planSocialTime");
                rivalPlanStudyTime = initiator.getInt("planStudyTime");
                rivalPlanNewsTime = initiator.getInt("planNewsTime");
                rivalPlanOtherTime = initiator.getInt("planOtherTime");
                rivalActualTotalTime = initiator.getInt("actualTotalTime");
                rivalActualSpareTime = initiator.getInt("actualSpareTime");
                rivalActualSocialTime = initiator.getInt("actualSocialTime");
                rivalActualStudyTime = initiator.getInt("actualStudyTime");
                rivalActualNewsTime = initiator.getInt("actualNewsTime");
                rivalActualOtherTime = initiator.getInt("actualOtherTime");
                rivalUnfinishedItems = initiator.getInt("unfinishedItems");
            }
            // 展示自己的数据

            myTotalTime.setText(convertSecondToHour(myPlanTotalTime));
            mySocialTime.setText(convertSecondToHour(myPlanSocialTime));
            mySpareTime.setText(convertSecondToHour(myPlanSpareTime));
            myStudyTime.setText(convertSecondToHour(myPlanStudyTime));
            myNewsTime.setText(convertSecondToHour(myPlanNewsTime));
            myOtherTime.setText(convertSecondToHour(myPlanOtherTime));

            if(myUnfinishedItems>0){
                textMyUnfinishedItems.setText(myUnfinishedItems+"项");
                textMyUnfinishedItems.setTextColor(Color.RED);
            }else{
                textMyUnfinishedItems.setText("全部达标");
                textMyUnfinishedItems.setTextColor(Color.rgb(0,255,127));
            }
            Resources resources = getResources();
            Drawable warnDrawable = resources.getDrawable(R.drawable.warnprogressbar);
            if(myActualTotalTime > myPlanTotalTime){
                myTotalTimePro.setProgressDrawable(warnDrawable);
            }
            if(myActualSpareTime > myPlanSpareTime){
                mySpareTimePro.setProgressDrawable(warnDrawable);
            }
            if(myActualSocialTime > myPlanSocialTime){
                mySocialTimePro.setProgressDrawable(warnDrawable);
            }
            if(myActualStudyTime < myPlanStudyTime){
                myStudyTimePro.setProgressDrawable(warnDrawable);


            }
            if(myActualNewsTime > myPlanNewsTime){
                myNewsTimePro.setProgressDrawable(warnDrawable);
            }
            if (myActualOtherTime > myPlanOtherTime){
                myOtherTimePro.setProgressDrawable(warnDrawable);
            }
            myTotalTimePro.setMax(myPlanTotalTime);
            mySpareTimePro.setMax(myPlanSpareTime);
            mySocialTimePro.setMax(myPlanSocialTime);
            myStudyTimePro.setMax(myPlanStudyTime);
            myNewsTimePro.setMax(myPlanNewsTime);
            myOtherTimePro.setMax(myPlanOtherTime);

            myTotalTimePro.setProgress(myActualTotalTime);
            mySpareTimePro.setProgress(myActualSpareTime);
            mySocialTimePro.setProgress(myActualSocialTime);
            myStudyTimePro.setProgress(myActualStudyTime);
            myNewsTimePro.setProgress(myActualNewsTime);
            myOtherTimePro.setProgress(myActualOtherTime);

            // 展示敌方的数据
            rivalTotalTime.setText(convertSecondToHour(rivalPlanTotalTime));
            rivalSocialTime.setText(convertSecondToHour(rivalPlanSocialTime));
            rivalSpareTime.setText(convertSecondToHour(rivalPlanSpareTime));
            rivalStudyTime.setText(convertSecondToHour(rivalPlanStudyTime));
            rivalNewsTime.setText(convertSecondToHour(rivalPlanNewsTime));
            rivalOtherTime.setText(convertSecondToHour(rivalPlanOtherTime));
            if(rivalUnfinishedItems>0){
                textRivalUnfinishedItems.setText(rivalUnfinishedItems+"项");
                textRivalUnfinishedItems.setTextColor(Color.RED);
            }else{
                textRivalUnfinishedItems.setText("全部达标");
                textRivalUnfinishedItems.setTextColor(Color.rgb(0,255,127));
            }


           // Resources resources = getResources();
           // Drawable warnDrawable = resources.getDrawable(R.drawable.warnprogressbar);
            if(rivalActualTotalTime > rivalPlanTotalTime){
                rivalTotalTimePro.setProgressDrawable(warnDrawable);
            }
            if(rivalActualSpareTime > rivalPlanSpareTime){
                rivalSpareTimePro.setProgressDrawable(warnDrawable);
            }
            if(rivalActualSocialTime > rivalPlanSocialTime){
                rivalSocialTimePro.setProgressDrawable(warnDrawable);
            }
            if(rivalActualStudyTime < rivalPlanStudyTime){
                rivalStudyTimePro.setProgressDrawable(warnDrawable);


            }
            if(rivalActualNewsTime > rivalPlanNewsTime){
                rivalNewsTimePro.setProgressDrawable(warnDrawable);
            }
            if (rivalActualOtherTime > rivalPlanOtherTime){
                rivalOtherTimePro.setProgressDrawable(warnDrawable);
            }
            rivalTotalTimePro.setMax(rivalPlanTotalTime);
            rivalSpareTimePro.setMax(rivalPlanSpareTime);
            rivalSocialTimePro.setMax(rivalPlanSocialTime);
            rivalStudyTimePro.setMax(rivalPlanStudyTime);
            rivalNewsTimePro.setMax(rivalPlanNewsTime);
            rivalOtherTimePro.setMax(rivalPlanOtherTime);

            rivalTotalTimePro.setProgress(rivalActualTotalTime);
            rivalSpareTimePro.setProgress(rivalActualSpareTime);
            rivalSocialTimePro.setProgress(rivalActualSocialTime);
            rivalStudyTimePro.setProgress(rivalActualStudyTime);
            rivalNewsTimePro.setProgress(rivalActualNewsTime);
            rivalOtherTimePro.setProgress(rivalActualOtherTime);
        }catch (Exception e){
            e.printStackTrace();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(this,PersonActivity.class));
                finish();
                break;
        }
    }
}
