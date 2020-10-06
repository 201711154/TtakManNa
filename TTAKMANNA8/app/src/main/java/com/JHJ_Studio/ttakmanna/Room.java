package com.JHJ_Studio.ttakmanna;

import java.io.Serializable;

public class Room implements Serializable {
    private int roomKey;
    private String roomName;
    private int closed = -1;
    private int mode = -1;
    private int number = -1;
    private int purpose = -1;

    public void setRoomKey(int roomKey){this.roomKey = roomKey;}
    public void setRoomName(String roomName){this.roomName = roomName;}
    public void setClosed(int closed){this.closed = closed;}
    public void setMode(int mode){this.mode = mode;}
    public void setNumber(int num){this.number = num;}
    public void setPurpose(int purpose){this.purpose = purpose;}

    public int getRoomKey(){return roomKey;}
    public String getRoomName(){return roomName;}
    public int getClosed(){return closed;}
    public int getMode(){return mode;}
    public int getNumber(){return number;}
    public int getPurpose(){return purpose;}
}
