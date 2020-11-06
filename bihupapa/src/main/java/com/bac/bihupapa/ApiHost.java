package com.bac.bihupapa;

public class ApiHost {
    // 正式服
    public static final String SCHEME = "https://";
    public static final String HOST = "app5.bac365.com";
    public static final String HTTP_PORT = "10443";
    public static final int RPC_PORT = 3000;
    public static final String HOST_PORT = HOST+":"+HTTP_PORT;

    // 测试服
//    public static final String SCHEME = "http://";
//    public static final String HOST = "intranet.camel.bac365.com";
//    public static final String HTTP_PORT = "20002";
//    public static final int RPC_PORT = 20098;
//    public static final String HOST_PORT = HOST+":"+HTTP_PORT;
}
