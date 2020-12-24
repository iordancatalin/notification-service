package com.icode.notificationservice.repository;

import com.icode.notificationservice.document.Notification;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<Notification> watchNotifications(String to) {
        final var criteria = new Criteria().andOperator(
                Criteria.where("to").is(to),
                Criteria.where("markedAsRead").is(false)
        );
        final var query = new Query(criteria);

        return reactiveMongoTemplate.tail(query, Notification.class);
    }

    public Mono<Notification> save(Notification notification) {
        return reactiveMongoTemplate.save(notification);
    }

    public Mono<UpdateResult> markNotificationAsRead(ObjectId id) {
        final var query = new Query(Criteria.where("id").is(id));
        final var update = new Update();

        update.set("markedAsRead", true);

        return reactiveMongoTemplate.updateFirst(query, update, Notification.class);
    }
}
