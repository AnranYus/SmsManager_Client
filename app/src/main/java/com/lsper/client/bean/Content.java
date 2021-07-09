package com.lsper.client.bean;

public class Content {
    String Content;
    String Type;
    long Date = System.currentTimeMillis();
    String SenderUUID;
    String RecipientUUID;
    String Origin;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Content() {
    }

    /**
     *
     * @param content 信息内容
     * @param type 信息类型
     * @param senderUUID 发送端UUIID
     * @param origin 客户端类型
     */
    public Content(String content, String type, String senderUUID, String origin) {
        Content = content;
        Type = type;
        SenderUUID = senderUUID;
        Origin = origin;
    }

    /**
     *
     * @param content 信息内容
     * @param type 信息类型
     * @param senderUUID 发送端UUID
     * @param recipientUUID 接收端UUID
     * @param origin 客户端类型
     */
    public Content(String content, String type, String senderUUID, String recipientUUID, String origin) {
        Content = content;
        Type = type;
        SenderUUID = senderUUID;
        RecipientUUID = recipientUUID;
        Origin = origin;
    }

    public long getDate() {
        return Date;
    }

    public String getSenderUUID() {
        return SenderUUID;
    }

    public void setSenderUUID(String senderUUID) {
        SenderUUID = senderUUID;
    }

    public String getRecipientUUID() {
        return RecipientUUID;
    }

    public void setRecipientUUID(String recipientUUID) {
        RecipientUUID = recipientUUID;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }
}
