package com.android.spaciocrm.util.model;

/**
 * Created by freshfuturesmy on 24/01/18.
 */

public class ProductsModel {

    private String PRODUCT_TITLE;
    private String PRODUCT_DESCRIPTION;
    private String PRODUCT_EMAIL;

    public ProductsModel(String name) {
        PRODUCT_TITLE = name;
    }

    public ProductsModel() {
    }

    public String getProductTitle() {
        return PRODUCT_TITLE;
    }

    public String getProductDesc() {
        return PRODUCT_DESCRIPTION;
    }

    public String getProductEmail() {
        return PRODUCT_EMAIL;
    }

}
