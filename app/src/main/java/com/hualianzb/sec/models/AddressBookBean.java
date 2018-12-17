package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2018/10/17
 * auther:wangtianyun
 * describe:
 */
public class AddressBookBean implements Serializable {
    public AddressBookBean() {
    }

    private String name;
    private String address;
    private String phone;
    private String email;
    private String remarks;
    private String creatTime;

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
