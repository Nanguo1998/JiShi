package com.icebreaker.timelapse.person;

/**
 * 每日目标
 * @author Marhong
 * @time 2018/5/28 9:41
 */
public class Goal {
    private String id;
    private String userName;
    private int planTotalTime;
    private int planSpareTime;
    private int planSocialTime;
    private int planStudyTime;
    private int planNewsTime;
    private int planOtherTime;
    private int actualTotalTime;
    private int actualSpareTime;
    private int actualSocialTime;
    private int actualStudyTime;
    private int actualNewsTime;
    private int actualOtherTime;
    private int unfinishedItems;


    public Goal(String id, String userName, int planTotalTime, int planSpareTime, int planSocialTime, int planStudyTime, int planNewsTime, int planOtherTime, int actualTotalTime, int actualSpareTime, int actualSocialTime, int actualStudyTime, int actualNewsTime, int actualOtherTime, int unfinishedItems) {
        this.id = id;
        this.userName = userName;
        this.planTotalTime = planTotalTime;
        this.planSpareTime = planSpareTime;
        this.planSocialTime = planSocialTime;
        this.planStudyTime = planStudyTime;
        this.planNewsTime = planNewsTime;
        this.planOtherTime = planOtherTime;
        this.actualTotalTime = actualTotalTime;
        this.actualSpareTime = actualSpareTime;
        this.actualSocialTime = actualSocialTime;
        this.actualStudyTime = actualStudyTime;
        this.actualNewsTime = actualNewsTime;
        this.actualOtherTime = actualOtherTime;
        this.unfinishedItems = unfinishedItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPlanTotalTime() {
        return planTotalTime;
    }

    public void setPlanTotalTime(int planTotalTime) {
        this.planTotalTime = planTotalTime;
    }

    public int getPlanSpareTime() {
        return planSpareTime;
    }

    public void setPlanSpareTime(int planSpareTime) {
        this.planSpareTime = planSpareTime;
    }

    public int getPlanSocialTime() {
        return planSocialTime;
    }

    public void setPlanSocialTime(int planSocialTime) {
        this.planSocialTime = planSocialTime;
    }

    public int getPlanStudyTime() {
        return planStudyTime;
    }

    public void setPlanStudyTime(int planStudyTime) {
        this.planStudyTime = planStudyTime;
    }

    public int getPlanNewsTime() {
        return planNewsTime;
    }

    public void setPlanNewsTime(int planNewsTime) {
        this.planNewsTime = planNewsTime;
    }

    public int getPlanOtherTime() {
        return planOtherTime;
    }

    public void setPlanOtherTime(int planOtherTime) {
        this.planOtherTime = planOtherTime;
    }

    public int getActualTotalTime() {
        return actualTotalTime;
    }

    public void setActualTotalTime(int actualTotalTime) {
        this.actualTotalTime = actualTotalTime;
    }

    public int getActualSpareTime() {
        return actualSpareTime;
    }

    public void setActualSpareTime(int actualSpareTime) {
        this.actualSpareTime = actualSpareTime;
    }

    public int getActualSocialTime() {
        return actualSocialTime;
    }

    public void setActualSocialTime(int actualSocialTime) {
        this.actualSocialTime = actualSocialTime;
    }

    public int getActualStudyTime() {
        return actualStudyTime;
    }

    public void setActualStudyTime(int actualStudyTime) {
        this.actualStudyTime = actualStudyTime;
    }

    public int getActualNewsTime() {
        return actualNewsTime;
    }

    public void setActualNewsTime(int actualNewsTime) {
        this.actualNewsTime = actualNewsTime;
    }

    public int getActualOtherTime() {
        return actualOtherTime;
    }

    public void setActualOtherTime(int actualOtherTime) {
        this.actualOtherTime = actualOtherTime;
    }

    public int getUnfinishedItems() {
        return unfinishedItems;
    }

    public void setUnfinishedItems(int unfinishedItems) {
        this.unfinishedItems = unfinishedItems;
    }
}
