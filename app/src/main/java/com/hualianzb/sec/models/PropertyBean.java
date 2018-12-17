package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2018/9/6
 * auther:wangtianyun
 * describe:
 */
public class PropertyBean implements Serializable {
    public PropertyBean() {
    }

    private String icon;
    private String name;
    private String allName;
    private String tokenAddress;
    private boolean checked;

    public String getAllName() {
        return allName;
    }

    public void setAllName(String allName) {
        this.allName = allName;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
