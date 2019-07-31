package com.icebreaker.timelapse.person;

/**
 * 对战记录
 * @author Marhong
 * @time 2018/5/25 19:47
 */
public class Record {
    private long id; // 对战记录唯一编号
    private String initiator; // 对战发起者名字
    private String receiver; // 对战接受者名字
    private String time; // 对战开始时间
    private String timestring; // 年月日组成，方便查询每日目标
    private int initiatorResult; // 对战发起者的结果
    private int receiverResult; // 对战接受者的结果
    private int iniUnfinishedNum; // 对战发起者未达标数目
    private int recUnfinishedNum; // 对战接受者未达标数目

    public Record(long id, String initiator, String receiver, String time,String timestring, int initiatorResult, int receiverResult,int iniUnfinishedNum,int recUnfinishedNum) {
        this.id = id;
        this.initiator = initiator;
        this.receiver = receiver;
        this.time = time;
        this.timestring = timestring;
        this.initiatorResult = initiatorResult;
        this.receiverResult = receiverResult;
        this.iniUnfinishedNum = iniUnfinishedNum;
        this.recUnfinishedNum = recUnfinishedNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimestring() {
        return timestring;
    }

    public void setTimestring(String timestring) {
        this.timestring = timestring;
    }

    public int getInitiatorResult() {
        return initiatorResult;
    }

    public void setInitiatorResult(int initiatorResult) {
        this.initiatorResult = initiatorResult;
    }

    public int getReceiverResult() {
        return receiverResult;
    }

    public void setReceiverResult(int receiverResult) {
        this.receiverResult = receiverResult;
    }

    public int getIniUnfinishedNum() {
        return iniUnfinishedNum;
    }

    public void setIniUnfinishedNum(int iniUnfinishedNum) {
        this.iniUnfinishedNum = iniUnfinishedNum;
    }

    public int getRecUnfinishedNum() {
        return recUnfinishedNum;
    }

    public void setRecUnfinishedNum(int recUnfinishedNum) {
        this.recUnfinishedNum = recUnfinishedNum;
    }

    @Override
    public String toString() {
        return id+"_"+initiator+"_"+receiver+"_"+time+"_"+initiatorResult+"_"+receiverResult;
    }
}
