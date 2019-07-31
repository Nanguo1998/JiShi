package com.icebreaker.timelapse.apppart;

import android.graphics.drawable.Drawable;

/**
 * Created by 小侯同学 on 2018/4/5.
 */

public class AppInfo {
    private Drawable icon;
    private String appPackage;
    private String appName;
    private long foregroundTime;
    private int launchCount;
    private String type;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getForegroundTime() {
        return foregroundTime;
    }

    public void setForegroundTime(long foregroundTime) {
        this.foregroundTime = foregroundTime;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return appName+" "+type;
    }
}
