package com.icebreaker.timelapse.person;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by Marhong on 2018/5/26.
 */

public class TextProgressBar extends ProgressBar {
    String text;
    Paint mPaint;

    public TextProgressBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        System.out.println("1");
        initText();
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        System.out.println("2");
        initText();
    }


    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        System.out.println("3");
        initText();
    }

    @Override
    public  synchronized void setProgress(int progress) {
        // TODO Auto-generated method stub
        setText(progress);
        super.setProgress(progress);
    }

    public void setText(int  progress) {

        this.text = convertSecondToHour(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
//        int x = 75;
//        int y = (getHeight() / 2) - rect.centerY();
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.mPaint);

    }

    //初始化，画笔
    private void initText(){
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(50);
    }


    @Override
    public synchronized void setMax(int max) {
        super.setMax(max);
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
            totalTime = "暂无";
        }else{
            int hour = time/3600;
            int minute = (time - hour*3600)/60;
            int second = time - hour*3600 - minute*60;

            if(hour > 0)
            {
                totalTime += hour+"时";
            }
            if(minute >0)
            {
                totalTime += minute+"分";
            }
            if(second >0)
            {
                totalTime += second+"秒";
            }
        }

        return totalTime;
    }
}
