package com.icode.notificationservice.document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@Builder
public class Notification {

    @Id
    @Setter(AccessLevel.NONE)
    private ObjectId id;

    private String from;
    private String to;
    private String projectName;
    private boolean markedAsRead;
}
