package com.android.spaciocrm.util.model;

/**
 * Created by freshfuturesmy on 22/01/18.
 */

public class CountryModel {

    private String mCtryName;
    private String mCtryCode;

    public CountryModel(String name, String code) {
        mCtryName = name;
        mCtryCode = code;
    }

    public CountryModel() {
        super();
    }

    public String getCtryName() {
        return mCtryName;
    }

    public String getCtryCode() {
        return mCtryCode;
    }

    public void setCtryName(String name) {
        this.mCtryName = name;
    }

}
