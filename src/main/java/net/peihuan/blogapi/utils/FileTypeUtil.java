package net.peihuan.blogapi.utils;

import net.peihuan.blogapi.enums.FileType;
import org.apache.commons.io.FilenameUtils;

import static net.peihuan.blogapi.enums.FileType.ARCHIVE;
import static net.peihuan.blogapi.enums.FileType.DOCUMENT;
import static net.peihuan.blogapi.enums.FileType.IMAGE;
import static net.peihuan.blogapi.enums.FileType.MUSIC;
import static net.peihuan.blogapi.enums.FileType.OTHERS;
import static net.peihuan.blogapi.enums.FileType.VIDEO;

public class FileTypeUtil {

    private static String[] img = {"bmp", "jpg", "jpeg", "png", "tiff", "gif", "svg", "psd", "raw", "wmf"};

    private static String[] document = {"txt", "doc", "docx", "xls", "htm", "html", "pdf", "ppt", "pptx"};

    private static String[] video = {"mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm"};

    private static String[] music = {"mp3", "wma", "wav", "mod", "ra", "cd", "md", "vqf", "ape", "mid", "ogg", "m4a"};

    private static String[] archive = {"rar", "zip", "cab", "iso", "jar", "ace", "7z", "tar", "gz", "arj", "lzh", "uue", "bz2", "z"};


    public static FileType fileType(String fileName) {
        if (fileName == null) {
            return OTHERS;
        } else {
            String fileType = FilenameUtils.getExtension(fileName).toLowerCase();
            for (String anImg : img ) {
                if (anImg.equals(fileType)) {
                    return IMAGE;
                }
            }
            for (String a : archive) {
                if (a.equals(fileType) ) {
                    return ARCHIVE;
                }
            }
            for (String aDocument : document) {
                if (aDocument.equals(fileType) ) {
                    return DOCUMENT;
                }
            }
            for (String aVideo : video) {
                if (aVideo.equals(fileType) ) {
                    return VIDEO;
                }
            }
            for (String aMusic : music) {
                if (aMusic.equals(fileType) ) {
                    return MUSIC;
                }
            }
        }
        return OTHERS;
    }
}
