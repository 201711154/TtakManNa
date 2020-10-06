package com.JHJ_Studio.ttakmanna;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Appointment {
    private int roomKey;
    private ArrayList<Location> destination = new ArrayList<>();
    private ArrayList<Time> times = new ArrayList<>();

    public void setRoomKey(int key){this.roomKey = key;}
    public void setDestination(ArrayList<Location> loc){this.destination = loc;}
    public void setTimes(ArrayList<Time> times){this.times = times;}

    public int getRoomKey(){return roomKey;}
    public ArrayList<Location> getDestination(){return destination;}
    public ArrayList<Time> getTimes(){return times;}

}
