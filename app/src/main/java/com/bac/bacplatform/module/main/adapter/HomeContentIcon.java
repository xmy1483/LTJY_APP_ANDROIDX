package com.bac.bacplatform.module.main.adapter;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.commonlib.domain
 * 创建人：Wjz
 * 创建时间：2016/9/11
 * 类描述：
 */
public class HomeContentIcon {
     String content;
    static String money;
    int label;

    public HomeContentIcon(String content, String money, int label) {
        this.content = content;
        this.money = money;
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
