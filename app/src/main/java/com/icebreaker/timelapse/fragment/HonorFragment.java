package com.icebreaker.timelapse.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
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
import com.icebreaker.timelapse.person.HonorTextProgressBar;
import com.icebreaker.timelapse.person.PersonActivity;
import com.icebreaker.timelapse.person.Record;
import com.icebreaker.timelapse.person.RecordAdapter;
import com.icebreaker.timelapse.person.TextProgressBar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * 展示荣誉信息界面
 * @author Marhong
 * @time 2018/5/25 19:35
 */
public class HonorFragment extends BaseFragment implements View.OnClickListener{
    private String userName;
    private static final int GET_USER_HONOR = 1;
//    勋章墙部分控件
    private TextView nineKills,eightKills,sevenKills,sixKills,fiveKills,fourKills,maxVictory,slainNum;
//    战绩部分控件
    private ImageView imageRange;
    private TextView curYear,textRange,totalFights,leftVictoryPoint;
    private HonorTextProgressBar progressBar;
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_USER_HONOR:

                    showHonor(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.honor_page, container, false);
        initViews(view);
        getCurUserHonor();
        return view;
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        // 勋章墙部分
        nineKills = (TextView)view.findViewById(R.id.nineTimes);
        eightKills = (TextView)view.findViewById(R.id.eightTimes);
        sevenKills = (TextView)view.findViewById(R.id.sevenTimes);
        sixKills = (TextView)view.findViewById(R.id.sixTimes);
        fiveKills = (TextView)view.findViewById(R.id.fiveTimes);
        fourKills = (TextView)view.findViewById(R.id.fourTimes);
        maxVictory = (TextView)view.findViewById(R.id.maxTimes);
        slainNum = (TextView)view.findViewById(R.id.maxPeople);

        // 战绩部分
        imageRange = (ImageView)view.findViewById(R.id.imageRange);
        curYear = (TextView)view.findViewById(R.id.curYear);
        textRange = (TextView)view.findViewById(R.id.textRange);
        totalFights = (TextView)view.findViewById(R.id.totalFights);
        leftVictoryPoint = (TextView)view.findViewById(R.id.leftVictoryPoint);
        progressBar = (HonorTextProgressBar) view.findViewById(R.id.progresss);
    }
    /**
     * 获取当前用户的荣誉
     * @author Marhong
     * @time 2018/5/27 1:20
     */
    private void getCurUserHonor(){
        SharedPreferences user = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userName = user.getString("userName",null);
        final ArrayList<NameValuePair> arrayValues = new ArrayList<NameValuePair>();
        arrayValues.add(new BasicNameValuePair("userName", userName));
        new Thread(new Runnable() { // 开启线程上传文件
            @Override
            public void run() {
                String loginResult = "";
                loginResult = HttpGetData.GetData("http://192.168.1.112:8080/Timelapse/getHonorServlet", arrayValues);
                Message msg = new Message();
                msg.obj = loginResult;
                msg.what = GET_USER_HONOR;
                uiHandler.sendMessage(msg);
            }
        }).start();
    }
    /**
     * 展示荣誉信息
     * @author Marhong
     * @time 2018/5/27 1:21
     */
    private void showHonor(String jsonString){

        try{
            // 解析数据
            JSONObject honor = new JSONObject(jsonString);
            int totalFightsNum = honor.getInt("totalFights");
            int victoryFightsNum = honor.getInt("victoryFights");
            int victoryPointsNum = honor.getInt("victoryPoints");
            int nineKillsNum = honor.getInt("nineKills");
            int eightKillsNum = honor.getInt("eightKills");
            int sevenKillsNum = honor.getInt("sevenKills");
            int sixKillsNum = honor.getInt("sixKills");
            int fiveKillsNum = honor.getInt("fiveKills");
            int fourKillsNum = honor.getInt("fourKills");
            int maxVictoryNum = honor.getInt("maxVictory");
            int slainNumNum = honor.getInt("slainNum");
            // 将数据展示出来
            if(nineKillsNum > 0 ){
                // 如果9连胜次数大于0
                nineKills.setText(String.valueOf(nineKillsNum)+"次");
                nineKills.setBackgroundResource(R.drawable.ninefinished);
            }else{
                nineKills.setText("0次");
                nineKills.setBackgroundResource(R.drawable.nineunfinished);
            }
            if(eightKillsNum > 0 ){
                // 如果8连胜次数大于0
                eightKills.setText(String.valueOf(eightKillsNum)+"次");
                eightKills.setBackgroundResource(R.drawable.eightfinished);
            }else{
                eightKills.setText("0次");
                eightKills.setBackgroundResource(R.drawable.eightunfinished);
            }
            if(sevenKillsNum > 0 ){
                // 如果7连胜次数大于0
                sevenKills.setText(String.valueOf(sevenKillsNum)+"次");
                sevenKills.setBackgroundResource(R.drawable.sevenfinished);
            }else{
                sevenKills.setText("0次");
                sevenKills.setBackgroundResource(R.drawable.sevenunfinished);
            }
            if(sixKillsNum > 0 ){
                // 如果6连胜次数大于0
                sixKills.setText(String.valueOf(sixKillsNum)+"次");
                sixKills.setBackgroundResource(R.drawable.sixfinished);
            }else{
                sixKills.setText("0次");
                sixKills.setBackgroundResource(R.drawable.sixunfinished);
            }
            if(fiveKillsNum > 0 ){
                // 如果5连胜次数大于0
                fiveKills.setText(String.valueOf(fiveKillsNum)+"次");
                fiveKills.setBackgroundResource(R.drawable.fivefinished);
            }else{
                fiveKills.setText("0次");
                fiveKills.setBackgroundResource(R.drawable.fiveunfinished);
            }
            if(fourKillsNum > 0 ){
                // 如果4连胜次数大于0
                fourKills.setText(String.valueOf(fourKillsNum)+"次");
                fourKills.setBackgroundResource(R.drawable.fourfinished);
            }else{
                fourKills.setText("0次");
                fourKills.setBackgroundResource(R.drawable.fourunfinished);
            }
            if(maxVictoryNum > 0 ){
                // 如果最大连胜连胜次数大于0
                maxVictory.setText(String.valueOf(maxVictoryNum)+"场");
                maxVictory.setBackgroundResource(R.drawable.maxvictory);
            }else{
                maxVictory.setText("0次");
                maxVictory.setBackgroundResource(R.drawable.unmaxvictory);
            }
            if(slainNumNum > 0 ){
                // 如果击败不同对手数目大于0
                slainNum.setText(String.valueOf(slainNumNum)+"人");
                slainNum.setBackgroundResource(R.drawable.maxslain);
            }else{
                slainNum.setText("0次");
                slainNum.setBackgroundResource(R.drawable.unmaxslain);
            }
            Calendar calendar = Calendar.getInstance();
            curYear.setText(String.valueOf(calendar.get(Calendar.YEAR))+"年");

                // 展示总对战场次
                totalFights.setText(String.valueOf(totalFightsNum) + "场");
                // 展示段位、段位徽章、胜点
                if(victoryPointsNum>=0 && victoryPointsNum<= 100){
                    // 属于童生
                    imageRange.setImageResource(R.drawable.tongsheng);
                    if(victoryPointsNum>=0 && victoryPointsNum<=50){
                        // 入门童生
                        textRange.setText("入门童生");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum));
                    }else if(victoryPointsNum<=80){
                        // 初级童生
                        textRange.setText("初级童生");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-50));
                    }else if(victoryPointsNum<=100){
                        // 高级童生
                        textRange.setText("高级童生");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-80));
                    }
                }else if(victoryPointsNum<=200){
                    // 属于秀才
                    imageRange.setImageResource(R.drawable.xiucai);
                    if(victoryPointsNum>=100 && victoryPointsNum<=150){
                        // 入门秀才
                        textRange.setText("入门秀才");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-100));
                    }else if(victoryPointsNum<=180){
                        // 初级秀才
                        textRange.setText("初级秀才");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-150));
                    }else if(victoryPointsNum<=200){
                        // 高级秀才
                        textRange.setText("高级秀才");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-180));
                    }
                }else if(victoryPointsNum<=300){
                    // 属于举人
                    imageRange.setImageResource(R.drawable.juren);
                    if(victoryPointsNum>=200 && victoryPointsNum<=250){
                        // 入门举人
                        textRange.setText("入门举人");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-200));
                    }else if(victoryPointsNum<=280){
                        // 初级举人
                        textRange.setText("初级举人");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-250));
                    }else if(victoryPointsNum<=300){
                        // 高级举人
                        textRange.setText("高级举人");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-280));
                    }
                }else if(victoryPointsNum<=400){
                    // 属于贡士
                    imageRange.setImageResource(R.drawable.gongshi);
                    if(victoryPointsNum>=300 && victoryPointsNum<=350){
                        // 入门贡士
                        textRange.setText("入门贡士");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-300));
                    }else if(victoryPointsNum<=380){
                        // 初级贡士
                        textRange.setText("初级贡士");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-350));
                    }else if(victoryPointsNum<=400){
                        // 高级贡士
                        textRange.setText("高级贡士");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-380));
                    }
                }else{
                    // 属于进士
                    imageRange.setImageResource(R.drawable.jinshi);
                    if(victoryPointsNum>=400 && victoryPointsNum<=450){
                        // 入门进士
                        textRange.setText("入门进士");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-400));
                    }else if(victoryPointsNum<=480){
                        // 初级进士
                        textRange.setText("初级进士");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-450));
                    }else{
                        // 高级进士
                        textRange.setText("高级进士");
                        leftVictoryPoint.setText(String.valueOf(victoryPointsNum-480));
                    }
                }
                // 展示胜率和获胜场次
            progressBar.setMax(totalFightsNum);
            progressBar.setProgress(victoryFightsNum);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        PersonActivity.curFragmentTag = getString(R.string.str_honor);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

        }
    }
}
