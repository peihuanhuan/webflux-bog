package net.peihuan.blogapi.form;

import lombok.Data;

import java.util.List;

@Data
public class PostArticleForm {
    private Long id;
    private String title;
    private String content;
    private List<String> categories;
    private List<String> tags;
}
