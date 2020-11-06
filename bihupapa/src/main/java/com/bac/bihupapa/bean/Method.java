package com.bac.bihupapa.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Method implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8393135805230764075L;
    private String token;
    /**
     * 方法名称
     */
    private String methodName;

    /**
     * map数据
     * map,listMap,mapData 三个同时只会有一个存在
     */
    private Map<String, Object> map = new HashMap<String, Object>();
    /**
     * 提交参数，和，返回数据
     */
    private List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();


    private Map<String, List<Map<String, Object>>> mapData = new HashMap<String, List<Map<String, Object>>>();

    /**
     * 信息提示
     */
    private String msg;

    /**
     * 错误码
     */
    private int errorId=0;


    public boolean getSuccess(){
        return errorId==0;
    }

    public Method addMap() {
        listMap.add(new HashMap<String, Object>());
        return this;
    }
    public Method addMap(Map<String, Object> map) {
        listMap.add(map);
        return this;
    }



    public Method put(String key, Object value) {
        if(listMap.size()==0) {
            addMap();
        }
        Map<String, Object> map=listMap.get(listMap.size()-1);
        map.put(key, value);
        return this;
    }

    public static final int QUERY=0;
    public static final int OK=0;


    //	public static final int NO_LOGIN_ERROR=1;
//	public static final int AES_ENCRYPT_ERROR=2;
//	public static final int AES_DECRYPT_ERROR=3;
//	public static final int ZIP_ZIP_ERROR=4;
//	public static final int ZIP_UNZIP_ERROR=5;
//	public static final int NO_METHOD_NAME_ERROR=6;
//	public static final int QUERY_ERROR=7;
//	public static final int EXEC_ERROR=8;
//	public static final int JSON_ERROR=9;
//	public static final int NO_USER_MD5_KEY=10;
//	public static final int SIGN_ERROR=11;
//	public static final int RSA_ERROR=12;
//	public static final int FILTER_ERROR = 13;
//	public static final int TOKEN_ERROR=14;
    public static final int TOKEN_ERROR=14;


    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public List<Map<String, Object>> getListMap() {
        return listMap;
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




    public Map<String, List<Map<String, Object>>> getMapData() {
        return mapData;
    }

    public void setData(String name, List<Map<String, Object>> data) {
        this.mapData.put(name,data);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void setListMap(List<Map<String, Object>> listMap) {
        this.listMap = listMap;
    }

    public void setMapData(Map<String, List<Map<String, Object>>> mapData) {
        this.mapData = mapData;
    }
}