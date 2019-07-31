package com.icebreaker.timelapse.util;


import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.apppart.AppInfo;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Alan on 2018/5/4.
 */

public class LineChartHelper {
    private LineChart lineChart;
    private long[] totalTime;
    public LineChartHelper(LineChart lineChart,long[] totalTime,int color){
        this.lineChart = lineChart;
        this.totalTime = totalTime;
        initLineChart(color);
    }

    private void initLineChart(int color){

        final String[] quarters = initDate();
        //自定义设置X轴的值为 => 日期
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };
        Description description = new Description();
        description.setText("");
        //设置说明
        lineChart.setDescription(description);
        //设置图例关
        lineChart.getLegend().setEnabled(false);
        //设置显示范围
        lineChart.setVisibleXRangeMaximum(2);
        lineChart.setVisibleYRangeMinimum(10f, YAxis.AxisDependency.LEFT);
        //设置透明度
        lineChart.setAlpha(1.0f);
        //设置背景色
        lineChart.setBackgroundColor(Color.WHITE);
        //设置边框
        lineChart.setBorderColor(Color.rgb(0, 0, 0));
        lineChart.setGridBackgroundColor(R.color.colorPrimary);
        //设置触摸(关闭影响下面3个属性)
        lineChart.setTouchEnabled(false);
        //设置是否可以拖拽
        lineChart.setDragEnabled(true);
        //设置是否可以缩放
        lineChart.setScaleEnabled(true);
        //设置是否能扩大扩小
        lineChart.setPinchZoom(true);

        //获取X轴
        XAxis xl = lineChart.getXAxis();
        //启用X轴
        xl.setEnabled(true);
        //设置X轴避免图表或屏幕的边缘的第一个和最后一个轴中的标签条目被裁剪
        xl.setAvoidFirstLastClipping(true);
        //设置X轴底部显示
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置竖网格
        xl.setDrawGridLines(false);
        //设置X轴文字大小
        xl.setTextSize(10f);
        //设置X轴单位间隔
        xl.setGranularity(1f);
        //设置X轴值
        xl.setValueFormatter(formatter);

        //获取Y轴(左)
        YAxis yl = lineChart.getAxisLeft();
        //设置Y轴文字在外部显示
        yl.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //Y轴字体
        yl.setTextSize(10f);
        //设置Y轴最大值
        yl.setAxisMaximum(getMaxValue(totalTime));
        //设置Y轴起始值
        yl.setAxisMinimum(0f);

        //获取Y轴(右)
        YAxis yl2 = lineChart.getAxisRight();
        //禁用右侧Y轴
        yl2.setEnabled(false);



       setData(color);

        //设置XY轴进入动画
        lineChart.animateXY(800, 800);
        //设置最小的缩放
        lineChart.setScaleMinima(1f, 1f);

        //刷新图表
        //lineChart.invalidate();List<Model> newData = new ArrayList<>();
    }
    public void setData(int color){
        ArrayList<Entry> entryList = new ArrayList<>();
        for(int i = 0;i < 7;i++){
            entryList.add(new Entry(i, totalTime[i]));
            Log.v("数据测试：",String.valueOf(totalTime[i]));
        }

        LineDataSet l1 = new LineDataSet(entryList, "蓝色");
        l1.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置包括的范围区域填充颜色
        l1.setDrawFilled(false);

        //设置线的宽度
        l1.setLineWidth(2f);
        //设置曲线的颜色
        l1.setColor(color);
        //设置曲率,0.05f-1f  1为折线
        l1.setCubicIntensity(1f);

        //设置有圆点
        l1.setDrawCircles(true);
        //设置小圆点的大小
        l1.setCircleRadius(5f);
        //设置圆圈颜色

        l1.setCircleColor(color);
        //填充圆圈内颜色
        l1.setCircleColorHole(color);

        //设置不显示数值
        l1.setDrawValues(false);

        List<ILineDataSet> lineDataSetArrayList = new ArrayList<>();
        lineDataSetArrayList.add(l1);

        LineData lineData = new LineData(lineDataSetArrayList);
        lineChart.setData(lineData);
    }
    public String[] initDate(){
        String[] dates = new String[7];
        for(int i = 0;i < 7;i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE,-i);
            dates[6-i] = (cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.DAY_OF_MONTH);
        }
        return dates;
    }
    public float getMaxValue(long[] count){
        float max = 0f;
        for(int i = 0;i<count.length;i++){
            if (count[i]>max){
                max = count[i];
            }
        }
        return max;
    }

}
