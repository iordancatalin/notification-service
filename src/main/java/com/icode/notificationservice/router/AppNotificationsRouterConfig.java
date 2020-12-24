package com.icode.notificationservice.router;

import com.icode.notificationservice.model.NotificationRequest;
import com.icode.notificationservice.model.NotificationResponse;
import com.icode.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

@Configuration
@RequiredArgsConstructor
public class AppNotificationsRouterConfig {

    private final NotificationService notificationService;

    @Bean
    public RouterFunction<ServerResponse> appNotificationsRouter() {
        return nest(
                path("/api/v1"),
                route(GET("/notifications/{username}"), this::getNotifications)
                        .andRoute(POST("/notification"), this::createNotification)
                        .andRoute(PUT("/notification/mark-as-read/{id}"), this::markNotificationAsRead)
        );
    }

    private Mono<ServerResponse> markNotificationAsRead(ServerRequest serverRequest) {
        final var id = serverRequest.pathVariable("id");
        return notificationService.markNotificationAsRead(id).then(ServerResponse.ok().build());
    }

    private Mono<ServerResponse> getNotifications(ServerRequest serverRequest) {
        final var username = serverRequest.pathVariable("username");
        final var notifications = notificationService.getNotifications(username);

        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(notifications, NotificationResponse.class);
    }

    private Mono<ServerResponse> createNotification(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(NotificationRequest.class)
                .flatMap(notificationService::createNotification)
                .then(ServerResponse.ok().build());
    }
}
