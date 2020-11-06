package com.bac.bacplatform.old.module.more;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.commonlib.domain
 * 创建人：Wjz
 * 创建时间：2016/9/11
 * 类描述：
 */
public class TitleAndIconBean {
    private String title;
    private int    img;
    private String content;
    private int    index;

    public TitleAndIconBean(String title, int img, int index) {
        this.title = title;
        this.img = img;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public TitleAndIconBean(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
