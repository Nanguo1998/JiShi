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
 * 荣誉界面的ProgressBar
 * @author Marhong
 * @time 2018/5/28 10:26
 */
public class HonorTextProgressBar extends ProgressBar {
    String text;
    Paint mPaint;

    public HonorTextProgressBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        System.out.println("1");
        initText();
    }

    public HonorTextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        System.out.println("2");
        initText();
    }


    public HonorTextProgressBar(Context context, AttributeSet attrs) {
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
        int i = (progress * 100)/this.getMax();
        this.text = "胜"+String.valueOf(i) + "%  "+progress+"场";
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
//        int x = 75;
//        int y = (getHeight() / 2) - rect.centerY();
        int x = (getWidth() / 2) - rect.centerX()*3;
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
}
