package net.peihuan.blogapi.repository;

import net.peihuan.blogapi.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, ObjectId> {
    Mono<User> findByUserName(String userName);
}
