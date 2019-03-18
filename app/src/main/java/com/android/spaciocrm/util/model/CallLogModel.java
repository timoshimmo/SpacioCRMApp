package com.android.spaciocrm.util.model;

public class CallLogModel {

    private String callType;
    private String callDateTime;
    private String callDuration;

    public CallLogModel() {
    }

    public String getCallType() {
        return callType;
    }

    public String getCallDateTime() {
        return callDateTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallType(String cTYpe) {
        this.callType = cTYpe;
    }

    public void setCallDateTime(String cDateTime) {
        this.callDateTime = cDateTime;
    }

    public void setCallDuration(String cDuration) {
        this.callDuration = cDuration;
    }

}
