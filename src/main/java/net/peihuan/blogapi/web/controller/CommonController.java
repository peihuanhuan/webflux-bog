package net.peihuan.blogapi.web.controller;

import net.peihuan.blogapi.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("common")
public class CommonController {


    @Autowired
    private CommonService commonService;

    @GetMapping(value = "count")
    public Mono<ResponseEntity> count() {
        return commonService.count();
    }

    @GetMapping(value = "count/article/category")
    public Mono<ResponseEntity> categoryCountArticle() {
        return commonService.categoryArticleCount();
    }

    @GetMapping(value = "count/article/tag")
    public Mono<ResponseEntity> tagCountArticle() {
        return commonService.tagArticleCount();
    }

    @GetMapping(value = "category")
    @Cacheable(value="getCategories")
    public Mono<ResponseEntity> category() {
        return commonService.category();
    }

    @GetMapping(value = "tag")
    @Cacheable(value="getTags")
    public Mono<ResponseEntity> tag() {
        return commonService.tag();
    }

}
