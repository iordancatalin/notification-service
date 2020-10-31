package com.icode.notificationservice.router;

import com.icode.notificationservice.model.EmailConfirmationModel;
import com.icode.notificationservice.service.MailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

@Configuration
public class EmailNotificationRouter {

    private final MailService mailService;

    public EmailNotificationRouter(MailService mailService) {
        this.mailService = mailService;
    }

    @Bean
    public RouterFunction<ServerResponse> emailNotificationRouterConfig() {
        return route(POST("/api/v1/email-confirmation"), this::handleEmailConfirmationRequest);
    }

    private Mono<ServerResponse> handleEmailConfirmationRequest(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EmailConfirmationModel.class)
                .doOnNext(mailService::sendConfirmationEmail)
                .flatMap(ignore -> ServerResponse.ok().build());
    }
}
