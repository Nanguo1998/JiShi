package com.icebreaker.timelapse.StartPage;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.icebreaker.timelapse.MainActivity;
import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.internet.HttpGetData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 注册界面
 * @author Marhong
 * @time 2018/5/25 0:35
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mUserName,mPassword,mConfirmPassword,mSignature  ;
    private Button mNextStep;
    private ImageView mBackLogin;
    private RelativeLayout mBarView;
    private static final int GET_REGISTER_RESULT = 1;
    private SharedPreferences userinfo;
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_REGISTER_RESULT:
                    showResult(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        setListener();
    }
    
    /**
     * 初始化注册界面所有控件
     * @author Marhong
     * @time 2018/5/25 0:37
     */
    private void initViews(){
        userinfo = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mUserName = (EditText)findViewById(R.id.username);
        mPassword = (EditText)findViewById(R.id.password);
        mNextStep = (Button)findViewById(R.id.nextStep);
        mSignature = (EditText)findViewById(R.id.signature);
        mConfirmPassword = (EditText)findViewById(R.id.confirmPassword);
        // 设置ActionBar
        StatusBarCompat.setStatusBarColor(this, Color.WHITE);
        mBarView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.actionbar_register,null);
        mBackLogin = (ImageView)mBarView.findViewById(R.id.back_login);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
    }
    private void setListener(){
        mUserName.setOnClickListener(this);
        mPassword.setOnClickListener(this);
        mBackLogin.setOnClickListener(this);
        mNextStep.setOnClickListener(this);
    }
    /**
     * 注册新用户名
     * @author Marhong
     * @time 2018/5/30 9:43
     */
    private void registerAccount(){
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        String signature = mSignature.getText().toString();
        if (!checkNetworkState()) {
            Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
        } else if (userName == null || userName.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else if (userName.length()>10) {
            Toast.makeText(this, "用户名应在10字以内", Toast.LENGTH_SHORT).show();
        }else if (password == null || password.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else if (password.length()<6) {
            Toast.makeText(this, "密码至少为6位", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(confirmPassword)){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        }else if(signature.length()>20){
            Toast.makeText(this, "个性签名长度应在20字以内", Toast.LENGTH_SHORT).show();
        } else{
            final ArrayList<NameValuePair> arrayValues = new ArrayList<NameValuePair>();
            arrayValues.add(new BasicNameValuePair("userName", userName));
            arrayValues.add(new BasicNameValuePair("signature", signature));
            arrayValues.add(new BasicNameValuePair("password", password));
            new Thread(new Runnable() { // 开启线程上传文件
                @Override
                public void run() {

                    String loginResult =  HttpGetData.GetData("http://192.168.1.112:8080/Timelapse/registerAccountServlet", arrayValues);
                    Log.e("直接返回的结果",loginResult+"_结果");
                    Message msg = new Message();
                    msg.obj = loginResult;
                    msg.what = GET_REGISTER_RESULT;
                    uiHandler.sendMessage(msg);
                }
            }).start();

        }
    }
    /**
     * 显示注册结果
     * @author Marhong
     * @time 2018/5/30 0:13
     */
    private void showResult(String result){
       // Log.e("修改签名结果",result+"_中文");
        try{
            JSONObject object = new JSONObject(result);
            String type = object.getString("type");

            if(type.equals("success")){

                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = userinfo.edit();
                editor.putString("signature",mSignature.getText().toString());
                editor.putString("userName",mUserName.getText().toString());
                editor.putInt("victoryPoint",0);
                editor.putInt("head",1);
                editor.putBoolean("keepLogin",true);
                editor.commit();
                startActivity(new Intent(this, MainActivity.class));
            }else if(type.equals("exist")){

                Toast.makeText(this,"该用户名已存在",Toast.LENGTH_SHORT).show();
                mUserName.setText("");
            }else if(type.equals("error")){
                Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_login:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
                break;
            case R.id.nextStep:
                registerAccount();
                break;
        }
    }
    /**
     * 检查是否联网
     *
     * @author Marhong
     * @time 2018/5/25 2:08
     */
    private boolean checkNetworkState() {
        boolean flag = false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }

        return flag;
    }
}
