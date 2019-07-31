package com.icebreaker.timelapse.apppart;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.icebreaker.timelapse.util.MyDBOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 小侯同学 on 2018/4/5.
 */

public class AppInfoHelper {
    private Context context;
    public AppInfoHelper(Context context){
        this.context = context;
    }


    public long getTotalUseTime(List<AppInfo> appInfos){
        long totalUseTime = 0;
        for(AppInfo appInfo : appInfos){
            totalUseTime = totalUseTime + appInfo.getForegroundTime();
        }
        return totalUseTime;
    }
    public long getTotalStudyTime(List<AppInfo> appInfos){
        long totalStudyTime = 0;
        for(AppInfo appInfo : appInfos){
            if(checkType(appInfo).equals("学习")){
                totalStudyTime += appInfo.getForegroundTime();
            }
        }
        return totalStudyTime;
    }
    public long getTotalWorkTime(List<AppInfo> appInfos){
        long totalStudyTime = 0;
        for(AppInfo appInfo : appInfos){
            if(checkType(appInfo).equals("工作")){
                totalStudyTime += appInfo.getForegroundTime();
            }
        }
        return totalStudyTime;
    }
    public long getTotalPlayTime(List<AppInfo> appInfos){
        long totalStudyTime = 0;
        for(AppInfo appInfo : appInfos){
            if(checkType(appInfo).equals("娱乐")){
                totalStudyTime += appInfo.getForegroundTime();
            }
        }
        return totalStudyTime;
    }
    public long getTotalOtherTime(List<AppInfo> appInfos){
        long totalStudyTime = 0;
        totalStudyTime = getTotalUseTime(appInfos) - getTotalStudyTime(appInfos) - getTotalWorkTime(appInfos)-getTotalPlayTime(appInfos);
        return totalStudyTime;
    }
    public long getTotalUseCount(List<AppInfo> appInfos){
        return appInfos.size();
    }

    //获取应用信息
    public List<AppInfo> getInformation(Calendar beginCal,Context context){
        List<AppInfo> appInfoList = new ArrayList<AppInfo>();
        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(context,"wimt.db",null,1);
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        String sqlQuery = "SELECT * FROM appUsageStats WHERE date = ?";
        String date = String.valueOf(beginCal.get(Calendar.YEAR)+" "+(beginCal.get(Calendar.MONTH)+1)+" "+beginCal.get(Calendar.DAY_OF_MONTH));
        Cursor cursor = db.rawQuery(sqlQuery,new String[]{date});
        if(cursor.moveToFirst()){
            do {
                AppInfo appInfo = new AppInfo();
                appInfo.setAppPackage(cursor.getString(cursor.getColumnIndex("package")));
                appInfo.setForegroundTime(cursor.getLong(cursor.getColumnIndex("time")));
                appInfo.setLaunchCount(cursor.getInt(cursor.getColumnIndex("count")));
                PackageManager pm = context.getPackageManager();
                try {
                    ApplicationInfo applicationInfo=pm.getApplicationInfo(appInfo.getAppPackage(), PackageManager.GET_META_DATA);
                    appInfo.setIcon(applicationInfo.loadIcon(pm));
                    appInfo.setAppName(applicationInfo.loadLabel(pm).toString());
                } catch (PackageManager.NameNotFoundException e) {
                    appInfo.setAppName("此应用已卸载");
                }
                /*
                String appType = cursor.getString(cursor.getColumnIndex("type"));
                if(appType!=null){
                    appInfo.setType(appType);
                }else{
                    appInfo.setType("其他");
                    String createSql = "UPDATE appUsageStats SET type = ? WHERE package = ?";
                    db.execSQL(createSql,new String[]{appInfo.getType(),appInfo.getAppPackage()});
                }
                */
                getAppConfig(appInfo);
                appInfoList.add(appInfo);
            }while(cursor.moveToNext());
        }
        myDBOpenHelper.close();
        return appInfoList;
    }
    //获取应用配置信息

    public void  getAppConfig(AppInfo appInfo){
        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(context,"wimt.db",null,1);
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        String appPackage = appInfo.getAppPackage();
        String sqlQuery = "SELECT type FROM appType WHERE package = ?";
        Cursor cursor = db.rawQuery(sqlQuery,new String[]{appPackage});
        if(cursor.moveToFirst()){
            appInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
        }else{
            appInfo.setType("其他");
            String createSql = "UPDATE appType SET type = ? WHERE package = ?";
            db.execSQL(createSql,new String[]{appInfo.getType(),appPackage});
        }
        myDBOpenHelper.close();
    }

    public String checkType(AppInfo appInfo){
        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(context,"wimt.db",null,1);
        SQLiteDatabase db = myDBOpenHelper.getWritableDatabase();
        String appPackage = appInfo.getAppPackage();
        String sqlQuery = "SELECT type FROM appType WHERE package = ?";
        Cursor cursor = db.rawQuery(sqlQuery,new String[]{appPackage});
        if(cursor.moveToFirst()){
            myDBOpenHelper.close();
            return cursor.getString(cursor.getColumnIndex("type"));
        }else{
            myDBOpenHelper.close();
            return "其他";
        }

    }

}
