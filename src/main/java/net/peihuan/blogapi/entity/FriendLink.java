package net.peihuan.blogapi.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "friend_link")
public class FriendLink {
    private String name;
    private String url;

}