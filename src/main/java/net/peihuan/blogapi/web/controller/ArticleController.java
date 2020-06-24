package net.peihuan.blogapi.web.controller;

import net.peihuan.blogapi.entity.Article;
import net.peihuan.blogapi.form.PostArticleForm;
import net.peihuan.blogapi.service.ArticleService;
import net.peihuan.blogapi.vo.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static net.peihuan.blogapi.vo.ResponseAPIStatus.SAVE_DRAFT_ERROR;


@RestController
@RequestMapping("article")
public class ArticleController {


    @Autowired
    private ArticleService articleService;


    @PostMapping("draft")
    public Mono<ResponseEntity<RestResult>> draft(@Valid PostArticleForm form, BindingResult bindingResult) {
        return articleService.draft(form);
    }

    @PostMapping("publish")
    @CacheEvict(value={"getCategories","getTags"}, allEntries = true)
    public Mono<ResponseEntity<RestResult>> publish(@Valid PostArticleForm form, BindingResult bindingResult) {
        return articleService.publish(form);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<RestResult>> delete(@PathVariable(name = "id") Long id) {
        return articleService.delete(id)
                .thenReturn(ResponseEntity.ok().body(new RestResult()))
                .onErrorReturn(ResponseEntity.status(SAVE_DRAFT_ERROR.getHttpStatus()).body(new RestResult(SAVE_DRAFT_ERROR, "删除出错")));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<RestResult>> get(@PathVariable(name = "id") Long id) {
        return articleService.findArticle(id);
    }

    @GetMapping(value = "list", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux list(Boolean deleted) {
        return articleService.list(deleted);
    }

    @PostMapping("update/file")
    public Mono<ResponseEntity<RestResult>> updateAllFiles() {
        return articleService.updateAllFiles();
    }



}
