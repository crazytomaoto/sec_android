package com.hualianzb.sec.models;

import java.io.Serializable;

public class MnemonicsBean implements Serializable {
    public MnemonicsBean() {
    }

    private String mnemonicsItem;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getMnemonicsItem() {
        return mnemonicsItem;
    }

    public void setMnemonicsItem(String mnemonicsItem) {
        this.mnemonicsItem = mnemonicsItem;
    }
}
