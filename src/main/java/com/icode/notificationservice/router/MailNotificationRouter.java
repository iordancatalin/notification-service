package com.icode.notificationservice.router;

import com.icode.notificationservice.model.EmailConfirmationModel;
import com.icode.notificationservice.model.ResetPasswordModel;
import com.icode.notificationservice.service.MailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

@Configuration
public class MailNotificationRouter {

    private final MailService mailService;

    public MailNotificationRouter(MailService mailService) {
        this.mailService = mailService;
    }

    @Bean
    public RouterFunction<ServerResponse> emailNotificationRouterConfig() {
        return nest(path("/api/v1"),
                route(POST("/email-confirmation"), this::handleEmailConfirmationRequest)
                        .andRoute(POST("/reset-password"), this::handleResetPasswordRequest)
        );
    }

    private Mono<ServerResponse> handleEmailConfirmationRequest(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EmailConfirmationModel.class)
                .doOnNext(mailService::sendConfirmationEmail)
                .flatMap(unused -> ServerResponse.ok().build());
    }

    private Mono<ServerResponse> handleResetPasswordRequest(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ResetPasswordModel.class)
                .doOnNext(mailService::sendResetPasswordMail)
                .flatMap(unused -> ServerResponse.ok().build());
    }
}
