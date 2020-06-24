package net.peihuan.blogapi.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "user")
public class User {
    private String userName;
    private String password;
}
