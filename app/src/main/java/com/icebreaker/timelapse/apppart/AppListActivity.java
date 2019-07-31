package com.icebreaker.timelapse.apppart;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.icebreaker.timelapse.MainActivity;
import com.icebreaker.timelapse.util.MyDBOpenHelper;
import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.calendar.util.CustomDate;
import com.icebreaker.timelapse.calendar.util.Util;
import com.icebreaker.timelapse.calendar.view.MyCalendar;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 小侯同学 on 2018/4/5.
 */

public class AppListActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemLongClickListener {
    private ListView appList;
    private ImageView list_error_img;
    private TextView list_error_tip;
    private AppAdapter appAdapter;
    private Calendar beginCal;
    private AppInfoHelper appInfoHelper;
    private RelativeLayout mBarView;
    private TextView list_date;
    private PopupWindow popupWindow;
    private CustomDate myDate,startDate;
    private ViewPager calendar;
    private MyCalendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        beginCal = Calendar.getInstance();
        initList(beginCal);
        setViews();
    }
    //初始化界面
    private void initList(Calendar beginCal){

        //设置起始时间为0时0分0秒
        beginCal.set(Calendar.HOUR_OF_DAY,0);
        beginCal.set(Calendar.MINUTE,0);
        beginCal.set(Calendar.SECOND,0);
        appInfoHelper = new AppInfoHelper(this);
        list_error_img = (ImageView)findViewById(R.id.list_error_img);
        list_error_tip = (TextView)findViewById(R.id.list_error_tip);
        appList = findViewById(R.id.app_list);
        appList.setOnItemLongClickListener(this);
        List<AppInfo> appInfos = appInfoHelper.getInformation(beginCal,AppListActivity.this);

        if(appInfos.size()==0){
            appList.setVisibility(View.INVISIBLE);
            list_error_img.setVisibility(View.VISIBLE);

            list_error_tip.setText("暂无记录");
            list_error_tip.setVisibility(View.VISIBLE);
            //Toast.makeText(AppListActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
        }else{
            appList.setVisibility(View.VISIBLE);
            list_error_img.setVisibility(View.INVISIBLE);
            list_error_tip.setVisibility(View.INVISIBLE);
            Collections.sort(appInfos,new SortByUseTime());
            //初始化ListView

            appAdapter = new AppAdapter();

            appList.setAdapter(appAdapter);
            appAdapter.initAppAdapter(appInfos,AppListActivity.this);
        }



    }
    private void setViews(){

        StatusBarCompat.setStatusBarColor(this, Color.WHITE);
        mBarView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.actionbar_list,null);
        ImageView back_main = (ImageView)mBarView.findViewById(R.id.back_main);
        back_main.setOnClickListener(this);
        list_date = (TextView)mBarView.findViewById(R.id.app_list_date);
        myDate = new CustomDate();
        startDate = new CustomDate(1979,1,1);
        list_date.setText(myDate.year+"-"+myDate.month+"-"+myDate.day);
        list_date.setOnClickListener(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);

    }

    /**
     * 日历显示窗口
     * @param view
     * @param myCalendar
     * @param nowDate
     * @param date
     * @param databack
     * @param i
     */
    private void showPopWindow(View view, MyCalendar myCalendar, CustomDate nowDate, CustomDate date, AppListActivity.DateBack databack, int i) {
        View contentView = LayoutInflater.from(AppListActivity.this).inflate(R.layout.view_calendar, null);
        popupWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT, Util.dip2px(this, 350), true);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(
                AppListActivity.this, R.drawable.backgroud));

        TextView left = (TextView) contentView.findViewById(R.id.btnPreMonth);
        TextView right = (TextView) contentView.findViewById(R.id.btnNextMonth);
        calendar = (ViewPager) contentView.findViewById(R.id.vp_calendar);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        myCalendar = new MyCalendar(calendar, this,databack,nowDate,date,i);
        myCalendar.setViews();
        popupWindow.showAsDropDown(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_main:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.app_list_date:
                showPopWindow(view,myCalendar,myDate,startDate,new DateBack(1),1);
                break;
            case R.id.btnPreMonth:
                calendar.setCurrentItem(calendar.getCurrentItem() - 1);
                break;
            case R.id.btnNextMonth:
                calendar.setCurrentItem(calendar.getCurrentItem() + 1);
                break;
        }
    }


    /**
     * 回调函数接口
     * @author Administrator
     *
     */
    private class DateBack implements MyCalendar.OnDateBack {

        private int i;
        public DateBack(int i) {
            // TODO Auto-generated constructor stub
            this.i = i;
        }

        @Override
        public void getDate(CustomDate date,boolean flag) {
            // TODO Auto-generated method stub
            if (i == 1) {
                myDate = date;
                beginCal.set(Calendar.YEAR,date.year);
                beginCal.set(Calendar.MONTH,date.month-1);
                beginCal.set(Calendar.DAY_OF_MONTH,date.day);
                initList(beginCal);
                list_date.setText(date.year + "-" + date.month + "-" + date.day);

                if (flag == true) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            popupWindow.dismiss();
                        }
                    }, 500);
                }
            } else {
                return;
            }

        }
        @Override
        public void getChangDate(CustomDate date) {
            // TODO Auto-generated method stub

            list_date.setText(date.year+"-"+date.month+"-"+date.day);
        }
    }

    private class SortByUseTime implements Comparator {
        public int compare(Object o1, Object o2) {
            AppInfo app1 = (AppInfo) o1;
            AppInfo app2 = (AppInfo) o2;
            if (app1.getForegroundTime() > app2.getForegroundTime()) {
                return -1;
            } else if (app1.getForegroundTime() < app2.getForegroundTime()) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        AppInfo appInfo = (AppInfo)adapterView.getItemAtPosition(i);
        String sqlQuery = "SELECT * FROM appType WHERE package = ? ";
         MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(this,"wimt.db",null,1);
         SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,new String[]{appInfo.getAppPackage()});
        if(cursor.moveToFirst()){

        }else{
            configView(appInfo);
        }
        myDBOpenHelper.close();
        return false;
    }

    @Override
    public void onRestart(){
        super.onRestart();
        initList(beginCal);
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this,MainActivity
                .class));
        finish();
        super.onBackPressed();
    }


    public void configView(final AppInfo appInfo){
        LinearLayout configForm = (LinearLayout)getLayoutInflater().inflate(R.layout.config_app,null);
        final RadioGroup typeRadio = (RadioGroup)configForm.findViewById(R.id.type_radio);
        final String app_package = appInfo.getAppPackage();
        final String app_type = appInfo.getType();
        final String[] tempStr = new String[2];
        for (int j = 0; j < typeRadio.getChildCount(); j++) {
            RadioButton rd = (RadioButton) typeRadio.getChildAt(j);
            if (rd.getText().equals(app_type)) {
                rd.setChecked(true);
                break;
            }
        }

        final MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(this,"wimt.db",null,1);
        final SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        new AlertDialog.Builder(this)
                .setIcon(appInfo.getIcon())
                .setTitle("为"+appInfo.getAppName()+" 设置标签")
                .setView(configForm)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < typeRadio.getChildCount(); j++) {
                            RadioButton rd = (RadioButton) typeRadio.getChildAt(j);
                            if (rd.isChecked()) {
                                tempStr[0] = (String) rd.getText();
                                break;
                            }
                        }

                        //String updateSql = "UPDATE appType SET type = ?  WHERE package = ?";
                        String createSql = "INSERT INTO  appType(type,package) VALUES(?,?)";
                        db.execSQL(createSql,new String[]{tempStr[0],app_package});
                        //db.execSQL(createSql,new String[]{date,"2"});
                       // db.execSQL(updateSql,new String[]{tempStr[0],app_package});
                        //db.execSQL("update appType set type = '"+tempStr[0]+"' where package = '"+app_package+"';");
                       // db.execSQL("insert into appType (type,package) values ('测试','com.test');");
                        myDBOpenHelper.close();
                        initList(Calendar.getInstance());
                        AppInfoHelper appInfoHelper2 = new AppInfoHelper(AppListActivity.this);
                        List<AppInfo> appInfos = appInfoHelper2.getInformation(beginCal,AppListActivity.this);
                        Log.e("ListInf",appInfos.toString());
                        //Toast.makeText(AppListActivity.this,appInfos.toString(),Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
}
