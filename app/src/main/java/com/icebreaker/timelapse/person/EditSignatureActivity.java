package com.icebreaker.timelapse.person;

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
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.internet.HttpGetData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditSignatureActivity extends AppCompatActivity implements View.OnClickListener{
    private  EditText newSignature;
    private SharedPreferences userinfo;
    private static final int GET_MODIFY_RESULT = 1;
    private static final String SUCCESS = "success";
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_MODIFY_RESULT:
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
        setContentView(R.layout.activity_edit_signature);
        initViews();
    }
    /**
     * 初始化所有控件
     * @author Marhong
     * @time 2018/5/30 0:12
     */
    private void initViews(){
        RelativeLayout mBarView;
        ImageView mBack;
        TextView confirm;
        // 初始化ActionBar
        StatusBarCompat.setStatusBarColor(this, Color.WHITE);
        mBarView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.actionbar_edit_signature,null);
        mBack = (ImageView)mBarView.findViewById(R.id.back);
        mBack.setOnClickListener(this);
        confirm = (TextView)mBarView.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);

         newSignature = (EditText)findViewById(R.id.newSignature);
         userinfo = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        newSignature.setText(userinfo.getString("signature","不过六级不改名"));
    }
    
    /**
     * 修改个性签名
     * @author Marhong
     * @time 2018/5/29 23:49
     */
    private void modifySignature(){
        String newsignature = newSignature.getText().toString();
        if (!checkNetworkState()) {
            Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
        } else if (newsignature.length()>20) {
            Toast.makeText(this, "签名长度应在20字以内", Toast.LENGTH_SHORT).show();
        } else {
            final ArrayList<NameValuePair> arrayValues = new ArrayList<NameValuePair>();
            arrayValues.add(new BasicNameValuePair("userName", userinfo.getString("userName",null)));
            arrayValues.add(new BasicNameValuePair("signature", newsignature));
            new Thread(new Runnable() { // 开启线程上传文件
                @Override
                public void run() {
                    String loginResult = "";
                    loginResult = HttpGetData.GetData("http://192.168.1.112:8080/Timelapse/modifySignatureServlet", arrayValues);
                    Message msg = new Message();
                    msg.obj = loginResult;
                    msg.what = GET_MODIFY_RESULT;
                    uiHandler.sendMessage(msg);
                }
            }).start();

        }
    }
    /**
     * 显示修改结果
     * @author Marhong
     * @time 2018/5/30 0:13
     */
    private void showResult(String result){
        Log.e("修改签名结果",result);
        try{
            JSONObject object = new JSONObject(result);
            String type = object.getString("type");
            if(type.equals("success")){
                Log.e("成功修改签名","修改成功");
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = userinfo.edit();
                editor.putString("signature",newSignature.getText().toString());
                editor.commit();
            }else{
                Log.e("修改签名失败",result+"_"+SUCCESS);
                Toast.makeText(this,"修改签名失败",Toast.LENGTH_SHORT).show();
                newSignature.setText("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(this,SettingActivity.class));
                finish();
                break;
            case R.id.confirm:
                modifySignature();
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
