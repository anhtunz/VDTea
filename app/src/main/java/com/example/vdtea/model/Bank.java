package com.example.vdtea.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bank {
    @SerializedName("code")
    private String code;
    @SerializedName("desc")
    private String desc;
    @SerializedName("data")
    private List<Data> data;

    public Bank() {
    }

    public Bank(String code, String desc, List<Data> data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("name")
        private String bankName;
        @SerializedName("logo")
        private String bankLogo;
        @SerializedName("short_name")
        private String bankShortName;

        public Data() {
        }

        public Data(String bankName, String bankLogo, String bankShortName) {
            this.bankName = bankName;
            this.bankLogo = bankLogo;
            this.bankShortName = bankShortName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankLogo() {
            return bankLogo;
        }

        public void setBankLogo(String bankLogo) {
            this.bankLogo = bankLogo;
        }

        public String getBankShortName() {
            return bankShortName;
        }

        public void setBankShortName(String bankShortName) {
            this.bankShortName = bankShortName;
        }
    }
}
