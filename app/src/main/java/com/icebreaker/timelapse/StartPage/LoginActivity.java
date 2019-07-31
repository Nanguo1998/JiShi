package com.icebreaker.timelapse.StartPage;


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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.icebreaker.timelapse.MainActivity;
import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.internet.HttpGetData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 登录界面
 * @author Marhong
 * @time 2018/5/25 0:08
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mUserName, mPassword;
    private TextView mRetrievePwd, mRegister;
    private Button mLogin;
    private static final String ERROR_CODE = "0204";
    private static final int GET_LOGININ_RESULT_DATA = 1;
    private static final String GET_SUCCESS_RESULT = "success";
    private static final String GET_INCORRECT_RESULT = "incorrect";
    private SharedPreferences userInf;// 存储个人信息
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_LOGININ_RESULT_DATA:
                    GetDataDetailFromLoginResultData(msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 判断是否保持登录状态
        if(!isLogin()){
            // 如果不是则需要重新输入用户名和密码
            initViews();
            setListener();
        }else{
            // 如果是则直接进入APP首页
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

    }
    
    /**
     * 判断用户是否保持登录
     * @author Marhong
     * @time 2018/5/25 16:04
     */
    private boolean isLogin(){

            return true;

    }
    /**
     * 初始化登录界面所有控件
     *
     * @author Marhong
     * @time 2018/5/25 0:09
     */
    private void initViews() {
        userInf = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        mUserName = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mRegister = (TextView) findViewById(R.id.register);
        mRetrievePwd = (TextView) findViewById(R.id.retrievePwd);
        mLogin = (Button) findViewById(R.id.login);
        StatusBarCompat.setStatusBarColor(this, Color.WHITE);

    }

    /**
     * 给所有控件设置监听器
     *
     * @author Marhong
     * @time 2018/5/25 0:10
     */
    private void setListener() {
        mUserName.setOnClickListener(this);
        mPassword.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mRetrievePwd.setOnClickListener(this);
        mLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
            case R.id.login:
                verifyAccount();
                break;
        }
    }
    
    /**
     * 验证用户登录信息是否正确
     * @author Marhong
     * @time 2018/5/25 15:32
     */
    private void verifyAccount() {
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        if (!checkNetworkState()) {
            Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
        } else if (userName.equals("") || userName == null) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
        } else if (password.equals("") || password == null) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            final ArrayList<NameValuePair> arrayValues = new ArrayList<NameValuePair>();
            arrayValues.add(new BasicNameValuePair("userName", userName));
            arrayValues.add(new BasicNameValuePair("password", password));
            new Thread(new Runnable() { // 开启线程上传文件
                @Override
                public void run() {
                    String loginResult = "";
                    loginResult = HttpGetData.GetData("http://192.168.1.112:8080/Timelapse/loginServlet", arrayValues);
                    Message msg = new Message();
                    msg.obj = loginResult;
                    msg.what = GET_LOGININ_RESULT_DATA;
                    uiHandler.sendMessage(msg);
                }
            }).start();
            mLogin.setText("正在登录...");
        }
    }

    /**
     * 获取服务器返回的详细信息
     *
     * @author Marhong
     * @time 2018/5/25 1:11
     */
    public void GetDataDetailFromLoginResultData(Object obj) {
        Log.e("result", obj.toString());
        // TODO Auto-generated method stub
        mLogin.setText("登录");
        try {
            JSONObject result = new JSONObject(obj.toString());
            String type = result.getString("type");

            if (type.equals(GET_SUCCESS_RESULT)) {
                Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                WriteUserInfo(result);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else if (type.equals(GET_INCORRECT_RESULT)) {
                Toast.makeText(getApplicationContext(), "用户名和密码不匹配", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "登录错误", Toast.LENGTH_SHORT).show();
            }
        }  catch (Exception e) {
            // TODO: handle exception
        }
    }
    /**
     * 将服务器返回的用户信息存入到SharedPreference中
     * @author Marhong
     * @time 2018/5/25 10:53
     */
    private void WriteUserInfo(JSONObject user) {
        // TODO Auto-generated method stub
        try {
            SharedPreferences.Editor edit = userInf.edit();
            edit.putString("userName", user.getString("userName"));
            edit.putString("password", user.getString("password"));
            edit.putString("signature", user.getString("signature"));
            edit.putInt("victoryPoint", user.getInt("victoryPoint"));
            edit.putInt("head",user.getInt("head"));
            edit.putBoolean("keepLogin",true);
            edit.commit();
        }catch (Exception e){
            e.printStackTrace();
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