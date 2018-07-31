package com.jeeva.sms.data.dto;

/**
 * Created by jeeva on 16/12/17.
 */
public class Sms {

    private long smsId;

    private String senderId;

    private String messageBody;

    private long receivedDate;

    public Sms() {
    }

    public Sms(long smsId, String senderId, String messageBody, long receivedDate) {
        this.smsId = smsId;
        this.senderId = senderId;
        this.messageBody = messageBody;
        this.receivedDate = receivedDate;
    }

    public Sms(String senderId, String messageBody, long receivedDate) {
        this.senderId = senderId;
        this.messageBody = messageBody;
        this.receivedDate = receivedDate;
    }

    public long getSmsId() {
        return smsId;
    }

    public void setSmsId(long smsId) {
        this.smsId = smsId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public long getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(long receivedDate) {
        this.receivedDate = receivedDate;
    }
}