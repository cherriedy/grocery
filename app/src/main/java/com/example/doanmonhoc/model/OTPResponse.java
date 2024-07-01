package com.example.doanmonhoc.model;

import java.util.List;

public class OTPResponse {
    private List<Message> Messages;

    public List<Message> getMessages() {
        return Messages;
    }

    public void setMessages(List<Message> messages) {
        this.Messages = messages;
    }

    public static class Message {
        private String Status;
        private String CustomID;
        private List<Recipient> To;

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            this.Status = status;
        }

        public String getCustomID() {
            return CustomID;
        }

        public void setCustomID(String customID) {
            this.CustomID = customID;
        }

        public List<Recipient> getTo() {
            return To;
        }

        public void setTo(List<Recipient> to) {
            this.To = to;
        }
    }

    public static class Recipient {
        private String Email;
        private String MessageUUID;
        private long MessageID;
        private String MessageHref;

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            this.Email = email;
        }

        public String getMessageUUID() {
            return MessageUUID;
        }

        public void setMessageUUID(String messageUUID) {
            this.MessageUUID = messageUUID;
        }

        public long getMessageID() {
            return MessageID;
        }

        public void setMessageID(long messageID) {
            this.MessageID = messageID;
        }

        public String getMessageHref() {
            return MessageHref;
        }

        public void setMessageHref(String messageHref) {
            this.MessageHref = messageHref;
        }
    }
}
