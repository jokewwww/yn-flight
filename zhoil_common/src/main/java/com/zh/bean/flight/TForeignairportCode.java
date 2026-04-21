package com.zh.bean.flight;

public class TForeignairportCode {
    private String id;

    private String alcdIcaoCode;

    private String alcdArlnName;

    private String cstmNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAlcdIcaoCode() {
        return alcdIcaoCode;
    }

    public void setAlcdIcaoCode(String alcdIcaoCode) {
        this.alcdIcaoCode = alcdIcaoCode == null ? null : alcdIcaoCode.trim();
    }

    public String getAlcdArlnName() {
        return alcdArlnName;
    }

    public void setAlcdArlnName(String alcdArlnName) {
        this.alcdArlnName = alcdArlnName == null ? null : alcdArlnName.trim();
    }

    public String getCstmNum() {
        return cstmNum;
    }

    public void setCstmNum(String cstmNum) {
        this.cstmNum = cstmNum == null ? null : cstmNum.trim();
    }
}