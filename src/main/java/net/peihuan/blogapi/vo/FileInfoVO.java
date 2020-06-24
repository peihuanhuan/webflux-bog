package net.peihuan.blogapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.peihuan.blogapi.enums.FileType;

@Data
@AllArgsConstructor
public class FileInfoVO {
    private String name;
    private Long len;
    private String modifyTime;
    private FileType type;
}
