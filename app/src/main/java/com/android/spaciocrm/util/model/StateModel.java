package com.android.spaciocrm.util.model;

/**
 * Created by freshfuturesmy on 23/01/18.
 */

public class StateModel {

    private String mStatetName;

    public StateModel(String name) {
        mStatetName = name;
    }

    public StateModel() {
        super();
    }

    public String getStateName() {
        return mStatetName;
    }

    public void setStateName(String name) {
        this.mStatetName = name;
    }

}
