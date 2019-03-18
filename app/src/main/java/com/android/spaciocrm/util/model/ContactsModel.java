package com.android.spaciocrm.util.model;

/**
 * Created by freshfuturesmy on 12/01/18.
 */

public class ContactsModel {

    private String CONTACT_TYPE;
    private String CONTACT_OPEN_CLOSE;
    private String CONTACT_FIRST_NAME;
    private String CONTACT_LAST_NAME;
    private String CONTACT_EMAIL;
    private String CONTACT_COMPANY_NAME;
    private String CONTACT_MOBILE;
    private String CONTACT_ADDRESS_TYPE;
    private String CONTACT_ADDRESS;
    private String CONTACT_CITY;
    private String CONTACT_STATE;
    private String CONTACT_COUNTRY;
    private String NEAREST_LANDMARK;

    public ContactsModel(String name, String mobile) {
        CONTACT_FIRST_NAME = name;
        CONTACT_MOBILE = mobile;
    }

    public ContactsModel(String fullname) {
        CONTACT_FIRST_NAME = fullname;
    }

    public ContactsModel() {
    }

    public String getContactType() {
        return CONTACT_TYPE;
    }

    public String getContactOpenClosed() {
        return CONTACT_OPEN_CLOSE;
    }

    public String getContactFirstName() {
        return CONTACT_FIRST_NAME;
    }

    public String getContactLastName() {
        return CONTACT_LAST_NAME;
    }

    public String getContactEmail() {
        return CONTACT_EMAIL;
    }

    public String getCompanyName() {
        return CONTACT_COMPANY_NAME;
    }

    public String getContactMobile() {
        return CONTACT_MOBILE;
    }

    public void setContactName(String name) {
        this.CONTACT_FIRST_NAME = name;
    }

    public String getContactAddyType() {
        return CONTACT_ADDRESS_TYPE;
    }

    public String getContactAddresse() {
        return CONTACT_ADDRESS;
    }

    public String getLocationCity() {
        return CONTACT_CITY;
    }

    public String getLocationState() {
        return CONTACT_STATE;
    }

    public String getLocationCountry() {
        return CONTACT_COUNTRY;
    }

    public String getNearestLandmark() {
        return NEAREST_LANDMARK;
    }

}
