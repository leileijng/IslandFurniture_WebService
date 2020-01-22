/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package HelperClasses;

import java.util.Date;
import java.util.List;

public class MessageHelper {
    private Long messageId;
    private String senderName;
    private String senderEmail;
    private List<String> receiversName;
    private List<String> receiversEmail;
    private String message;
    private Date sentDate;
    private Boolean messageRead;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public List<String> getReceiversName() {
        return receiversName;
    }

    public void setReceiversName(List<String> receiversName) {
        this.receiversName = receiversName;
    }

    public List<String> getReceiversEmail() {
        return receiversEmail;
    }

    public void setReceiversEmail(List<String> receiversEmail) {
        this.receiversEmail = receiversEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Boolean isMessageRead() {
        return messageRead;
    }

    public void setMessageRead(Boolean messageRead) {
        this.messageRead = messageRead;
    }
}
