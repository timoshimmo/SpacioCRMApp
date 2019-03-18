package com.android.spaciocrm.util.model;

/**
 * Created by freshfuturesmy on 13/02/18.
 */

public class UsersModel {

    private String USERNAME;
    private String PASSWORD;
    private String FIRST_NAME;
    private String LAST_NAME;
    private String EMAIL;
    private String MOBILE;
    private String DATE_OF_BIRTH;
    private String GENDER;
    private int POSITION;

    public UsersModel() {
    }

    public String getUsername() {
        return USERNAME;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public String getFirstname() {
        return FIRST_NAME;
    }

    public String getLastName() {
        return LAST_NAME;
    }

    public String getUserEmail() {
        return EMAIL;
    }

    public String getMobile() {
        return MOBILE;
    }

    public String getBirthdate() {
        return DATE_OF_BIRTH;
    }

    public String getGender() {
        return GENDER;
    }

    public int getUserPosition() {
        return POSITION;
    }

}
