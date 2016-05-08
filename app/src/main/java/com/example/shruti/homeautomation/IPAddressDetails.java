package com.example.shruti.homeautomation;

import java.io.Serializable;

/**
 * Created by Vishwas on 4/27/16.
 */
public class IPAddressDetails implements Serializable {

    private String deviceIpAddress;

    private String deviceName;

    private String deviceStatus;

    public String getDeviceIpAddress() {
        return deviceIpAddress;
    }

    public void setDeviceIpAddress(String deviceIpAddress) {
        this.deviceIpAddress = deviceIpAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    @Override
    public String toString() {
        return "IPAddressDetails{" +
                "deviceIpAddress='" + deviceIpAddress + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceStatus='" + deviceStatus + '\'' +
                '}';
    }
}
