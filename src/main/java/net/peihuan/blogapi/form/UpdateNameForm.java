package net.peihuan.blogapi.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateNameForm {
    @NotBlank
    private String oldName;
    @NotBlank
    private String newName;
}
