package com.icebreaker.timelapse.util;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.icebreaker.timelapse.apppart.AppInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Alan on 2018/5/4.
 */

public class BarChartHelper {
    private BarChart barChart;
    private List<AppInfo> appInfos;
    private Calendar calendar;
    public BarChartHelper(BarChart barChart,List<AppInfo> appInfos,Calendar calendar){
        this.barChart = barChart;
        this.appInfos = appInfos;
        this.calendar = calendar;
        initBarChart();
    }

    public void initBarChart(){
        //名称
        ArrayList<String> names=new ArrayList<String>();
        names.add("1月1日");
        names.add("1月2日");
        names.add("1月3日");
        names.add("1月4日");
        names.add("1月5日");
        names.add("1月6日");
        names.add("1月7日");
        names.add("1月8日");
        names.add("1月9日");
        //大小（高低）
        ArrayList<BarEntry> sizes=new ArrayList<BarEntry>();
        sizes.add(new BarEntry(80,0));
        sizes.add(new BarEntry(70,1));
        sizes.add(new BarEntry(60,2));
        sizes.add(new BarEntry(50,3));
        sizes.add(new BarEntry(40,4));
        sizes.add(new BarEntry(30,5));
        sizes.add(new BarEntry(20,6));
        sizes.add(new BarEntry(10,7));
        sizes.add(new BarEntry(0,8));

        //颜色
        BarDataSet barDataSet=new BarDataSet(sizes,"");
        barDataSet.setColor(Color.parseColor("#FFBB33"));


        barChart.setDescription(null);//数据描述
        barChart.setNoDataText("无数据");

        //barChart.setDragEnabled(false);//拖拽（蛋疼）

        barChart.setScaleEnabled(true);//手动缩放效果
        barChart.setPinchZoom(false);//xy轴同时缩放,和setScaleEnabled一起使用

        barChart.setHighlightPerTapEnabled(true);//按下时高亮显示

        //barChart.setDrawGridBackground(false);
        //barChart.setDrawBorders(false);//画布边框

        //barChart.setVisibleXRange(7);
        //barChart.setMaxVisibleValueCount(6);
        barChart.setDrawBarShadow(false);//设置矩形阴影不显示
        barChart.setBackgroundColor(Color.parseColor("#FFFFFF"));//设置背景颜色

        barChart.setMinOffset(0);//=padding
        barChart.setDrawValueAboveBar(true);

        List<IBarDataSet> barDataSetArrayList = new ArrayList<>();
        barDataSetArrayList.add(barDataSet);
        BarData barData = new BarData(barDataSetArrayList);
        barChart.setData(barData);
        barChart.animateXY(1000, 1000);//设置动画

        Legend legend=barChart.getLegend();//取消图形说明
        legend.setEnabled(false);

        //获取X轴坐标
        XAxis xAxis=barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X坐标位于图标底部
        xAxis.setDrawGridLines(false);
        //xAxis.

        //获取Y轴右坐标
        YAxis yAxisR=barChart.getAxisRight();
        yAxisR.setEnabled(false);
        yAxisR.setDrawGridLines(false);

        //获取Y轴左坐标
        YAxis yAxisL=barChart.getAxisLeft();
        yAxisL.setEnabled(false);
        yAxisL.setDrawGridLines(false);
    }
}
