package com.lsper.client.bean;

public class Sms {
    Long lateTime;
    String phoneNumber;
    String smsContent;
    String person;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Long getLateTime() {
        return lateTime;
    }

    public void setLateTime(Long lateTime) {
        this.lateTime = lateTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }
}
