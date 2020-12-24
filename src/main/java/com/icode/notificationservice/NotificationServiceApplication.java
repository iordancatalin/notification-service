package com.icode.notificationservice;

import com.icode.notificationservice.document.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@SpringBootApplication
@RequiredArgsConstructor
public class NotificationServiceApplication {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {

        final var collectionOptions = CollectionOptions.empty()
                .size(10_000)
                .maxDocuments(100_000)
                .capped();

        final var createNotificationCollection = reactiveMongoTemplate.createCollection(Notification.class, collectionOptions);

        reactiveMongoTemplate.collectionExists(Notification.class)
                .filter(Boolean.FALSE::equals)
                .flatMap(unused -> createNotificationCollection)
                .subscribe();
    }
}
