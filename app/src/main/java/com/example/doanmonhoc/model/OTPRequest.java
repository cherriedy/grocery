package com.example.doanmonhoc.model;

import java.util.List;

public class OTPRequest {
    private List<Message> Messages;

    public OTPRequest(List<Message> messages) {
        this.Messages = messages;
    }

    public static class Message {
        private From From;
        private List<To> To;
        private String Subject;
        private String TextPart;
        private String HTMLPart;

        public Message(From from, List<To> to, String subject, String textPart, String htmlPart) {
            this.From = from;
            this.To = to;
            this.Subject = subject;
            this.TextPart = textPart;
            this.HTMLPart = htmlPart;
        }
    }

    public static class From {
        private String Email;
        private String Name;

        public From(String email, String name) {
            this.Email = email;
            this.Name = name;
        }
    }

    public static class To {
        private String Email;

        public To(String email) {
            this.Email = email;
        }
    }
}
