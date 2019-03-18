package com.android.spaciocrm.util.model;

/**
 * Created by freshfuturesmy on 06/02/18.
 */

public class ContactProductsModel {

    private String CONTACT_NAME;
    private String[] PRODUCTS;

    public ContactProductsModel() {
    }

    public ContactProductsModel(String[] pr) {
        this.PRODUCTS = pr;
    }

    public String getContactName() {
        return CONTACT_NAME;
    }

    public String[] getProducts() {
        return PRODUCTS;
    }
}
