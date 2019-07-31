package com.icebreaker.timelapse.person;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.githang.statusbar.StatusBarCompat;
import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.StartPage.LoginActivity;

/**
 * 设置个人信息
 * @author Marhong
 * @time 2018/5/29 17:19
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }
    /**
     * 初始化个人信息界面所有控件
     * @author Marhong
     * @time 2018/5/29 17:29
     */
    private void initView(){
         RelativeLayout mBarView,editUserName,editSignature;
         ImageView mBack;
         Button exitLogin;
        // 初始化ActionBar
        StatusBarCompat.setStatusBarColor(this, Color.WHITE);
        mBarView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.actionbar_setting,null);
        mBack = (ImageView)mBarView.findViewById(R.id.back);
        mBack.setOnClickListener(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);

        editUserName = (RelativeLayout)findViewById(R.id.editUserName);
        editSignature = (RelativeLayout)findViewById(R.id.editSignature);
        exitLogin = (Button)findViewById(R.id.exitLogin);
        editSignature.setOnClickListener(this);
        editUserName.setOnClickListener(this);
        exitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editUserName:
                startActivity(new Intent(this,EditUserNameActivity.class));
                this.finish();
                break;
            case R.id.editSignature:
                startActivity(new Intent(this,EditSignatureActivity.class));
                this.finish();
                break;
            case R.id.exitLogin:
                SharedPreferences UserInfo = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = UserInfo.edit();
                edit.clear();
                edit.commit();
                startActivity(new Intent(this, LoginActivity.class));
                this.finish();
                break;
            case R.id.back:
                startActivity(new Intent(this,PersonActivity.class));
                finish();
                break;
        }
    }
}
