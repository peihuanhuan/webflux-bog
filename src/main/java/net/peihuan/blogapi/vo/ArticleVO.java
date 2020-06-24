package net.peihuan.blogapi.vo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ArticleVO {

    private Long id;

    private String title;

    private String categories;

    private String tags;

    private String createTime;

    private String updateTime;

}
