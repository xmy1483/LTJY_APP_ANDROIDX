package com.bac.commonlib.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.commonlib.domain
 * 创建人：Wjz
 * 创建时间：2017/4/10
 * 类描述：
 */

public class BacHttpBean {

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 提交参数，和，返回数据
     */
    private List<Map<String, Object>> listMap = new ArrayList<>();


    private Map<String, List<Map<String, Object>>> mapData = new HashMap<String, List<Map<String, Object>>>();


    /**
     * 0=查询
     * 1=执行
     */
    private int actionType = 0;

    /**
     * 信息提示
     */
    private String msg;

    /**
     * 错误码
     */
    private int errorId = 0;

    /**
     * token
     */
    private String token;

    /**
     * 无参数添加
     *
     * @return
     */
    public BacHttpBean addMap() {
        listMap.add(new HashMap<String, Object>());
        return this;
    }

    /**
     * Map添加
     *
     * @param map
     * @return
     */
    public BacHttpBean addMap(Map<String, Object> map) {
        listMap.add(map);
        return this;
    }

    /**
     * put 参数
     *
     * @param key
     * @param value
     * @return
     */
    public BacHttpBean put(String key, Object value) {
        if (listMap.size() == 0) {
            addMap();
        }
        listMap.get(listMap.size() - 1)
                .put(key, value);
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public BacHttpBean setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public List<Map<String, Object>> getListMap() {
        return listMap;
    }

    public void setListMap(List<Map<String, Object>> listMap) {
        this.listMap = listMap;
    }

    public int getActionType() {
        return actionType;
    }

    public BacHttpBean setActionType(int actionType) {
        this.actionType = actionType;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, List<Map<String, Object>>> getMapData() {
        return mapData;
    }

    public void setMapData(Map<String, List<Map<String, Object>>> mapData) {
        this.mapData = mapData;
    }
}
