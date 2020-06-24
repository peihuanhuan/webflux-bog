package net.peihuan.blogapi.enums;

public enum  ArticleStatus {
    NORMAL("正常"),
    DRAFT("草稿"),
    NO_DISPLAYED("首页不展示"),
    HIDE("完全隐藏");

    private String desc;

    ArticleStatus(String desc) {
        this.desc = desc;
    }
}
