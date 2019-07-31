package com.icebreaker.timelapse.person;

/**
 * 记录用户荣誉信息
 * @author Marhong
 * @time 2018/5/27 0:59
 */
public class Honor {
    private String userName; // 用户名
    private int totalFights; // 总对战次数
    private int victoryFights; // 获胜对战次数
    private int victoryPoints; // 用户胜点
    private int nineKills; // 9连胜次数
    private int eightKills; // 8连胜次数
    private int sevenKills; // 7连胜次数
    private int sixKills; // 6连胜次数
    private int fiveKills; // 5连胜次数
    private int fourKills; // 4连胜次数
    private int maxVictory; // 最大连胜次数
    private int slainNum; // 击败的不同人数

    public Honor(String userName, int totalFights, int victoryFights, int victoryPoints, int nineKills, int eightKills, int sevenKills, int sixKills, int fiveKills, int fourKills, int maxVictory, int slainNum) {
        this.userName = userName;
        this.totalFights = totalFights;
        this.victoryFights = victoryFights;
        this.victoryPoints = victoryPoints;
        this.nineKills = nineKills;
        this.eightKills = eightKills;
        this.sevenKills = sevenKills;
        this.sixKills = sixKills;
        this.fiveKills = fiveKills;
        this.fourKills = fourKills;
        this.maxVictory = maxVictory;
        this.slainNum = slainNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalFights() {
        return totalFights;
    }

    public void setTotalFights(int totalFights) {
        this.totalFights = totalFights;
    }

    public int getVictoryFights() {
        return victoryFights;
    }

    public void setVictoryFights(int victoryFights) {
        this.victoryFights = victoryFights;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public int getNineKills() {
        return nineKills;
    }

    public void setNineKills(int nineKills) {
        this.nineKills = nineKills;
    }

    public int getEightKills() {
        return eightKills;
    }

    public void setEightKills(int eightKills) {
        this.eightKills = eightKills;
    }

    public int getSevenKills() {
        return sevenKills;
    }

    public void setSevenKills(int sevenKills) {
        this.sevenKills = sevenKills;
    }

    public int getSixKills() {
        return sixKills;
    }

    public void setSixKills(int sixKills) {
        this.sixKills = sixKills;
    }

    public int getFiveKills() {
        return fiveKills;
    }

    public void setFiveKills(int fiveKills) {
        this.fiveKills = fiveKills;
    }

    public int getFourKills() {
        return fourKills;
    }

    public void setFourKills(int fourKills) {
        this.fourKills = fourKills;
    }

    public int getMaxVictory() {
        return maxVictory;
    }

    public void setMaxVictory(int maxVictory) {
        this.maxVictory = maxVictory;
    }

    public int getSlainNum() {
        return slainNum;
    }

    public void setSlainNum(int slainNum) {
        this.slainNum = slainNum;
    }
}
