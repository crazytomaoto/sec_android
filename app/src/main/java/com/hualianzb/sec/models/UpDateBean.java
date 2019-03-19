package com.hualianzb.sec.models;

import java.io.Serializable;

/**
 * Date:2019/1/17
 * auther:wangtianyun
 * describe:检测更新版本
 */
public class UpDateBean implements Serializable {
    /**
     * version : 1.0.0
     * describ : 1. First Version of Mobile SEC Wallet
     * status : 1
     * link : https://github.com/SEC-Block/secblock-client/releases/download/1.2.52/SEC_Client.Setup.1.2.52.exe
     * android : {"version":"1.0.0","describ":"1. First Version of Mobile SEC Wallet","status":"2","link":"https://github.com/SEC-Block/secblock-client/releases/download/1.2.52/SEC_Client.Setup.1.2.52.exe","platform":"android"}
     * ios : {"version":"1.0.0","describ":"1. First Version of Mobile SEC Wallet","status":"3","link":"https://github.com/SEC-Block/secblock-client/releases/download/1.2.52/SEC_Client.Setup.1.2.52.exe","platform":"ios"}
     */

    private String version;
    private String describ;
    private String status;
    private String link;
    private AndroidBean android;
    private IosBean ios;

    public UpDateBean() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AndroidBean getAndroid() {
        return android;
    }

    public void setAndroid(AndroidBean android) {
        this.android = android;
    }

    public IosBean getIos() {
        return ios;
    }

    public void setIos(IosBean ios) {
        this.ios = ios;
    }

    public static class AndroidBean implements Serializable {
        /**
         * version : 1.0.0
         * describ : 1. First Version of Mobile SEC Wallet
         * status : 2
         * link : https://github.com/SEC-Block/secblock-client/releases/download/1.2.52/SEC_Client.Setup.1.2.52.exe
         * platform : android
         */

        private String version;
        private String describ;
        private String status;
        private String link;
        private String platform;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescrib() {
            return describ;
        }

        public void setDescrib(String describ) {
            this.describ = describ;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }
    }

    public static class IosBean implements Serializable {
        /**
         * version : 1.0.0
         * describ : 1. First Version of Mobile SEC Wallet
         * status : 3
         * link : https://github.com/SEC-Block/secblock-client/releases/download/1.2.52/SEC_Client.Setup.1.2.52.exe
         * platform : ios
         */

        private String version;
        private String describ;
        private String status;
        private String link;
        private String platform;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescrib() {
            return describ;
        }

        public void setDescrib(String describ) {
            this.describ = describ;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }
    }
}
