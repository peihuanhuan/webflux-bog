package net.peihuan.blogapi.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class LoginForm {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
