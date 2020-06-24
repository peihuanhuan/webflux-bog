package net.peihuan.blogapi.service;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.peihuan.blogapi.entity.Article;
import net.peihuan.blogapi.form.UpdateNameForm;
import net.peihuan.blogapi.repository.ArticleRepository;
import net.peihuan.blogapi.repository.FriendLinkRepository;
import net.peihuan.blogapi.vo.CountArticleVO;
import net.peihuan.blogapi.vo.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;


@Service
@Slf4j
public class CommonService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private FriendLinkRepository friendLinkRepository;
    @Autowired
    private FileService fileService;

    public Mono<ResponseEntity> count() {
        Flux<Article> articles = articleRepository.findAll();
        Mono<Long> articleCount = articles.count();
        Mono<Long> categoryCount = articles.flatMap(article -> Flux.fromIterable(article.getCategories())).distinct().count();
        Mono<Long> tagCount = articles.flatMap(article -> Flux.fromIterable(article.getTags())).distinct().count();
        Mono<Long> linkCount = friendLinkRepository.count();
        Mono<Long> fileCount = Mono.just(fileService.countFiles());

        return Mono.zip(articleCount, categoryCount, tagCount, linkCount, fileCount).map(tuple -> {
            Long article = tuple.getT1();
            Long category = tuple.getT2();
            Long tag = tuple.getT3();
            Long link = tuple.getT4();
            Long file = tuple.getT5();

            RestResult result = new RestResult();
            result.add("article", article);
            result.add("category", category);
            result.add("tag", tag);
            result.add("link", link);
            result.add("file", file);

            return ResponseEntity.ok().body(result);
        });
    }

    public Mono<ResponseEntity> categoryArticleCount() {
        return articleRepository.findAll()
                .flatMap(article -> Flux.fromIterable(article.getCategories()))
                .groupBy(String::toString)
                .flatMap(grouped -> Mono.just(Objects.requireNonNull(grouped.key())).zipWith(grouped.count()))
                .map(tuple2 -> {
                    CountArticleVO categoryArticle = new CountArticleVO();
                    categoryArticle.setName(tuple2.getT1());
                    categoryArticle.setCount(tuple2.getT2());
                    return categoryArticle;
                })
                .collectList()
                .map(list -> ResponseEntity.ok().body(new RestResult("info", list)));
    }


    public static void main(String[] args) throws InterruptedException {
        Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(1))
                .subscribe(i -> {

                    System.out.println(i);
                    System.out.println(Thread.currentThread().getName());
                });

        Thread.sleep(1100000);
    }


    public Mono<ResponseEntity> tagArticleCount() {
        return articleRepository.findAll()
                .flatMap(article -> Flux.fromIterable(article.getTags()))
                .groupBy(String::toString)
                .flatMap(grouped -> Mono.just(Objects.requireNonNull(grouped.key())).zipWith(grouped.count()))
                .map(tuple2 -> {
                    CountArticleVO tagArticle = new CountArticleVO();
                    tagArticle.setName(tuple2.getT1());
                    tagArticle.setCount(tuple2.getT2());
                    return tagArticle;
                })
                .collectList()
                .map(list -> ResponseEntity.ok().body(new RestResult("info", list)));
    }


    public Mono<ResponseEntity> category() {
        log.info("-----未走 分类 缓存");
        return articleRepository.findAll()
                .flatMap(article -> Flux.fromIterable(article.getCategories()))
                .distinct()
                .collectList()
                .map(list -> ResponseEntity.ok().body(new RestResult("info", list)));
    }


    public Mono<ResponseEntity> tag() {
        log.info("-----未走 标签 缓存");
        return articleRepository.findAll()
                .flatMap(article -> Flux.fromIterable(article.getTags()))
                .distinct()
                .collectList()
                .map(list -> ResponseEntity.ok().body(new RestResult("info", list)));
    }

    //@CacheEvict(value = "getCategorys")
    //public void updateCategory(UpdateNameForm form) {
    //    if (form.getNewName().equals(form.getOldName())) {
    //        return;
    //    }
    //    articleRepository.findAll()
    //            .map(Article::getCategories)
    //            .hasElement(form.getNewName())
    //            .doOnNext(b -> {
    //                if (b) {
    //                    System.out.println("重复");
    //                } else {
    //
    //                }
    //            });
    //            //.onerror(b -> {
    //            //    return ResponseEntity.ok().body(new RestResult());
    //            //});
    //
    //
    //    articleRepository.findAllByCategoryName(form.getOldName())
    //            .subscribe(article -> {
    //                article.setCategoryName(form.getNewName());
    //                articleRepository.save(article);
    //            });
    //}

}