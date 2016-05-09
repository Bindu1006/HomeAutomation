package com.example.shruti.homeautomation;

/**
 * Created by Shruti on 5/8/16.
 */
public class PubNubKeysBO {

    private String pubKey;

    private String subKey;

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getSubKey() {
        return subKey;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    @Override
    public String toString() {
        return "PubNubKeysBO{" +
                "pubKey='" + pubKey + '\'' +
                ", subKey='" + subKey + '\'' +
                '}';
    }
}
