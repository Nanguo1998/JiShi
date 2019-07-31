package com.icebreaker.timelapse.addresspart;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.icebreaker.timelapse.MainActivity;
import com.icebreaker.timelapse.R;
import com.icebreaker.timelapse.StartPage.LoginActivity;

public class StartPosterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_poster);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(StartPosterActivity.this,LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        },3000);

    }
}
