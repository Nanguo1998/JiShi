package com.icebreaker.timelapse.apppart;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.icebreaker.timelapse.AnalyseActivity;
import com.icebreaker.timelapse.MainActivity;
import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.util.LineChartHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

public class OtherStatisticActivity extends AppCompatActivity {

       private TextView previous_page_hint,trendData;
    private ImageView previous_page,trendPic;
    private AppInfoHelper appInfoHelper;
    float x_temp01 = 0.0f;
    float y_temp01 = 0.0f;
    float x_temp02 = 0.0f;
    float y_temp02 = 0.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

// No Titlebar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_statistic);
        previous_page = (ImageView)findViewById(R.id.previous_page);
        previous_page_hint = (TextView)findViewById(R.id.previous_page_hint);
        trendData = (TextView)findViewById(R.id.trendData);
        trendPic = (ImageView)findViewById(R.id.trendPic);
        appInfoHelper = new AppInfoHelper(this);
        initChart();


    }

    private void initChart(){
        long[] totalTime = new long[7];
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        for(int i = 0;i < 7;i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE,-i);
            List<AppInfo> appInfos = appInfoHelper.getInformation(cal,this);
            long count = appInfoHelper.getTotalOtherTime(appInfos);
            totalTime[6-i] = count/60;
            Log.e("数据:",String.valueOf(count));
        }
        if(totalTime[6]>totalTime[5]){
            trendPic.setImageResource(R.drawable.up);
            double change =0;
            if(totalTime[5] != 0){
                change = (totalTime[6] - totalTime[5])/totalTime[5];
            }else{
                change = 1;
            }

            trendData.setText(nt.format(change));
        }else{
            trendPic.setImageResource(R.drawable.down);
            double change =0;
            if(totalTime[5] != 0){
                change = (totalTime[6] - totalTime[5])/totalTime[5];
            }else{
                change = 1;
            }

            trendData.setText(nt.format(change));
        }
        LineChartHelper chart = new LineChartHelper((LineChart) findViewById(R.id.chart),totalTime, Color.rgb(255,215,0));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
        // super.onBackPressed();
    }
    /**
     * 通过重写该方法，对触摸事件进行处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                x_temp01 = x;
                y_temp01 = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                x_temp02 = x;
                y_temp02 = y;

                if (x_temp01 != 0 && y_temp01 != 0)//
                {
                    // 比较x_temp01和x_temp02
                    if (x_temp01 > x_temp02)//向左
                    {
                        startActivity(new Intent(OtherStatisticActivity.this, MainActivity.class));
                        finish();
                    }
                    //移动了x_temp01-x_temp02
                }

                if (x_temp01 < x_temp02)//向右
                {
                    startActivity(new Intent(OtherStatisticActivity.this, PlayStatisticActivity.class));
                    finish();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }


        }
        return super.onTouchEvent(event);
    }

}
