package com.xavelo.template.render.api.application.port.out;

public interface SendEmailPort {
    void sendEmail(String from, String to, String subject, String content);
}
