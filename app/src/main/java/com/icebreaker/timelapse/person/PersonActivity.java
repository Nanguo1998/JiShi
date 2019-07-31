package com.icebreaker.timelapse.person;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.icebreaker.timelapse.MainActivity;
import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.apppart.OtherStatisticActivity;
import com.icebreaker.timelapse.apppart.PlayStatisticActivity;
import com.icebreaker.timelapse.apppart.WorkStatisticActivity;
import com.icebreaker.timelapse.fragment.BaseFragment;
import com.icebreaker.timelapse.fragment.HonorFragment;
import com.icebreaker.timelapse.fragment.RecordFragment;
import com.icebreaker.timelapse.fragment.TodayFragment;

/**
 * 个人中心界面
 * @author Marhong
 * @time 2018/5/25 15:31
 */
public class PersonActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView mBack,mSetting;
    private RelativeLayout mBarView;

    public static String curFragmentTag;
    public RecordFragment recordFragment;
    public HonorFragment honorFragment;
    public TodayFragment todayFragment;
   // private View recordPage, honorPage, todayPage;
   // private ImageView imageRecord, imageHonor, imageToday;
    private TextView textRecord, textHonor, textToday,textUserName,textUserSig;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private int page = 0;
    private SharedPreferences FragmentPage;
    public static Activity activity;
    private SharedPreferences userInfo;// 存储个人信息
    private static final int GET_VERSION_RESULT = 1;
    private float x_temp01 = 0.0f;
    private float y_temp01 = 0.0f;
    private float x_temp02 = 0.0f;
    private float y_temp02 = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        initViews();
        setListener();
        read();
        mFragmentManager = getSupportFragmentManager();
        if (page == 1) {
            setTabSelection(getString(R.string.str_record));
        } else if (page == 2) {
            setTabSelection(getString(R.string.str_honor));
        } else if (page == 3) {
            setTabSelection(getString(R.string.str_today));
        } else if (page == 0) {
            setCurrentFragment();
        }
    }
    
    /**
     * 初始化个人中心界面所有控件
     * @author Marhong
     * @time 2018/5/25 15:33
     */
    private void initViews(){
        // 初始化ActionBar
        StatusBarCompat.setStatusBarColor(this, Color.WHITE);
        mBarView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.actionbar_person,null);
        mBack = (ImageView)mBarView.findViewById(R.id.back);
        mSetting = (ImageView)mBarView.findViewById(R.id.setting);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        // 初始化ViewPage
        userInfo = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        FragmentPage = getSharedPreferences("fragmentinfo", MODE_PRIVATE);


        textRecord = (TextView) findViewById(R.id.text_record);
        textHonor = (TextView) findViewById(R.id.text_honor);
        textToday = (TextView) findViewById(R.id.text_today);
        textUserName = (TextView)findViewById(R.id.textUserName);
        textUserSig =(TextView)findViewById(R.id.textUserSig);
        textUserName.setText(userInfo.getString("userName","不过六级不改名"));
        textUserSig.setText(userInfo.getString("signature","好好学习,天天向上"));
    }
    /**
     * 给所有控件设置监听器
     * @author Marhong
     * @time 2018/5/25 15:34
     */
    private void setListener(){
        // ActionBar部分
        mBack.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        // ViewPage部分
        textRecord.setOnClickListener(this);
        textHonor.setOnClickListener(this);
        textToday.setOnClickListener(this);
//        recordPage.setOnClickListener(this);
//        honorPage.setOnClickListener(this);
//        todayPage.setOnClickListener(this);
    }
    /**
     * 从SharedPreference中读取当前页面
     * @author Marhong
     * @time 2018/5/25 16:35
     */
    private void read() {
        // TODO Auto-generated method stub
        page = FragmentPage.getInt("fragment", 0);
    }
    /**
     * 设置当前Page
     * @author Marhong
     * @time 2018/5/25 16:37
     */
    public void setTabSelection(String tag) {

        clearSelection();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (TextUtils.equals(tag, getString(R.string.str_record))) {

            textRecord.setTextColor(Color.rgb(251,2,5));
//            imageRecord.setVisibility(View.VISIBLE);
//            imageHonor.setVisibility(View.INVISIBLE);
//            imageToday.setVisibility(View.INVISIBLE);
            if (recordFragment == null) {
                recordFragment = new RecordFragment();
            }

        } else if (TextUtils.equals(tag, getString(R.string.str_honor))) {
//            imageRecord.setVisibility(View.INVISIBLE);
//            imageHonor.setVisibility(View.VISIBLE);
//            imageToday.setVisibility(View.INVISIBLE);
            textHonor.setTextColor(Color.rgb(251,2,5));
            if (honorFragment == null) {
                honorFragment = new HonorFragment();
            }

        } else if (TextUtils.equals(tag, getString(R.string.str_today))) {
//            imageRecord.setVisibility(View.INVISIBLE);
//            imageHonor.setVisibility(View.INVISIBLE);
//            imageToday.setVisibility(View.VISIBLE);
            textToday.setTextColor(Color.rgb(251,2,5));
            if (todayFragment == null) {
                todayFragment = new TodayFragment();
            }

        }
        switchFragment(tag);

    }
    /**
     * 设置RecordPage为默认Page
     * @author Marhong
     * @time 2018/5/25 16:38
     */
    private void setCurrentFragment() {
        clearSelection();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        textRecord.setTextColor(Color.rgb(251,2,5));
        write(1);
        if (recordFragment == null) {
            recordFragment = new RecordFragment();
            mFragmentTransaction.add(R.id.content, recordFragment, getString(R.string.str_record));
            commitTransactions();
        }
        curFragmentTag = getString(R.string.str_record);
    }
    /**
     * 使选中状态的字体回复原状
     * @author Marhong
     * @time 2018/5/25 16:52
     */
    private void clearSelection() {

        textRecord.setTextColor(Color.parseColor("#82858b"));

        textHonor.setTextColor(Color.parseColor("#82858b"));

        textToday.setTextColor(Color.parseColor("#82858b"));
//        imageRecord.setVisibility(View.VISIBLE);
//        imageHonor.setVisibility(View.INVISIBLE);
//        imageToday.setVisibility(View.INVISIBLE);

    }
    /**
     * 根据选择切换Page
     * @author Marhong
     * @time 2018/5/25 16:45
     */
    public void switchFragment(String tag) {
        if (TextUtils.equals(tag, curFragmentTag)) {
            return;
        }

        if (curFragmentTag != null) {
            detachFragment(getFragment(curFragmentTag));

        }
        attachFragment(R.id.content, getFragment(tag), tag);
        curFragmentTag = tag;
        commitTransactions();
    }
    /**
     * 在当前页面移除Fragment
     * @author Marhong
     * @time 2018/5/25 16:45
     */
    private void detachFragment(Fragment f) {

        if (f != null && !f.isDetached()) {
            ensureTransaction();
            mFragmentTransaction.detach(f);
        }
    }
    /**
     * 在当前页面添加Fragment
     * @author Marhong
     * @time 2018/5/25 16:46
     */
    private void attachFragment(int layout, Fragment f, String tag) {
        if (f != null) {
            if (f.isDetached()) {
                ensureTransaction();
                mFragmentTransaction.attach(f);

            } else if (!f.isAdded()) {
                ensureTransaction();
                mFragmentTransaction.add(layout, f, tag);
            }
        }
    }
    /**
     * 确保完成一次事务
     * @author Marhong
     * @time 2018/5/25 16:47
     */
    private FragmentTransaction ensureTransaction() {
        if (mFragmentTransaction == null) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        }
        return mFragmentTransaction;

    }
    /**
     * 提交事务
     * @author Marhong
     * @time 2018/5/25 16:47
     */
    private void commitTransactions() {
        if (mFragmentTransaction != null && !mFragmentTransaction.isEmpty()) {
            mFragmentTransaction.commit();
            mFragmentTransaction = null;
        }
    }
    /**
     * 根据选择获取相应Fragment
     * @author Marhong
     * @time 2018/5/25 16:48
     */
    private Fragment getFragment(String tag) {

        Fragment f = mFragmentManager.findFragmentByTag(tag);

        if (f == null) {
            f = BaseFragment.newInstance(getApplicationContext(), tag);
        }
        return f;

    }
    /**
     * 将选中的页面存入到SharedPreference中
     * @author Marhong
     * @time 2018/5/25 16:49
     */
    private void write(int i) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor edit = FragmentPage.edit();
        edit.putInt("fragment", i);
        edit.commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                write(0);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.setting:
                startActivity(new Intent(this,SettingActivity.class));
                finish();
                break;
            case R.id.text_record:
                write(1);
                setTabSelection(getString(R.string.str_record));
                break;
            case R.id.text_honor:
                write(2);
                setTabSelection(getString(R.string.str_honor));
                break;
            case R.id.text_today:
                write(3);
                setTabSelection(getString(R.string.str_today));
                break;
        }

    }
    /**
     * 通过在屏幕左右滑动切换Page
     * @author Marhong
     * @time 2018/5/25 17:29
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
                        read();
                        switch (page){
                            case 0:
                                write(2);
                                setTabSelection(getString(R.string.str_honor));
                                break;
                            case 1:
                                write(2);
                                setTabSelection(getString(R.string.str_honor));
                                break;
                            case 2:
                                write(3);
                                setTabSelection(getString(R.string.str_today));
                                break;
                        }
                    }
                    //移动了x_temp01-x_temp02
                }

                if (x_temp01 < x_temp02)//向右
                {
                    read();
                    switch (page){
                        case 2:
                            write(1);
                            setTabSelection(getString(R.string.str_record));
                            break;
                        case 3:
                            write(2);
                            setTabSelection(getString(R.string.str_honor));
                            break;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }


        }
        return super.onTouchEvent(event);
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        write(0);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
