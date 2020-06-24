package net.peihuan.blogapi.repository;

import net.peihuan.blogapi.entity.FriendLink;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FriendLinkRepository extends ReactiveCrudRepository<FriendLink, Long> {
}
