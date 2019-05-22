package com.ustglobal.myustapp.NavigationDrawer;

/**
 * Created by Shubham Singhal.
 */

public class NavDrawerModel {
    private boolean showNotify;
    private String title;
    private int icon;

    public NavDrawerModel() {

    }

    public NavDrawerModel(boolean showNotify, String title, int icon) {
        this.showNotify = showNotify;
        this.title = title;
        this.icon = icon;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIocn(int icon) {
        this.icon = icon;
    }
}
