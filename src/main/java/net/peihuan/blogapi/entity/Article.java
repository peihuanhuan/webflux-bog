package net.peihuan.blogapi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(value = "article")
public class Article {
    @Id
    private Long id;

    private String title;

    private String content;

    private List<String> categories;

    private List<String> tags;

    private Date createTime;

    private Date updateTime;

    private Boolean deleted;
}
