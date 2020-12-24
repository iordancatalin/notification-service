package com.icode.notificationservice.model;


import lombok.Data;

@Data
public class NotificationRequest {

    private String from;
    private String to;
    private String projectName;
}
