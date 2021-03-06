package com.shujia.bean;

public class WeiBo {
    private String id;
    private String content;

    public WeiBo(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public WeiBo() {
    }

    @Override
    public String toString() {
        return "WeiBo{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
