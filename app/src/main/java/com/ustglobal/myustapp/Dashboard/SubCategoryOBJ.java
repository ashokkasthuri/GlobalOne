package com.ustglobal.myustapp.Dashboard;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by Shubham Singhal.
 */

public class SubCategoryOBJ implements  Serializable {

    private String SubCategory;
    private JSONArray SubCatgData;


    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public JSONArray getSubCatgData() {
        return SubCatgData;
    }

    public void setSubCatgData(JSONArray subCatgData) {
        SubCatgData = subCatgData;
    }


}
