package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2018/9/21
 * auther:wangtianyun
 * describe:
 */
public class WalletKindBean implements Serializable {
    public WalletKindBean() {
    }

    private String name;
    private boolean isChecked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
