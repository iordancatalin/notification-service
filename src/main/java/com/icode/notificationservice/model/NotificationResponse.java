package com.icode.notificationservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {

    private String id;
    private String from;
    private String projectName;
    private boolean markedAsRead;
}
