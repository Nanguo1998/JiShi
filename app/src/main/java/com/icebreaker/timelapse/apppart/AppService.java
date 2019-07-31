package com.icebreaker.timelapse.apppart;

import android.app.KeyguardManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.icebreaker.timelapse.util.MyDBOpenHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 小侯同学 on 2018/4/5.
 */

public class AppService extends Service {
    public static final String TAG = "UnlockService";
    private MyThread myThread = null;

    private BroadcastReceiver screenUnlockReceiver;
    private SQLiteDatabase db;
    private int unlockCount = 1;

    final Handler unlockHandler = new Handler()
    {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if(msg.what == 0x123)
            {
                updateUnlockCount();
                sendBroadcast(new Intent("com.icebreaker.timelapse.SCREEN_UNLOCK"));

            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        listenUnlock();
        myThread = new MyThread(this);
        myThread.start();
        Log.i(TAG, "Service is start.");
    }

    private void listenUnlock() {
        //屏幕解锁计数
        screenUnlockReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    unlockHandler.sendEmptyMessage(0x123);
                }
            }
        };
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(screenUnlockReceiver, itFilter);
    }


    private void updateUnlockCount(){
        //数据库操作
        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(AppService.this,"wimt.db",null,1);
        db = myDBOpenHelper.getWritableDatabase();
        Calendar todayCal = Calendar.getInstance();
        String date = String.valueOf(todayCal.get(Calendar.YEAR)+" "+(todayCal.get(Calendar.MONTH)+1)+" "+todayCal.get(Calendar.DAY_OF_MONTH));
        String sql = "SELECT count FROM unlockCount WHERE date = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{date});
        if (cursor.moveToFirst()){
            unlockCount = cursor.getInt(cursor.getColumnIndex("count"));
            unlockCount++;
        }else{
            String createSql = "INSERT INTO  unlockCount(date,count) VALUES(?,?)";
            db.execSQL(createSql,new String[]{date,"2"});
        }
        String updateSql = "UPDATE unlockCount SET count = ? WHERE date = ?";
        db.execSQL(updateSql,new String[]{String.valueOf(unlockCount),date});
        cursor.close();
        db.close();
        myDBOpenHelper.close();
    }


    private static class MyThread extends Thread {
        private Context context;
        private boolean isRun = true;
        private String currentApp = "";
        private long currentRuntime = 0;
        private MyThread(Context context) {
            this.context = context;
        }
        public void setStop() {
            isRun = false;
        }
        @Override
        public void run() {
            while (isRun) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    KeyguardManager mKeyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
                    if (!mKeyguardManager.inKeyguardRestrictedInputMode()) {
                        getTopApp(context);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }

        }

        private void getTopApp(Context context) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                if (m != null) {
                    //long now = System.currentTimeMillis();
                    Calendar now = Calendar.getInstance();
                    Calendar beginCal = Calendar.getInstance();
                    beginCal.set(Calendar.HOUR_OF_DAY,0);
                    beginCal.set(Calendar.MINUTE,0);
                    beginCal.set(Calendar.SECOND,0);
                    //获取一天之内的应用数据
                    List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginCal.getTimeInMillis(), now.getTimeInMillis());

                    String topApp = "";

                    //取得最近运行的一个app，即当前运行的app
                    if ((stats != null) && (!stats.isEmpty())) {
                        int j = 0;
                        for (int i = 0; i < stats.size(); i++) {
                            if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                                j = i;
                            }
                        }
                        topApp = stats.get(j).getPackageName();

                    }
                    Log.i(TAG, "top running app is : "+topApp);
                    if(currentApp.equals("")){
                        currentApp = topApp;
                    }

                    if(!(currentApp.equals("")&&topApp.equals(""))){
                        updateAppUsageStats(currentApp,topApp);
                        //limitAppUse(topApp);
                    }
                        currentApp = topApp;
                }
            }
        }

        private void updateAppUsageStats(String appPackage,String topApp) throws NoSuchFieldException, IllegalAccessException {

            MyDBOpenHelper dbOpenHelper = new MyDBOpenHelper(context,"wimt.db",null,1);
            SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
            Calendar todayCal = Calendar.getInstance();
            String date = String.valueOf(todayCal.get(Calendar.YEAR)+" "+(todayCal.get(Calendar.MONTH)+1)+" "+todayCal.get(Calendar.DAY_OF_MONTH));
            String sqlQuery = "SELECT time,count FROM appUsageStats WHERE package = ? AND date = ?";
            Cursor cursor = db.rawQuery(sqlQuery,new String[]{appPackage,date});
            if(cursor.moveToFirst()){
                long appTime = cursor.getLong(cursor.getColumnIndex("time"))+2;
                int appCount = cursor.getInt(cursor.getColumnIndex("count"));
                if(!currentApp.equals(topApp)){
                    appCount++;
                }
                String sqlUpdate = "UPDATE appUsageStats SET time = ? , count = ? WHERE package = ? AND date = ?";
                db.execSQL(sqlUpdate,new String[]{String.valueOf(appTime),String.valueOf(appCount),appPackage,date});
                Log.v(TAG,"时间"+date+"\t名称"+appPackage+"\t时长"+appTime+"\t次数"+appCount);
            }else{
                String sqlInsert = "INSERT INTO  appUsageStats(date,time,package,count) VALUES(?,?,?,?)";
                db.execSQL(sqlInsert,new String[]{date,"2",appPackage,"1"});
                Log.v(TAG,"时间"+date+"\t名称"+appPackage+"\t时长"+2+"\t次数"+1);
            }
            cursor.close();
            db.close();
            dbOpenHelper.close();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenUnlockReceiver);
        myThread.setStop();
        Log.i(TAG, "Service is stop.");
    }
}
