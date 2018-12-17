package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2018/8/28
 * auther:wangtianyun
 * describe:
 */
public class TokenBean implements Serializable {
    public TokenBean() {
    }

    private String name;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == null && obj == null)
            return true;
        if (this == null || obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        TokenBean a = (TokenBean) obj;
        if (this.name.equals(a.name) && this.token.equals(a.token))
            return true;
        if (!this.name.equals(a.name) || !this.token.equals(a.token))
            return false;
        return false;
    }
}
