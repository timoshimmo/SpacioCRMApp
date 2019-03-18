package com.android.spaciocrm.util.model;

/**
 * Created by freshfuturesmy on 07/02/18.
 */

public class SalesPersonModel {

    private String SALES_FIRST_NAME;
    private String SALES_LAST_NAME;
    private String SALES_EMAIL;
    private String SALES_MOBILE;

    public SalesPersonModel() {
    }

    public String getFirstName() {
        return SALES_FIRST_NAME;
    }

    public String getLastName() {
        return SALES_LAST_NAME;
    }

    public String getSalesEmail() {
        return SALES_EMAIL;
    }

    public String getMobile() {
        return SALES_MOBILE;
    }
}
