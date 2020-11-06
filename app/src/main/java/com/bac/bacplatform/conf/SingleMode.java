package com.bac.bacplatform.conf;

/**
 * Created by chenjie on 2018/4/27.
 */

public class SingleMode {
    //构造方法私有化，这样外界就不能访问了
    private SingleMode(){
    };
    //当类被初始化的时候，就直接new出来
    private static SingleMode instance = new SingleMode();
    //提供一个方法，给他人调用
    public static SingleMode getInstance(){
        return instance;
    }

    public boolean is_hasmakecard() {
        return is_hasmakecard;
    }

    public void setIs_hasmakecard(boolean is_hasmakecard) {
        this.is_hasmakecard = is_hasmakecard;
    }

    private boolean is_hasmakecard;

}
