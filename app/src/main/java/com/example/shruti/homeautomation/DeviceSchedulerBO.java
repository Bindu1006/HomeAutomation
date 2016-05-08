package com.example.shruti.homeautomation;

import java.io.Serializable;

/**
 * Created by Shruti  on 5/3/16.
 */
public class DeviceSchedulerBO implements Serializable {

    private String deviceName;

    private String deviceIP;

    private String deviceStatus;

    private String alarmTime;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceIP() {
        return deviceIP;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    @Override
    public String toString() {
        return "DeviceSchedulerBO{" +
                "deviceName='" + deviceName + '\'' +
                ", deviceIP='" + deviceIP + '\'' +
                ", deviceStatus='" + deviceStatus + '\'' +
                ", alarmTime='" + alarmTime + '\'' +
                '}';
    }
}
