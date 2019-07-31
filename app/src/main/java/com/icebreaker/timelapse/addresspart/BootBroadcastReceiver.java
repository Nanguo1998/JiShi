package com.icebreaker.timelapse.addresspart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.icebreaker.timelapse.apppart.AppService;


public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent bootIntent = new Intent(context, AppService.class);
        bootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(bootIntent);

        Intent bootIntent2 = new Intent(context, LocationService.class);
        bootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(bootIntent2);
    }
}
