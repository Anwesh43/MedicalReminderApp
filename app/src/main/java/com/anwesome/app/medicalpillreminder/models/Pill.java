package com.anwesome.app.medicalpillreminder.models;

import java.util.*;

import io.realm.RealmList;
import io.realm.RealmObject;
/**
 * Created by anweshmishra on 12/02/17.
 */
public class Pill extends RealmObject{
    private String name;
    private String notificationTimes = "";
    private int id;
    private int pillsNumber = 0;
    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setPillsNumber(int pillsNumber) {
        this.pillsNumber = pillsNumber;
    }
    public void changePill(int dir) {
        this.pillsNumber+=dir;
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getPillsNumber() {
        return pillsNumber;
    }

    public String getNotificationTimes() {
        return notificationTimes;
    }

    public void setNotificationTimes(String notificationTimes) {
        this.notificationTimes = notificationTimes;
    }
    public void addNotification(String notification) {
        if(notification  == null || notificationTimes.equals("")) {
            notificationTimes = notification;
        }
        else {
            notificationTimes = notificationTimes + "," + notification;
        }
    }
    public int hashCode() {
        return name.hashCode()+id+pillsNumber+notificationTimes.hashCode();
    }
}
