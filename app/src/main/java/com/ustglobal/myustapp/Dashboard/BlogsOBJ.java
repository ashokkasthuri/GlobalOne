package com.ustglobal.myustapp.Dashboard;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by Shubham Singhal.
 */

public class BlogsOBJ implements Serializable {

    private String SuperCategory;
    private JSONArray SuperCatg;

    public String getSuperCategory() {
        return SuperCategory;
    }

    public void setSuperCategory(String superCategory) {
        SuperCategory = superCategory;
    }

    public JSONArray getSuperCatg() {
        return SuperCatg;
    }

    public void setSuperCatg(JSONArray superCatg) {
        SuperCatg = superCatg;
    }
}
