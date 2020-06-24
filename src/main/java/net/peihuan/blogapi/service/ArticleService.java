package net.peihuan.blogapi.service;

import lombok.extern.slf4j.Slf4j;
import net.peihuan.blogapi.entity.Article;
import net.peihuan.blogapi.form.PostArticleForm;
import net.peihuan.blogapi.repository.ArticleRepository;
import net.peihuan.blogapi.vo.ArticleVO;
import net.peihuan.blogapi.vo.RestResult;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import static net.peihuan.blogapi.vo.ResponseAPIStatus.SAVE_DRAFT_ERROR;

@Service
@Slf4j
public class ArticleService {

    @Value("${path.hexo}")
    private String hexoRootPath;


    @Autowired
    private ArticleRepository articleRepository;

    public Mono<ResponseEntity<RestResult>> draft(PostArticleForm form) {
        if (form.getId() == null) {
            return saveNewArticleToMongo(form);
        } else {
            return updateArticleToMongo(form);
        }
    }

    public Mono<ResponseEntity<RestResult>> publish(PostArticleForm form) {
        if (form.getId() == null) {
            createArticleFile(form, DateTime.now());
            return saveNewArticleToMongo(form);
        } else {
            return updateArticleAndWriteFile(form);
        }
    }

    private Mono<ResponseEntity<RestResult>> saveNewArticleToMongo(PostArticleForm form) {
        long id = System.currentTimeMillis();
        Date now = new Date();
        Article article = new Article();
        BeanUtils.copyProperties(form, article);
        article.setId(id);
        article.setCreateTime(now);
        article.setUpdateTime(now);
        article.setDeleted(false);
        articleRepository.save(article).subscribe();
        return Mono.just(ResponseEntity.ok().body(new RestResult("id", id)));
    }

    private Mono<ResponseEntity<RestResult>> updateArticleToMongo(PostArticleForm form) {
        return articleRepository
                .findById(form.getId())
                .doOnNext(article -> {
                    BeanUtils.copyProperties(form, article);
                    article.setUpdateTime(new Date());
                    articleRepository.save(article).subscribe();
                })
                .thenReturn(ResponseEntity.ok().body(new RestResult()))
                .onErrorReturn(ResponseEntity.status(SAVE_DRAFT_ERROR.getHttpStatus()).body(new RestResult(SAVE_DRAFT_ERROR, "保存出错")));
    }

    private Mono<ResponseEntity<RestResult>> updateArticleAndWriteFile(PostArticleForm form) {
        return articleRepository
                .findById(form.getId())
                .doOnNext(article -> {
                    if (!article.getTitle().equals(form.getTitle())) {
                        deleteFile(article.getTitle());
                    }
                    createArticleFile(form, new DateTime(article.getCreateTime()));
                    BeanUtils.copyProperties(form, article);
                    article.setUpdateTime(new Date());
                    articleRepository.save(article).subscribe();
                })
                .thenReturn(ResponseEntity.ok().body(new RestResult()))
                .onErrorReturn(ResponseEntity.status(SAVE_DRAFT_ERROR.getHttpStatus()).body(new RestResult(SAVE_DRAFT_ERROR, "保存出错")));
    }

    private void createArticleFile(PostArticleForm form, DateTime createTime) {
        String content = generateHexoFileContent(form, createTime);
        writeFile(form.getTitle(), content);
    }

    private void createArticleFile(Article article) {
        String content = generateHexoFileContent(article);
        writeFile(article.getTitle(), content);
    }

    private void writeFile(String title, String content) {
        try {
            String pathName = getPathName(title);
            File file = new File(pathName);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException(String.format("---------------- 删除文件失败 %s ----------------", pathName));
                }
            }
            if (!file.createNewFile()) {
                throw new RuntimeException(String.format("---------------- 创建文件失败 %s ----------------", pathName));
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteFile(String title) {
        String pathName = getPathName(title);
        File file = new File(pathName);
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException(String.format("---------------- 删除文件失败 %s ----------------", pathName));
            }
        }
    }

    private String getPathName(String title) {
        return hexoRootPath + "/source/_posts/" + filenameFilter(title) + ".md";
    }

    private String generateHexoFileContent(PostArticleForm form, DateTime createTime) {
        return "---\n"
                + "title: " + form.getTitle() + "\n"
                + "date: " + createTime.toString("yyyy-MM-dd HH:mm:ss") + "\n"
                + "tags: " + form.getTags().toString() + "\n"
                + "categories: " + form.getCategories().toString() + "\n"
                + "---\n"
                + form.getContent();
    }
    private String generateHexoFileContent(Article article) {
        return "---\n"
                + "title: " + article.getTitle() + "\n"
                + "date: " + new DateTime(article.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss") + "\n"
                + "tags: " + article.getTags().toString() + "\n"
                + "categories: " + article.getCategories().toString() + "\n"
                + "---\n"
                + article.getContent();
    }

    public Flux list(Boolean deleted) {
        if (deleted == null) {
            deleted = false;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return articleRepository.findAllByDeleted(deleted, sort)
                .map(article -> ArticleVO.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .categories(Optional.ofNullable(article.getCategories()).orElse(Collections.emptyList()).toString().replace("[","").replace("]",""))
                        .tags(Optional.ofNullable(article.getTags()).orElse(Collections.emptyList()).toString().replace("[","").replace("]",""))
                        .createTime(new DateTime(article.getCreateTime()).toString("yyyy-MM-dd"))
                        .updateTime(new DateTime(article.getUpdateTime()).toString("yyyy-MM-dd"))
                        .build());
    }

    public Mono<Article> delete(Long id) {
        return articleRepository
                .findById(id)
                .doOnNext(article -> {
                    deleteFile(article.getTitle());
                    article.setDeleted(true);
                    articleRepository.save(article).subscribe();
                });
    }

    public Mono<ResponseEntity<RestResult>> updateAllFiles() {
        return articleRepository
                .findAllByDeleted(false)
                .doOnNext(this::createArticleFile)
                .then(Mono.just(ResponseEntity.ok(new RestResult())))
                .onErrorReturn(ResponseEntity.status(SAVE_DRAFT_ERROR.getHttpStatus()).body(new RestResult(SAVE_DRAFT_ERROR, "创建文件出错")));
    }

    public Mono<ResponseEntity<RestResult>> findArticle(Long id) {
        return articleRepository
                .findById(id)
                .map(article -> ResponseEntity.ok(new RestResult("article", article)));
    }

    private static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");
    public static String filenameFilter(String str) {
        return str==null ? null : FilePattern.matcher(str).replaceAll("");
    }


    public static void main(String[] args) {
        System.out.println(filenameFilter("*哈哈哈"));
    }
}
