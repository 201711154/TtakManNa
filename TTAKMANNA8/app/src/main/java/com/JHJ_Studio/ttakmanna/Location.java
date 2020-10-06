package com.JHJ_Studio.ttakmanna;

public class Location {
    private String loc_name;
    private float latitude;
    private float longitude;

    public void setLoc_name(String name){this.loc_name = name;}
    public void setLatitude(float latitude){this.latitude = latitude;}
    public void setLongitude(float longitude){this.longitude = longitude;}

    public String getLoc_name(){return loc_name;}
    public float getLatitude(){return latitude;}
    public float getLongitude(){return longitude;}
}
