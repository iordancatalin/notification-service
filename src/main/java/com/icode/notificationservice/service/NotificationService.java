package com.icode.notificationservice.service;

import com.icode.notificationservice.document.Notification;
import com.icode.notificationservice.model.NotificationRequest;
import com.icode.notificationservice.model.NotificationResponse;
import com.icode.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Flux<NotificationResponse> getNotifications(String username) {
        return notificationRepository.watchNotifications(username)
                .map(this::buildNotificationResponse);
    }

    public Mono<Notification> createNotification(NotificationRequest notificationRequest) {
        final var notification = Notification.builder()
                .from(notificationRequest.getFrom())
                .to(notificationRequest.getTo())
                .projectName(notificationRequest.getProjectName())
                .markedAsRead(false)
                .build();

        return notificationRepository.save(notification);
    }

    public Mono<Void> markNotificationAsRead(String id) {
        final var notificationId = new ObjectId(id);
        return notificationRepository.markNotificationAsRead(notificationId).then();
    }

    private NotificationResponse buildNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId().toString())
                .from(notification.getFrom())
                .projectName(notification.getProjectName())
                .markedAsRead(notification.isMarkedAsRead())
                .build();
    }
}
