package net.peihuan.blogapi.repository;

import net.peihuan.blogapi.entity.Article;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ArticleRepository extends ReactiveCrudRepository<Article, Long> {
    Flux<Article> findAllByDeleted(Boolean deleted);
    Flux<Article> findAllByDeleted(Boolean deleted, Sort sort);
    Flux<Article> findAllById(Long id);
}
